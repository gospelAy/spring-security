package africa.semicolon.regcrow.controllers;

import africa.semicolon.regcrow.dtos.request.CustomerRegistrationRequest;
import africa.semicolon.regcrow.dtos.request.UpdateCustomerRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.Part;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.RequestEntity.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


@SpringBootTest
@AutoConfigureMockMvc
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Test
    public void registerTest() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        CustomerRegistrationRequest registrationRequest = new CustomerRegistrationRequest();
        registrationRequest.setEmail("test@email.com");
        registrationRequest.setPassword("tinu");
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/customer")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsBytes(registrationRequest)))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED.value()))
                .andDo(print());
    }

    @Test
    void getCustomerByIdTest() {
    }

    @Test
    void getAllCustomersTest() {
    }

    @Test
    void updateCustomerDetailsTest() throws Exception {
        UpdateCustomerRequest updateCustomerRequest = new UpdateCustomerRequest();
        updateCustomerRequest.setAccountName("new_guy");
        var image = new MockMultipartFile("test",new FileInputStream("C:\\Users\\semicolon\\Documents\\java_workspace\\regcrow\\src\\test\\resources\\assets\\goat.jpg"));
        ObjectMapper mapper = new ObjectMapper();
        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart(HttpMethod.PATCH, "/api/v1/customer")
                        .file(image)
                        .content(mapper.writeValueAsString(updateCustomerRequest))
                        .param("id", "1");
        mockMvc.perform(builder)
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andDo(print());
    }
}