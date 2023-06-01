package africa.semicolon.regcrow.services;

import africa.semicolon.regcrow.dtos.request.CustomerRegistrationRequest;
import africa.semicolon.regcrow.dtos.response.CustomerRegistrationResponse;
import africa.semicolon.regcrow.exceptions.CustomerRegistrationFailedException;
import africa.semicolon.regcrow.models.BioData;
import africa.semicolon.regcrow.models.Customer;
import africa.semicolon.regcrow.repositories.BioDataRepository;
import africa.semicolon.regcrow.repositories.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static africa.semicolon.regcrow.utils.ExceptionUtils.USER_REGISTRATION_FAILED;
import static africa.semicolon.regcrow.utils.ResponseUtils.USER_REGISTRATION_SUCCESSFUL;


@Service
@AllArgsConstructor
public class RegcrowCustomerService implements CustomerService{
    private final CustomerRepository customerRepository;

    private final BioDataRepository bioDataRepository;
    @Override
    public CustomerRegistrationResponse register(CustomerRegistrationRequest customerRegistrationRequest) throws CustomerRegistrationFailedException {


        Customer customer = new Customer();
        customer.setBioData(bioData);
        Customer savedCustomer=customerRepository.save(customer);

        boolean isSavedCustomer = savedCustomer.getId() != null;
        if (!isSavedCustomer)
            throw new CustomerRegistrationFailedException(
                    String.format(USER_REGISTRATION_FAILED, customerEmail));

        CustomerRegistrationResponse customerRegistrationResponse = new CustomerRegistrationResponse();
        customerRegistrationResponse.setMessage(USER_REGISTRATION_SUCCESSFUL);

        return customerRegistrationResponse;
    }
}
