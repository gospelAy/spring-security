package africa.semicolon.regcrow.repositories;

import africa.semicolon.regcrow.models.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {
}
