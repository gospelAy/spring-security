package africa.semicolon.regcrow.repositories;

import africa.semicolon.regcrow.models.BioData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BioDataRepository extends JpaRepository<BioData, Long> {
}
