package br.com.hbparking.tipoveiculo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@WebMvcTest(VehicleTypeController.class)
public class VehicleTypesControllerTest {

    @Autowired
    private VehicleTypeController vehicleTypeController;

    @MockBean
    private VehicleTypeService vehicleTypeService;

    @Test
    public void getTypes_returnAllTypesOfVehicles() {
        given(vehicleTypeService.parseAllVehicleTypesToList()).willReturn(new ArrayList<>(EnumSet.allOf(VehicleType.class)));

        List<VehicleType> vehicleTypes = vehicleTypeController.getAllTypes();

        assertThat(vehicleTypes).isNotNull();
        assertThat(vehicleTypes).isNotEmpty();
        assertThat(vehicleTypes).isEqualTo(new ArrayList<>(EnumSet.allOf(VehicleType.class)));
    }

    @Test
    public void getTypes_returnCompletedTypesOfVehicles() {
        given(vehicleTypeService.getAllVehicleTypesInDTO()).willReturn(getDefaultVehicleTypesInDTO());

        List<VehicleTypeDTO> vehicleTypes = vehicleTypeController.getAllCompletedTypes();

        assertThat(vehicleTypes).isNotNull();
        assertThat(vehicleTypes).isNotEmpty();
        assertThat(vehicleTypes.get(0).getCodigo()).isEqualTo(VehicleType.valueOf("AUTOMOVEL").ordinal());
        assertThat(vehicleTypes.get(1).getCodigo()).isEqualTo(VehicleType.valueOf("BICICLETA").ordinal());
        assertThat(vehicleTypes.get(2).getCodigo()).isEqualTo(VehicleType.valueOf("MOTO").ordinal());
        assertThat(vehicleTypes.get(3).getCodigo()).isEqualTo(VehicleType.valueOf("PATINETE").ordinal());
    }

    private List<VehicleTypeDTO> getDefaultVehicleTypesInDTO() {
        return new ArrayList<>(Arrays.asList(new VehicleTypeDTO(0, "AUTOMOVEL"),
                new VehicleTypeDTO(1, "BICICLETA"),
                new VehicleTypeDTO(2, "MOTO"),
                new VehicleTypeDTO(3, "PATINETE")));
    }
}
