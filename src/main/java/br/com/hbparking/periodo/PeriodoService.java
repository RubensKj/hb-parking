package br.com.hbparking.periodo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PeriodoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PeriodoService.class);

    private final IPeriodoRepository iPeriodoRepository;

    public PeriodoService(IPeriodoRepository iPeriodoRepository) {
        this.iPeriodoRepository = iPeriodoRepository;
    }

    public PeriodoDTO create(PeriodoDTO periodoDTO) {
        this.validarPeriodo(periodoDTO);
        LOGGER.info("Criando periodo");
        return PeriodoDTO.of(this.iPeriodoRepository.save(new Periodo(periodoDTO.getVehicleType(), periodoDTO.getDataInicial(), periodoDTO.getDataFinal())));
    }

    public void validarPeriodo(PeriodoDTO periodoDTO) {
        LOGGER.info("Validando periodo");
        if (periodoDTO.getDataInicial().isAfter(periodoDTO.getDataFinal())) {
            throw new IllegalArgumentException("Data final deve ser após data inicial");
        }
        if (periodoDTO.getDataInicial().isEqual(periodoDTO.getDataFinal())) {
            throw new IllegalArgumentException("Um periodo deve ter data inicial e final distintas");
        }
        if (periodoDTO.getDataInicial() == null) {
            throw new IllegalArgumentException("Data inicial não pode ser nula");
        }
        if (periodoDTO.getDataFinal() == null) {
            throw new IllegalArgumentException("Data final não pode ser nula");
        }
        if (periodoDTO.getVehicleType() == null) {
            throw new IllegalArgumentException("Tipo de veículo não pode ser nulo");
        }
    }
}
