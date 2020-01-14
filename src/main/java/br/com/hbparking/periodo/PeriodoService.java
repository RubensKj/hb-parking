package br.com.hbparking.periodo;

import br.com.hbparking.tipoveiculo.VehicleType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PeriodoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PeriodoService.class);

    private final IPeriodoRepository iPeriodoRepository;

    public PeriodoService(IPeriodoRepository iPeriodoRepository) {
        this.iPeriodoRepository = iPeriodoRepository;
    }

    public PeriodoDTO create(PeriodoDTO periodoDTO) throws InvalidPeriodDatesException {
        this.validarPeriodo(periodoDTO);
        this.validateIfPeriodoIsBetween(periodoDTO);
        LOGGER.info("Criando periodo");
        return PeriodoDTO.of(this.iPeriodoRepository.save(new Periodo(periodoDTO.getVehicleType(), periodoDTO.getDataInicial(), periodoDTO.getDataFinal())));
    }

    public List<PeriodoDTO> findPeriodoByVehicleType(VehicleType vehicleType) {
        List<Periodo> periodoList = this.iPeriodoRepository.findAllByTipoVeiculo(vehicleType);
        return this.parsePeriodoListToPeriodoDTOList(periodoList);
    }

    public Periodo findById(Long id) {
        Optional<Periodo> periodo = this.iPeriodoRepository.findById(id);
        if (periodo.isPresent()) {
            return periodo.get();
        }
        throw new IllegalArgumentException(String.format("ID %s de periodo não existe", id));
    }

    public List<PeriodoDTO> findAllPeriodos() {
        return this.parsePeriodoListToPeriodoDTOList(this.iPeriodoRepository.findAll());
    }

    public List<PeriodoDTO> parsePeriodoListToDTO(List<Periodo> periodoList) {
        List<PeriodoDTO> periodoDTOList = new ArrayList<>();
        periodoList.forEach(periodo -> periodoDTOList.add(PeriodoDTO.of(periodo)));
        return periodoDTOList;
    }


    public List<PeriodoDTO> parsePeriodoListToPeriodoDTOList(List<Periodo> periodosList) {
        List<PeriodoDTO> periodoDTOList = new ArrayList<>();
        List<Periodo> periodosFiltrada = periodosList.stream().filter(periodo -> periodo.getDataFinal().isAfter(LocalDate.now())).collect(Collectors.toList());
        periodosFiltrada.forEach(periodo -> periodoDTOList.add(PeriodoDTO.of(periodo)));
        return periodoDTOList;
    }

    private void validarPeriodo(PeriodoDTO periodoDTO) {
        LOGGER.info("Validando periodo");

        if (periodoDTO.getDataInicial() == null) {
            throw new IllegalArgumentException("Data inicial não pode ser nula");
        }
        if (periodoDTO.getDataFinal() == null) {
            throw new IllegalArgumentException("Data final não pode ser nula");
        }
        if (periodoDTO.getVehicleType() == null) {
            throw new IllegalArgumentException("Tipo de veículo não pode ser nulo");
        }
        if (periodoDTO.getDataInicial().isAfter(periodoDTO.getDataFinal())) {
            throw new IllegalArgumentException("Data final deve ser após data inicial");
        }
        if (periodoDTO.getDataInicial().isEqual(periodoDTO.getDataFinal())) {
            throw new IllegalArgumentException("Um periodo deve ter data inicial e final distintas");
        }
    }

    public void validateIfPeriodoIsBetween(PeriodoDTO periodoDTO) throws InvalidPeriodDatesException {
        for (Periodo periodo : this.iPeriodoRepository.findAll()) {
            if (periodo.getTipoVeiculo().equals(periodoDTO.getVehicleType())) {
                if (!periodoDTO.getDataInicial().isBefore(periodo.getDataInicial()) && periodoDTO.getDataInicial().isBefore(periodo.getDataFinal())) {
                    throw new InvalidPeriodDatesException("Datas do período não podem sobrepor outros períodos vigentes");
                }
                if (!periodoDTO.getDataInicial().isAfter(periodo.getDataFinal()) && periodoDTO.getDataFinal().isAfter(periodo.getDataFinal())) {
                    throw new InvalidPeriodDatesException("Datas do período não podem sobrepor outros períodos vigentes");
                }
                if (periodoDTO.getDataInicial().isEqual(periodo.getDataInicial()) || periodoDTO.getDataFinal().isEqual(periodo.getDataFinal()) || periodoDTO.getDataInicial().isEqual(periodo.getDataFinal()) || periodoDTO.getDataFinal().isEqual(periodo.getDataInicial())) {
                    throw new InvalidPeriodDatesException("Datas do período não podem sobrepor outros periodos vigentes");
                }
            }
        }
    }
}
