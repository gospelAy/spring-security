package africa.semicolon.regcrow.services;

import africa.semicolon.regcrow.dtos.request.CustomerRegistrationRequest;
import africa.semicolon.regcrow.dtos.request.UpdateCustomerRequest;
import africa.semicolon.regcrow.dtos.response.ApiResponse;
import africa.semicolon.regcrow.dtos.response.CustomerRegistrationResponse;
import africa.semicolon.regcrow.dtos.response.CustomerResponse;
import africa.semicolon.regcrow.exceptions.CustomerRegistrationFailedException;
import africa.semicolon.regcrow.exceptions.ProfileUpdateFailedException;
import africa.semicolon.regcrow.exceptions.RegCrowException;
import africa.semicolon.regcrow.exceptions.UserNotFoundException;
import com.github.fge.jackson.jsonpointer.JsonPointerException;
import com.github.fge.jsonpatch.JsonPatch;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CustomerService {
    CustomerRegistrationResponse register(CustomerRegistrationRequest customerRegistrationRequest) throws RegCrowException;

    CustomerResponse getCustomerById(Long id) throws UserNotFoundException;

    ApiResponse<?> verifyCustomer(String token) throws RegCrowException;


    List<CustomerResponse> getAllCustomers(int page, int items);

    ApiResponse<?> deleteCustomer(Long id);

    void deleteAll();

    ApiResponse<?> updateCustomerDetails(Long id, UpdateCustomerRequest updateCustomerRequest) throws UserNotFoundException, ProfileUpdateFailedException, JsonPointerException, IllegalAccessException;
}
