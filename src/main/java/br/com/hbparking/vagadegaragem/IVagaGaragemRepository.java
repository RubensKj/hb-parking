package br.com.hbparking.vagadegaragem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IVagaGaragemRepository extends JpaRepository<VagaGaragem, Long> {
}
