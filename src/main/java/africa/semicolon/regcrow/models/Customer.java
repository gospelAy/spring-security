package africa.semicolon.regcrow.models;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
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
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private BioData bioData;
    private String firstname;
    private String lastname;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private BankAccount bankAccount;
    private String profileImage;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime timeCreated;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime lastModifiedDate;

    @PrePersist
    public void setTimeCreated(){
        this.timeCreated = LocalDateTime.now();
    }


    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", bioData=" + "{" +"email"+bioData.getEmail()
                +"password"+bioData.getPassword()+
                "}" +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", bankAccount=" + bankAccount +
                ", profileImage='" + profileImage + '\'' +
                ", timeCreated=" + timeCreated +
                ", lastModifiedDate=" + lastModifiedDate +
                '}';
    }
}
