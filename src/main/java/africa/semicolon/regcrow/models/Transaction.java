package africa.semicolon.regcrow.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToOne
    private Payment payment;
    private Long sellerId;
    private Long buyerId;
    private String description;
    private LocalDateTime createdAt;
    private Status status;

}
