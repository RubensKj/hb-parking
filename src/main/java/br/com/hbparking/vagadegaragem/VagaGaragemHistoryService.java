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

    public void saveData(VagaGaragem vagaGaragem, Integer prioridade) {
        this.vagaGaragemHistoryRepository.save(new VagaGaragemHistory(vagaGaragem, prioridade, LocalDateTime.now(Clock.systemUTC()), "CRIACAO"));
    }

    public void saveUpdateAction(VagaGaragem vagaGaragem, String message, Integer prioridade) {
        this.vagaGaragemHistoryRepository.save(new VagaGaragemHistory(vagaGaragem, prioridade, LocalDateTime.now(Clock.systemUTC()), message));
    }
}
