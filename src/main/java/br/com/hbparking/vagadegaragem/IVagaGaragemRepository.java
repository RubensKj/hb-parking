package br.com.hbparking.vagadegaragem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

@Repository
public interface IVagaGaragemRepository extends JpaRepository<VagaGaragem, Long> {

    Stream<VagaGaragem> streamAll();
}
