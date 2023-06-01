package africa.semicolon.regcrow.services;

import africa.semicolon.regcrow.dtos.request.CustomerRegistrationRequest;
import africa.semicolon.regcrow.dtos.response.ApiResponse;
import africa.semicolon.regcrow.dtos.response.CustomerRegistrationResponse;
import africa.semicolon.regcrow.dtos.response.CustomerResponse;
import africa.semicolon.regcrow.exceptions.CustomerRegistrationFailedException;
import africa.semicolon.regcrow.exceptions.ProfileUpdateFailedException;
import africa.semicolon.regcrow.exceptions.UserNotFoundException;
import africa.semicolon.regcrow.models.BioData;
import africa.semicolon.regcrow.models.Customer;
import africa.semicolon.regcrow.repositories.CustomerRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static africa.semicolon.regcrow.utils.AppUtils.buildPageRequest;
import static africa.semicolon.regcrow.utils.ExceptionUtils.USER_REGISTRATION_FAILED;
import static africa.semicolon.regcrow.utils.ExceptionUtils.USER_WITH_ID_NOT_FOUND;
import static africa.semicolon.regcrow.utils.ResponseUtils.*;


@Service
@AllArgsConstructor
@Slf4j
public class RegcrowCustomerService implements CustomerService{
    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;
    @Override
    public CustomerRegistrationResponse register(CustomerRegistrationRequest customerRegistrationRequest) throws CustomerRegistrationFailedException {
        BioData bioData = modelMapper.map(customerRegistrationRequest, BioData.class);
        Customer customer=new Customer();
        customer.setBioData(bioData);
        Customer savedCustomer=customerRepository.save(customer);

        boolean isSavedCustomer = savedCustomer.getId() != null;
        if (!isSavedCustomer) throw new CustomerRegistrationFailedException(String.format(USER_REGISTRATION_FAILED, customerRegistrationRequest.getEmail()));
        return buildRegisterCustomerResponse(savedCustomer.getId());
    }

    @Override
    public CustomerResponse getCustomerById(Long id) throws UserNotFoundException {
        Optional<Customer> foundCustomer =  customerRepository.findById(id);
        Customer customer = foundCustomer.orElseThrow(()->new UserNotFoundException(
                String.format(USER_WITH_ID_NOT_FOUND, id)
        ));
        CustomerResponse customerResponse = buildCustomerResponse(customer);
        log.info("registration response {}", customerResponse);
        return customerResponse;
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
                               .build();
    }

    @Override
    public void deleteAll() {
        customerRepository.deleteAll();
    }

    @Override
    public ApiResponse<?> updateCustomerDetails(Long id, JsonPatch jsonPatch) throws UserNotFoundException, ProfileUpdateFailedException {
        ObjectMapper mapper = new ObjectMapper();
        Optional<Customer> foundCustomer = customerRepository.findById(id);
        Customer customer = foundCustomer.orElseThrow(()->
                new UserNotFoundException(String.format(USER_WITH_ID_NOT_FOUND, id)));

        JsonNode customerNode = mapper.convertValue(customer, JsonNode.class);
        try {
            JsonNode updatedNode=jsonPatch.apply(customerNode);
            Customer updatedCustomer =  mapper.convertValue(updatedNode, Customer.class);
            updatedCustomer.setLastModifiedDate(LocalDateTime.now());
            customerRepository.save(updatedCustomer);
            return ApiResponse.builder()
                                .message(PROFILE_UPDATED_SUCCESSFULLY)
                                .build();
        } catch (JsonPatchException e) {
            throw new ProfileUpdateFailedException(e.getMessage());
        }
    }

    private static CustomerRegistrationResponse buildRegisterCustomerResponse(Long customerId) {
        CustomerRegistrationResponse customerRegistrationResponse = new CustomerRegistrationResponse();
        customerRegistrationResponse.setMessage(USER_REGISTRATION_SUCCESSFUL);
        customerRegistrationResponse.setId(customerId);

        return customerRegistrationResponse;
    }
}
