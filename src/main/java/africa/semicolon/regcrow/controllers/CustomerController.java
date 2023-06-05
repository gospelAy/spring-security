package africa.semicolon.regcrow.controllers;


import africa.semicolon.regcrow.dtos.request.CustomerRegistrationRequest;
import africa.semicolon.regcrow.exceptions.CustomerRegistrationFailedException;
import africa.semicolon.regcrow.services.CustomerService;
import com.github.fge.jsonpatch.JsonPatch;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static africa.semicolon.regcrow.utils.AppUtils.JSON_PATCH_CONSTANT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/v1/customer")
@AllArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<?> register(@RequestBody CustomerRegistrationRequest request){
        try {
            var response = customerService.register(request);
            return ResponseEntity.ok(response);
        } catch (CustomerRegistrationFailedException e) {
            return ResponseEntity.badRequest().body(e);
        }

    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getCustomerById(@PathVariable Long id){
        try {
            var response = customerService.getCustomerById(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e);
        }

    }

    @GetMapping("/all/{page}/{items}")
    public ResponseEntity<?> getAllCustomers(@PathVariable int page, @PathVariable int items){
        try {
            var response = customerService.getAllCustomers(page, items);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e);
        }

    }

    @PatchMapping(value = "/{id}", consumes = {JSON_PATCH_CONSTANT})
    public ResponseEntity<?> updateCustomerDetails(@PathVariable Long id, @RequestBody JsonPatch updatePatch){
        try {
            var response = customerService.updateCustomerDetails(id, updatePatch, null);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e);
        }

    }
}
