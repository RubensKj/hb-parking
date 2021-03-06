package br.com.hbparking.vagastests;

import br.com.hbparking.colaborador.NoConnectionAPIException;
import br.com.hbparking.cor.Color;
import br.com.hbparking.marca.MarcaServiceTest;
import br.com.hbparking.periodo.Periodo;
import br.com.hbparking.tipoveiculo.VehicleType;
import br.com.hbparking.vagadegaragem.*;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = MarcaServiceTest.class)
public class VagaGaragemServiceTest {


    @InjectMocks
    private VagaGaragemService vagaGaragemService;

    @Mock
    private ValidadeOnHBEmployee validadeOnHBEmployee;

    @Before
    public void SetUp() {
        validadeOnHBEmployee = new ValidadeOnHBEmployee();
    }


    @Test
    public void vagaDTONull() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.vagaGaragemService.validate(null);
        });
    }

    @Test
    public void vagaCARROSemPlaca() {
        VagaGaragemDTO vagaGaragemDTO = new VagaGaragemDTO(VehicleType.CARRO,1L,1L, Color.BRANCO, null,1l , 2l, StatusVaga.EMAPROVACAO);

        assertThrows(NullPointerException.class, () -> {
            this.vagaGaragemService.validate(vagaGaragemDTO);
        });
    }

    @Test
    public void vagaCARROSemModelo() {
        VagaGaragemDTO vagaGaragemDTO = new VagaGaragemDTO(VehicleType.CARRO,1L,null, Color.BRANCO, "MCI5965",1l , 2l, StatusVaga.EMAPROVACAO);

        assertThrows(NullPointerException.class, () -> {
            this.vagaGaragemService.validate(vagaGaragemDTO);
        });
    }

    @Test
    public void vagaCARROSemMarca() {
        VagaGaragemDTO vagaGaragemDTO = new VagaGaragemDTO(VehicleType.CARRO,null,1L, Color.BRANCO, "MCI5965",1l , 2l, StatusVaga.EMAPROVACAO);

        assertThrows(NullPointerException.class, () -> {
            this.vagaGaragemService.validate(vagaGaragemDTO);
        });
    }

    @Test
    public void vagaMOTOSemModelo() {
        VagaGaragemDTO vagaGaragemDTO = new VagaGaragemDTO(VehicleType.MOTO,1L,null, Color.BRANCO, "abc21d3",1l , 2l, StatusVaga.EMAPROVACAO);

        assertThrows(NullPointerException.class, () -> {
            this.vagaGaragemService.validate(vagaGaragemDTO);
        });
    }

    @Test
    public void vagaMOTOSemMarca() {
        VagaGaragemDTO vagaGaragemDTO = new VagaGaragemDTO(VehicleType.MOTO,null,1l, Color.BRANCO, "abc21d3",1l , 2l, StatusVaga.EMAPROVACAO);

        assertThrows(NullPointerException.class, () -> {
            this.vagaGaragemService.validate(vagaGaragemDTO);
        });
    }

    @Test
    public void vagaMOTOSemPlaca() {
        VagaGaragemDTO vagaGaragemDTO = new VagaGaragemDTO(VehicleType.MOTO,1l,1l, Color.BRANCO, null,1l , 2l, StatusVaga.EMAPROVACAO);

        assertThrows(NullPointerException.class, () -> {
            this.vagaGaragemService.validate(vagaGaragemDTO);
        });
    }

    @Test
    public void vagaPatinete() {
        VagaGaragemDTO vagaGaragemDTO = new VagaGaragemDTO(VehicleType.PATINETE, null, null, null, null, 1l, 2l, StatusVaga.EMAPROVACAO);
            this.vagaGaragemService.validate(vagaGaragemDTO);

    }

    @Test
    public void vagaBicicleta() {
        VagaGaragemDTO vagaGaragemDTO = new VagaGaragemDTO(VehicleType.BICICLETA, null, null, null, null, 1l, 2l, StatusVaga.EMAPROVACAO);
            this.vagaGaragemService.validate(vagaGaragemDTO);
    }

    @Test
    public void vagaCarroPlacaInvalida() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.vagaGaragemService.placaValidator("999999");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            this.vagaGaragemService.placaValidator("aaaaaa");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            this.vagaGaragemService.placaValidator("a1");
        });
    }

    @Test
    public void vagaCarroPlacaValida() {
       this.vagaGaragemService.placaValidator("123ABCD"); // placa do Paraguai
        this.vagaGaragemService.placaValidator("ABC1234");// placa do Uruguai
        this.vagaGaragemService.placaValidator("ABC1D23");// placa do Brasil
        this.vagaGaragemService.placaValidator("AB123CD");// placa da Argentina
        this.vagaGaragemService.placaValidator("MCI5965");// placa Antiga
    }

    @org.junit.Test(expected = NoConnectionAPIException.class)
    public void noConnectioToApiThrowException() throws Exception {
        validadeOnHBEmployee.validate("teste");
    }
    @Test
    public void invalidVehicleTypeFromPeiodo() {
        VagaGaragem vagaGaragem = new VagaGaragem();
        vagaGaragem.setTipoVeiculo(VehicleType.BICICLETA);
        Periodo periodo = new Periodo();
        periodo.setTipoVeiculo(VehicleType.CARRO);
        vagaGaragem.setPeriodo(periodo);

        assertThrows(InvalidVehicleTipoFromPeriodo.class, () -> {
            vagaGaragemService.validateTipoPeriodo(vagaGaragem);
        });

    }

}
