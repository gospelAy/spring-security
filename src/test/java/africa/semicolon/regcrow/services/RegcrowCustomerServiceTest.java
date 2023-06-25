package africa.semicolon.regcrow.services;


import africa.semicolon.regcrow.dtos.request.CustomerRegistrationRequest;
import africa.semicolon.regcrow.dtos.request.UpdateCustomerRequest;
import africa.semicolon.regcrow.dtos.response.CustomerRegistrationResponse;
import africa.semicolon.regcrow.dtos.response.CustomerResponse;
import africa.semicolon.regcrow.exceptions.CustomerRegistrationFailedException;
import africa.semicolon.regcrow.exceptions.ProfileUpdateFailedException;
import africa.semicolon.regcrow.exceptions.RegCrowException;
import africa.semicolon.regcrow.exceptions.UserNotFoundException;
import com.fasterxml.jackson.databind.node.TextNode;
import com.github.fge.jackson.jsonpointer.JsonPointer;
import com.github.fge.jackson.jsonpointer.JsonPointerException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchOperation;
import com.github.fge.jsonpatch.ReplaceOperation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import static java.math.BigInteger.*;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
public class RegcrowCustomerServiceTest {
    @Autowired
    private CustomerService customerService;

    private CustomerRegistrationRequest customerRegistrationRequest;
    private CustomerRegistrationResponse customerRegistrationResponse;

    @BeforeEach
    public void setUp() throws RegCrowException {
        customerService.deleteAll();
        customerRegistrationRequest = new CustomerRegistrationRequest();
        customerRegistrationRequest.setEmail("9kicks@email.com");
        customerRegistrationRequest.setPassword("");

        customerRegistrationResponse = customerService.register(customerRegistrationRequest);
    }
    @Test
    public void testThatCustomerCanRegister() throws CustomerRegistrationFailedException {
        assertThat(customerRegistrationResponse).isNotNull();
    }

    @Test
    public void getCustomerByIdTest() throws CustomerRegistrationFailedException, UserNotFoundException {
        var foundCustomer = customerService.getCustomerById(customerRegistrationResponse.getId());
        assertThat(foundCustomer).isNotNull();
        assertThat(foundCustomer.getEmail()).isNotNull();
        assertThat(foundCustomer.getEmail()).isEqualTo(customerRegistrationRequest.getEmail());

    }


    @Test
    public void testVerify(){

    }

    @Test
    public void getAllCustomersTest() throws RegCrowException {
        customerRegistrationRequest.setEmail("Felix@gmail.com");
        customerRegistrationRequest.setPassword("12345");
        customerService.register(customerRegistrationRequest);
        List<CustomerResponse> customers = customerService.getAllCustomers(ONE.intValue(), TEN.intValue());
        assertThat(customers.size()).isEqualTo(TWO.intValue());
    }


    @Test
    public void deleteCustomerTest(){
        var customers = customerService.getAllCustomers(ONE.intValue(), TEN.intValue());
        int numberOfCustomers = customers.size();
        assertThat(numberOfCustomers).isGreaterThan(ZERO.intValue());
        customerService.deleteCustomer(customerRegistrationResponse.getId());
        List<CustomerResponse> currentCustomers = customerService.getAllCustomers(ONE.intValue(), TEN.intValue());
        assertThat(currentCustomers.size()).isEqualTo(numberOfCustomers-ONE.intValue());
    }


    @Test
    public void updateCustomerTest() throws Exception {
        CustomerResponse foundCustomer = customerService.getCustomerById(customerRegistrationResponse.getId());
        assertThat(foundCustomer.getName().contains("Moyin")
                &&foundCustomer.getName().contains("Zainab")).isFalse();

        UpdateCustomerRequest updateCustomerRequest = new UpdateCustomerRequest();
        var image = new MockMultipartFile("2 goats",
                new FileInputStream("C:\\Users\\semicolon\\Documents\\java_workspace\\regcrow\\src\\test\\resources\\assets\\goat.jpg")
        );
        updateCustomerRequest.setProfileImage(image);
        updateCustomerRequest.setFirstname("Moyin");
        updateCustomerRequest.setLastname("Zainab");
        updateCustomerRequest.setBankName("Zen Bank");
        updateCustomerRequest.setAccountName("Moyin Zainab");
        updateCustomerRequest.setAccountNumber("0123456789");
        var response =
                customerService.updateCustomerDetails(customerRegistrationResponse.getId(),
                        updateCustomerRequest
                        );

        assertThat(response).isNotNull();
        foundCustomer = customerService.getCustomerById(customerRegistrationResponse.getId());
        assertThat(foundCustomer.getProfileImage()).isNotNull();

        CustomerResponse customerResponse = customerService.getCustomerById(customerRegistrationResponse.getId());
        assertThat(customerResponse.getName().contains("Moyin")
                &&customerResponse.getName().contains("Zainab")).isTrue();
    }

}
