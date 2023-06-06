package africa.semicolon.regcrow.dtos.request;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateCustomerRequest {
    private String firstname;
    private String lastname;
    private String bankName;
    private String accountName;
    private String accountNumber;
    private MultipartFile profileImage;
}
