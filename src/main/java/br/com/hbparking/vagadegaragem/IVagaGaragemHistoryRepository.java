package br.com.hbparking.vagadegaragem;

import br.com.hbparking.periodo.Periodo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IVagaGaragemHistoryRepository extends MongoRepository<VagaGaragemHistory, String> {
    List<VagaGaragemHistory> findByVagaGaragem_Periodo(Periodo periodo);
}
