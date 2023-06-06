package africa.semicolon.regcrow.services;


import africa.semicolon.regcrow.dtos.request.CustomerRegistrationRequest;
import africa.semicolon.regcrow.dtos.response.CustomerRegistrationResponse;
import africa.semicolon.regcrow.dtos.response.CustomerResponse;
import africa.semicolon.regcrow.exceptions.CustomerRegistrationFailedException;
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

//
//    @Test
//    public void updateCustomerTest() throws UserNotFoundException, ProfileUpdateFailedException, IOException {
//        JsonPatch updateForm = buildUpdatePatch();
//        CustomerResponse foundCustomer = customerService.getCustomerById(customerRegistrationResponse.getId());
//        assertThat(foundCustomer.getName().contains("Folahan")
//                &&foundCustomer.getName().contains("Doe")).isFalse();
//
//        var response =
//                customerService.updateCustomerDetails(customerRegistrationResponse.getId(),
//                        updateForm,
//                        new MockMultipartFile("2 goats",
//                                new FileInputStream("C:\\Users\\semicolon\\Documents\\java_workspace\\regcrow\\src\\test\\resources\\assets\\goat.jpg")
//                        ));
//
//        assertThat(response).isNotNull();
//        foundCustomer = customerService.getCustomerById(customerRegistrationResponse.getId());
//        assertThat(foundCustomer.getProfileImage()).isNotNull();
//
//        CustomerResponse customerResponse = customerService.getCustomerById(customerRegistrationResponse.getId());
//        assertThat(customerResponse.getName().contains("Folahan")
//                &&customerResponse.getName().contains("Joshua")).isTrue();
//    }

    private JsonPatch buildUpdatePatch() {
        try {
            List<JsonPatchOperation> updates = List.of(
                    new ReplaceOperation(
                            new JsonPointer("/firstname"),
                            new TextNode("Folahan")
                    ),
                    new ReplaceOperation(
                            new JsonPointer("/lastname"),
                            new TextNode("Joshua")
                    ),
                    new ReplaceOperation(
                            new JsonPointer("/bankAccount/accountName"),
                            new TextNode("Folahan Joshua")
                    ),
                    new ReplaceOperation(
                            new JsonPointer("/bankAccount/accountNumber"),
                            new TextNode("0123456789")
                    ),
                    new ReplaceOperation(
                            new JsonPointer("/bankAccount/bankName"),
                            new TextNode("Prof-Pay")
                    )
            );
            return new JsonPatch(updates);
        } catch (JsonPointerException e) {
            throw new RuntimeException(e);
        }
    }


}
