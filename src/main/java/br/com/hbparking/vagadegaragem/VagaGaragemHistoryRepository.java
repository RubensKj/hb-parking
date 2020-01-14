package br.com.hbparking.vagadegaragem;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface VagaGaragemHistoryRepository extends MongoRepository<VagaGaragemHistory, String> {
}
