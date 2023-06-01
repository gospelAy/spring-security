package africa.semicolon.regcrow.repositories;

import africa.semicolon.regcrow.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
