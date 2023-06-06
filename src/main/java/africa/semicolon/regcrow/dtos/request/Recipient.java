package africa.semicolon.regcrow.dtos.request;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class Recipient {
    private final String name;
    private final String email;
}
