package br.com.hbparking.periodo;

import br.com.hbparking.tipoveiculo.VehicleType;
import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.Silent.class)
public class PeriodoServiceTest {

    @Mock
    private IPeriodoRepository iPeriodoRepository;

    private PeriodoService periodoService;

    @Before
    public void setUp() {
        periodoService = new PeriodoService(iPeriodoRepository);
    }

    @Test
    public void createPeriodo_ShouldReturnPeriodo() throws InvalidPeriodDatesException {
        PeriodoDTO periodoDTO = new PeriodoDTO(VehicleType.CARRO, LocalDate.parse("2019-12-12"), LocalDate.parse("2019-12-15"));

        when(iPeriodoRepository.save(any(Periodo.class))).thenReturn(new Periodo(VehicleType.CARRO, LocalDate.parse("2019-12-12"), LocalDate.parse("2019-12-15")));

        PeriodoDTO periodoCreated = this.periodoService.create(periodoDTO);

        assertThat(periodoCreated).isNotNull();
        assertThat(periodoCreated.getVehicleType().name()).isEqualTo(periodoDTO.getVehicleType().name());
        assertThat(periodoCreated.getDataFinal()).isEqualTo(periodoDTO.getDataFinal());
        assertThat(periodoCreated.getDataInicial()).isEqualTo(periodoDTO.getDataInicial());
    }

    @Test
    public void getAllPeriodosByVehicleType_ShouldReturnPeriodoList() {
        List<Periodo> periodoList = new ArrayList<>();
        Periodo periodoUm = new Periodo(VehicleType.CARRO, LocalDate.parse("2019-12-12"), LocalDate.parse("2019-12-15"));
        Periodo periodoDois = new Periodo(VehicleType.BICICLETA, LocalDate.parse("2022-10-05"), LocalDate.parse("2022-11-06"));
        Periodo periodoTres = new Periodo(VehicleType.CARRO, LocalDate.parse("2018-06-17"), LocalDate.parse("2018-09-23"));

        periodoList.add(periodoUm);
        periodoList.add(periodoDois);
        periodoList.add(periodoTres);

        given(iPeriodoRepository.findAllByTipoVeiculo(VehicleType.CARRO)).willReturn(periodoList.stream().filter(periodo -> periodo.getTipoVeiculo().equals(VehicleType.CARRO)).collect(Collectors.toList()));
        List<Periodo> periodoFilteredWithVehicleType = periodoList.stream().filter(periodo -> periodo.getTipoVeiculo().equals(VehicleType.CARRO)).collect(Collectors.toList());

        assertThat(periodoFilteredWithVehicleType).isNotEmpty();
        assertThat(periodoFilteredWithVehicleType).isNotNull();
        assertEquals(2, periodoFilteredWithVehicleType.size());
    }

    @Test
    public void validateIfPeriodoIsBetween() {
        PeriodoDTO periodoDTO = new PeriodoDTO(VehicleType.CARRO, LocalDate.of(2019, 10, 7), LocalDate.of(2019, 10, 10));

        List<Periodo> periodoList = new ArrayList<>();
        periodoList.add(new Periodo(VehicleType.CARRO, LocalDate.of(2019, 10, 7), LocalDate.of(2019, 10, 10)));
        periodoList.add(new Periodo(VehicleType.CARRO, LocalDate.of(2019, 10, 11), LocalDate.of(2019, 10, 20)));
        periodoList.add(new Periodo(VehicleType.CARRO, LocalDate.of(2019, 10, 21), LocalDate.of(2019, 10, 23)));

        given(iPeriodoRepository.findAll()).willReturn(periodoList);

        assertThrows(InvalidPeriodDatesException.class, () -> periodoService.validateIfPeriodoIsBetween(periodoDTO));
    }
}