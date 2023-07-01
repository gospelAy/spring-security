package africa.semicolon.regcrow.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class BioData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true)
    private String email;
    private String password;
    @JsonIgnore
    private Boolean isEnabled;
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Role> roles;

}
