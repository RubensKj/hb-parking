package br.com.hbparking.vagadegaragem;

import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;

@Service
public class VagaGaragemHistoryService {

    private final VagaGaragemHistoryRepository vagaGaragemHistoryRepository;

    public VagaGaragemHistoryService(VagaGaragemHistoryRepository vagaGaragemHistoryRepository) {
        this.vagaGaragemHistoryRepository = vagaGaragemHistoryRepository;
    }

    public void saveData(VagaGaragem vagaGaragem) {
        this.vagaGaragemHistoryRepository.save(new VagaGaragemHistory(vagaGaragem, 0, LocalDateTime.now(Clock.systemUTC()), "CRIACAO"));
    }

    public void saveUpdateAction(VagaGaragem vagaGaragem, String message) {
        this.vagaGaragemHistoryRepository.save(new VagaGaragemHistory(vagaGaragem, 0, LocalDateTime.now(Clock.systemUTC()), message));
    }
}
