package br.com.hbparking.vagadegaragem;

import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;

@Service
public class VagaGaragemHistoryService {

    private final IVagaGaragemHistoryRepository IVagaGaragemHistoryRepository;

    public VagaGaragemHistoryService(IVagaGaragemHistoryRepository IVagaGaragemHistoryRepository) {
        this.IVagaGaragemHistoryRepository = IVagaGaragemHistoryRepository;
    }

    public void saveData(VagaGaragem vagaGaragem, Integer prioridade) {
        this.IVagaGaragemHistoryRepository.save(new VagaGaragemHistory(vagaGaragem, prioridade, LocalDateTime.now(Clock.systemUTC()), "CRIACAO"));
    }

    public void saveUpdateAction(VagaGaragem vagaGaragem, String message, Integer prioridade) {
        this.IVagaGaragemHistoryRepository.save(new VagaGaragemHistory(vagaGaragem, prioridade, LocalDateTime.now(Clock.systemUTC()), message));
    }
}
