package br.com.hbparking.periodo;

import br.com.hbparking.marcas.TipoVeiculoEnum;
import br.com.hbparking.tipoveiculo.VehicleType;
import org.apache.commons.lang3.EnumUtils;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PeriodoServiceTest {

    @Mock
    private IPeriodoRepository iPeriodoRepository;

    private PeriodoService periodoService;

    @Captor
    private ArgumentCaptor<Periodo> argumentCaptor;

    @Before
    public void setUp() throws Exception {
        periodoService = new PeriodoService(iPeriodoRepository);
    }

    @Test
    public void createPeriodo_ShouldReturnPeriodo() {

        PeriodoDTO periodoDTO = new PeriodoDTO(VehicleType.AUTOMOVEL, LocalDate.parse("2019-12-12"), LocalDate.parse("2019-12-15"));

        when(iPeriodoRepository.save(any(Periodo.class))).thenReturn(new Periodo(VehicleType.MOTO, LocalDate.parse("2019-12-12"), LocalDate.parse("2019-12-15")));

        PeriodoDTO periodoCreated = this.periodoService.create(periodoDTO);
        ;

        assertThat(periodoCreated).isNotNull();
        assertThat(periodoCreated.getVehicleType().name()).isEqualTo(periodoDTO.getVehicleType().name());
        assertThat(periodoCreated.getDataFinal()).isEqualTo(periodoDTO.getDataFinal());
        assertThat(periodoCreated.getDataInicial()).isEqualTo(periodoDTO.getDataInicial());
    }
}