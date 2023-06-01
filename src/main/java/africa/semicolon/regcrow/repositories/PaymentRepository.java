package africa.semicolon.regcrow.repositories;

import africa.semicolon.regcrow.models.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
