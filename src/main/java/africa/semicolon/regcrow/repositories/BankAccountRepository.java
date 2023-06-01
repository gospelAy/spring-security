package africa.semicolon.regcrow.repositories;

import africa.semicolon.regcrow.models.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
}
