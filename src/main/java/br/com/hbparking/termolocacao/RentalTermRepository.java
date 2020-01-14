package br.com.hbparking.termolocacao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RentalTermRepository extends JpaRepository<RentalTerm, Long> {
    Optional<RentalTerm> findRentalTermByRentalTermStatusIs(RentalTermStatus status);

    boolean existsByFileName(String fileName);
}
