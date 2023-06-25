package africa.semicolon.regcrow.dtos.request;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static africa.semicolon.regcrow.utils.AppUtils.FIELD_CANNOT_BE_EMPTY_VALUE;


@Getter
@Setter
public class CustomerRegistrationRequest {

    @NotNull(message = FIELD_CANNOT_BE_EMPTY_VALUE)
    @NotEmpty(message = FIELD_CANNOT_BE_EMPTY_VALUE)
    @Email
    @JsonProperty("email")
    private String email;
    @NotNull
    @NotEmpty
    @JsonProperty("password")
    private String password;
}
