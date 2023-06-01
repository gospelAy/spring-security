package africa.semicolon.regcrow.models;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(cascade = CascadeType.ALL)
    private BioData bioData;
    private String firstname;
    private String lastname;
    @OneToOne
    private BankAccount bankAccount;
    private String profileImage;
    private LocalDateTime timeCreated;

    @PrePersist
    public void setTimeCreated(){
        this.timeCreated = LocalDateTime.now();
    }
}
