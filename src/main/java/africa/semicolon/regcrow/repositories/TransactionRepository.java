package africa.semicolon.regcrow.repositories;

import africa.semicolon.regcrow.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
