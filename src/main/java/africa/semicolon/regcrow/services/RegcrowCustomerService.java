package africa.semicolon.regcrow.services;

import africa.semicolon.regcrow.dtos.request.*;
import africa.semicolon.regcrow.dtos.response.ApiResponse;
import africa.semicolon.regcrow.dtos.response.CustomerRegistrationResponse;
import africa.semicolon.regcrow.dtos.response.CustomerResponse;
import africa.semicolon.regcrow.exceptions.*;
import africa.semicolon.regcrow.models.BankAccount;
import africa.semicolon.regcrow.models.BioData;
import africa.semicolon.regcrow.models.Customer;
import africa.semicolon.regcrow.repositories.CustomerRepository;
import africa.semicolon.regcrow.services.cloud.CloudService;
import africa.semicolon.regcrow.services.notification.mail.MailService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TextNode;
import com.github.fge.jackson.jsonpointer.JsonPointer;
import com.github.fge.jackson.jsonpointer.JsonPointerException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchOperation;
import com.github.fge.jsonpatch.ReplaceOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static africa.semicolon.regcrow.utils.AppUtils.*;
import static africa.semicolon.regcrow.utils.ExceptionUtils.*;
import static africa.semicolon.regcrow.utils.ResponseUtils.*;


@Service
@AllArgsConstructor
@Slf4j
public class RegcrowCustomerService implements CustomerService{
    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;
    private final CloudService cloudService;
    private final MailService mailService;


    @Override
    public CustomerRegistrationResponse register(CustomerRegistrationRequest customerRegistrationRequest) throws RegCrowException {
        BioData bioData = new BioData();
        modelMapper.map(customerRegistrationRequest, bioData);
        Customer customer=new Customer();
        customer.setBioData(bioData);
        customer.setBankAccount(new BankAccount());
        Customer savedCustomer=customerRepository.save(customer);
        EmailNotificationRequest emailNotificationRequest = buildEmailRequest(savedCustomer);
        var response = mailService.sendMail(emailNotificationRequest);
        log.info("response-->{}", response);
        boolean isSavedCustomer = savedCustomer.getId() != null;
        if (!isSavedCustomer) throw new CustomerRegistrationFailedException(String.format(USER_REGISTRATION_FAILED, customerRegistrationRequest.getEmail()));
        return buildRegisterCustomerResponse(savedCustomer.getId());
    }

    private EmailNotificationRequest buildEmailRequest(Customer customer) throws RegCrowException {
        String token = JWT.create()
                .withIssuedAt(Instant.now())
                .withExpiresAt(Instant.now().plusSeconds(60L))
                .sign(Algorithm.HMAC512("secret".getBytes()));
        EmailNotificationRequest request = new EmailNotificationRequest();
        Sender sender = new Sender(APP_NAME, APP_EMAIL);
        Recipient recipient = new Recipient(customer.getFirstname(), customer.getBioData().getEmail());
        request.setEmailSender(sender);
        request.setRecipients(Set.of(recipient));
        request.setSubject(ACTIVATION_LINK_VALUE);
        String template = getEmailTemplate();
        request.setContent(String.format(template, ACTIVATE_ACCOUNT_URL+"?"+token));
        return request;
    }

    private String getEmailTemplate() throws RegCrowException {
        try(BufferedReader reader =
                    new BufferedReader(new FileReader(MAIL_TEMPLATE_LOCATION))){
             return  reader.lines().collect(Collectors.joining());
        }catch (IOException exception){
            throw new RegCrowException("Failed to send activation link");
        }
    }

    @Override
    public CustomerResponse getCustomerById(Long id) throws UserNotFoundException {
        Optional<Customer> foundCustomer =  customerRepository.findById(id);
        Customer customer = foundCustomer.orElseThrow(()->new UserNotFoundException(
                String.format(USER_WITH_ID_NOT_FOUND, id)
        ));
        CustomerResponse customerResponse = buildCustomerResponse(customer);
        return customerResponse;
    }

