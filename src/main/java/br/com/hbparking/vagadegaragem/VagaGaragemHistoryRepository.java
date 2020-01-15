package br.com.hbparking.vagadegaragem;

import br.com.hbparking.periodo.Periodo;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface VagaGaragemHistoryRepository extends MongoRepository<VagaGaragemHistory, String> {
}
