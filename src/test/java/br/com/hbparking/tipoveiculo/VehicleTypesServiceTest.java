package br.com.hbparking.tipoveiculo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class VehicleTypesServiceTest {
 
    private VehicleTypeService vehicleTypeService;

    @Before
    public void setUp() {
        vehicleTypeService = new VehicleTypeService();
    }

    @Test
    public void parseAllVehicleTypesToList() {
        List<VehicleType> vehicleTypes = vehicleTypeService.parseAllVehicleTypesToList();

        assertThat(vehicleTypes).isNotNull();
        assertThat(vehicleTypes).isNotEmpty();
        assertThat(vehicleTypes).isEqualTo(new ArrayList<>(EnumSet.allOf(VehicleType.class)));
    }

    @Test
    public void getAllVehicleTypesInDTOTest() {
        List<VehicleTypeDTO> vehicleTypeDTOs = vehicleTypeService.getAllVehicleTypesInDTO();

        assertThat(vehicleTypeDTOs).isNotNull();
        assertThat(vehicleTypeDTOs).isNotEmpty();
    }

    @Test
    public void parseAllVehicleTypesToDTOListTest() {
        List<VehicleTypeDTO> vehicleTypeDTOs = vehicleTypeService.parseAllVehicleTypesToDTOList(new ArrayList<>(EnumSet.allOf(VehicleType.class)));

        assertThat(vehicleTypeDTOs).isNotNull();
        assertThat(vehicleTypeDTOs).isNotEmpty();
    }
}
