package br.com.hbparking.vagadegaragem;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IVagaGaragemHistoryRepository extends MongoRepository<VagaGaragemHistory, String> {
}