    @Override
    public ApiResponse<?> verifyCustomer(String token) throws RegCrowException {
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512("secret".getBytes()))
                .build().verify(token);
        if (decodedJWT==null) throw new RegCrowException("):");
        return ApiResponse.builder().message("Account Verified").build();
    }

    @Override
    public List<CustomerResponse> getAllCustomers(int page, int items) {
        Pageable pageable = buildPageRequest(page, items);
        Page<Customer> customerPage = customerRepository.findAll(pageable);
        List<Customer> customers=customerPage.getContent();
        return customers.stream()
                        .map(RegcrowCustomerService::buildCustomerResponse)
                        .toList();
    }

    @Override
    public ApiResponse<?> deleteCustomer(Long id) {
        customerRepository.deleteById(id);
        return ApiResponse.builder()
                          .message(USER_DELETED_SUCCESSFULLY)
                          .build();
    }

    private static CustomerResponse buildCustomerResponse(Customer customer) {
        return CustomerResponse.builder()
                               .id(customer.getId())
                               .email(customer.getBioData().getEmail())
                               .name(customer.getFirstname()+" "+customer.getLastname())
                               .profileImage(customer.getProfileImage())
                               .build();
    }

    @Override
    public void deleteAll() {
        customerRepository.deleteAll();
    }


    @Override
    public ApiResponse<?> updateCustomerDetails(Long id, UpdateCustomerRequest updateCustomerRequest) throws UserNotFoundException, ProfileUpdateFailedException, JsonPointerException, IllegalAccessException {
        Optional<Customer> foundCustomer = customerRepository.findById(id);
        MultipartFile image = updateCustomerRequest.getProfileImage();
        JsonPatch jsonPatch = buildUpdatePatch(updateCustomerRequest);
        Customer customer = foundCustomer.orElseThrow(()->
                new UserNotFoundException(String.format(USER_WITH_ID_NOT_FOUND, id)));
        Customer updatedCustomer =  updateCustomer(customer,jsonPatch, image);
        customerRepository.save(updatedCustomer);
        return ApiResponse.builder()
                .message(PROFILE_UPDATED_SUCCESSFULLY)
                .build();
    }

    private JsonPatch buildUpdatePatch(UpdateCustomerRequest updateCustomerRequest) throws IllegalAccessException, JsonPointerException {
        List<JsonPatchOperation> operations =new ArrayList<>();
        List<String> updateFields =
                List.of("bankName", "accountName", "accountNumber", "email", "password", "profileImage");
        Field[] fields = updateCustomerRequest.getClass().getDeclaredFields();
        for (Field field:fields) {
            field.setAccessible(true);

            if (field.get(updateCustomerRequest)!=null&&
                    !updateFields.contains(field.getName())){
                var operation = new ReplaceOperation(
                        new JsonPointer("/"+field.getName()),
                        new TextNode(field.get(updateCustomerRequest).toString())
                );
                operations.add(operation);
            }else if (field.get(updateCustomerRequest)!=null&&
                    updateFields.contains(field.getName())&&!field.getName().equals("profileImage")){
                if (field.getName().contains("bank")||field.getName().contains("account")){
                    var operation = new ReplaceOperation(
                            new JsonPointer("/bankAccount/"+field.getName()),
                            new TextNode(field.get(updateCustomerRequest).toString())
                    );
                    operations.add(operation);
                }else{
                    var operation = new ReplaceOperation(
                            new JsonPointer("/bioData/"+field.getName()),
                            new TextNode(field.get(updateCustomerRequest).toString())
                    );
                    operations.add(operation);
                }
            }
        }
        return new JsonPatch(operations);
    }

    private Customer updateCustomer(Customer customer, JsonPatch jsonPatch, MultipartFile image) throws ProfileUpdateFailedException {
        ObjectMapper mapper = new ObjectMapper();
        log.info("Patch {}", jsonPatch.toString());
        JsonNode customerNode = mapper.convertValue(customer, JsonNode.class);
        try {
            JsonNode updatedNode= jsonPatch.apply(customerNode);

            Customer updatedCustomer =  mapper.convertValue(updatedNode, Customer.class);
            boolean isProfileImagePresent = image != null;
            if (isProfileImagePresent) uploadImage(image, updatedCustomer);
            updatedCustomer.setLastModifiedDate(LocalDateTime.now());
            return updatedCustomer;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ProfileUpdateFailedException(PROFILE_UPDATE_FAILED);
        }
    }

    private void uploadImage(MultipartFile image, Customer updatedCustomer) throws ImageUploadFailedException, IOException {
        String imageUrl = cloudService.upload(image.getBytes());
        updatedCustomer.setProfileImage(imageUrl);
    }

    private static CustomerRegistrationResponse buildRegisterCustomerResponse(Long customerId) {
        CustomerRegistrationResponse customerRegistrationResponse = new CustomerRegistrationResponse();
        customerRegistrationResponse.setMessage(USER_REGISTRATION_SUCCESSFUL);
        customerRegistrationResponse.setId(customerId);

        return customerRegistrationResponse;
    }
}
