package africa.semicolon.regcrow.services;


import africa.semicolon.regcrow.dtos.request.CustomerRegistrationRequest;
import africa.semicolon.regcrow.dtos.response.CustomerRegistrationResponse;
import africa.semicolon.regcrow.exceptions.CustomerRegistrationFailedException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class RegcrowCustomerServiceTest {
    @Autowired
    private CustomerService customerService;

    @Test
    public void testThatCustomerCanRegister() throws CustomerRegistrationFailedException {
        CustomerRegistrationRequest customerRegistrationRequest = new CustomerRegistrationRequest();
        customerRegistrationRequest.setEmail("9kicks@email.com");
        customerRegistrationRequest.setPassword("");

        CustomerRegistrationResponse registrationResponse =
                customerService.register(customerRegistrationRequest);

        assertThat(registrationResponse).isNotNull();
    }

}
