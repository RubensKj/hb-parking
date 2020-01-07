package br.com.hbparking.vagaInfo;

import br.com.hbparking.periodo.IPeriodoRepository;
import br.com.hbparking.periodo.Periodo;
import br.com.hbparking.periodo.PeriodoService;
import br.com.hbparking.tipoveiculo.VehicleType;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class VagaInfoServiceTest {

    @Mock
    private IVagaInfoRepository iVagaInfoRepository;

    @InjectMocks
    private VagaInfoService vagaInfoService;

    @Mock
    private IPeriodoRepository iPeriodoRepository;

    @Mock
    private PeriodoService periodoService;

    @Before
    public void setUp() throws Exception {
        periodoService = new PeriodoService(iPeriodoRepository);
        vagaInfoService = new VagaInfoService(iVagaInfoRepository, periodoService);
    }

    @Captor
    private ArgumentCaptor<VagaInfo> argumentCaptor;

    @Test
    public void cadastrar() throws PeriodoAlreadyExistsException {
        long id = 1;
        VagaInfoDTO vagaInfoDTO = new VagaInfoDTO(300, id, VehicleType.PATINETE_E_BICICLETA, 150.0);

        when(iPeriodoRepository.findById(any())).thenReturn(java.util.Optional.of(new Periodo(VehicleType.CARRO, LocalDate.of(2019, 10, 7), LocalDate.of(2019, 10, 10))));

        Periodo periodo = new Periodo(VehicleType.CARRO, LocalDate.of(2019, 10, 7), LocalDate.of(2019, 10, 10));

        given(iVagaInfoRepository.save(any(VagaInfo.class))).willReturn(new VagaInfo((long) 1, 300, 150.0, VehicleType.PATINETE_E_BICICLETA, periodo));

        this.vagaInfoService.cadastrar(vagaInfoDTO);

        verify(this.iVagaInfoRepository, times(1)).save(this.argumentCaptor.capture());

        VagaInfo savedVagaInfo = argumentCaptor.getValue();

        assertThat(savedVagaInfo.getValor()).isPositive();
        assertThat(savedVagaInfo.getQuantidade()).isPositive();
        assertThat(savedVagaInfo.getVehicleType()).isNotNull();
    }
}