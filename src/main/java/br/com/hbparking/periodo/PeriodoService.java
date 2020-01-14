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

import static br.com.hbparking.util.DateHelper.validateDateIsAfter;
import static br.com.hbparking.util.DateHelper.validateDateIsEqual;
import static br.com.hbparking.util.ValidationUtils.validateNotNull;

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

    private void validarPeriodo(PeriodoDTO periodoDTO) throws InvalidPeriodDatesException {
        LOGGER.info("Validando periodo");

        validateNotNull(periodoDTO, "Periodo DTO não pode ser nulo");
        validateNotNull(periodoDTO.getDataFinal(), "Data Final não pode ser nula");
        validateNotNull(periodoDTO.getDataInicial(), "Data inicial não pode ser nula");
        validateNotNull(periodoDTO.getVehicleType(), "Tipo do veículo não pode ser nulo");
        validateDateIsAfter(periodoDTO.getDataInicial(), periodoDTO.getDataFinal());
        validateDateIsEqual(periodoDTO.getDataInicial(), periodoDTO.getDataFinal());

        this.validateIfPeriodoIsBetween(periodoDTO);
    }

    public void validateIfPeriodoIsBetween(PeriodoDTO periodoDTO) throws InvalidPeriodDatesException {
        for (Periodo periodo : this.iPeriodoRepository.findAll()) {
            if (periodo.getTipoVeiculo().equals(periodoDTO.getVehicleType())) {
                if (periodoDTO.getDataInicial().isAfter(periodo.getDataInicial()) && periodoDTO.getDataFinal().isBefore(periodo.getDataFinal())) {
                    throw new InvalidPeriodDatesException("Período não pode iniciar e finalizar dentro de outro período");
                }
                if (periodoDTO.getDataInicial().isBefore(periodo.getDataFinal()) && periodoDTO.getDataFinal().isBefore(periodo.getDataFinal())) {
                    throw new InvalidPeriodDatesException("Período não pode começar dentro de outro período que ainda está vigente");
                }
                if (periodoDTO.getDataInicial().isBefore(periodo.getDataInicial()) && periodoDTO.getDataFinal().isAfter(periodo.getDataFinal())) {
                    throw new InvalidPeriodDatesException("Período não pode iniciar antes de um período já existente e finalizar depois dele");
                }
                if (periodoDTO.getDataInicial().isEqual(periodo.getDataInicial()) || periodoDTO.getDataFinal().isEqual(periodo.getDataFinal()) || periodoDTO.getDataInicial().isEqual(periodo.getDataFinal()) || periodoDTO.getDataFinal().isEqual(periodo.getDataInicial())) {
                    throw new InvalidPeriodDatesException("Existem correspondências iguais de datas informadas");
                }
            }
        }
    }
}
