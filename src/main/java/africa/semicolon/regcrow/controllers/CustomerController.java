package africa.semicolon.regcrow.controllers;


import africa.semicolon.regcrow.dtos.request.CustomerRegistrationRequest;
import africa.semicolon.regcrow.dtos.request.UpdateCustomerRequest;
import africa.semicolon.regcrow.dtos.response.ApiResponse;
import africa.semicolon.regcrow.dtos.response.CustomerRegistrationResponse;
import africa.semicolon.regcrow.dtos.response.CustomerResponse;
import africa.semicolon.regcrow.exceptions.RegCrowException;
import africa.semicolon.regcrow.repositories.CustomerRepository;
import africa.semicolon.regcrow.services.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customer")
@AllArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<CustomerRegistrationResponse> register(@RequestBody CustomerRegistrationRequest customerRegistrationRequest){
        try{
            var response = customerService.register(customerRegistrationRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }catch (RegCrowException exception){
            var response = new CustomerRegistrationResponse();
            response.setMessage(exception.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PatchMapping
    public ResponseEntity<?> updateCustomerAccount(@RequestParam Long id, @ModelAttribute UpdateCustomerRequest updateCustomerRequest){
        try{
            var response = customerService.updateCustomerDetails(id, updateCustomerRequest);
            return ResponseEntity.ok(response);
        }catch (Exception exception){
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }
}
