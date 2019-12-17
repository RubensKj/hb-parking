package br.com.hbparking.tipoveiculo;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

@Service
public class VehicleTypeService {

    public List<VehicleType> parseAllVehicleTypesToList() {
        return new ArrayList<>(EnumSet.allOf(VehicleType.class));
    }

    public List<VehicleTypeDTO> getAllVehicleTypesInDTO() {
        return this.parseAllVehicleTypesToDTOList(new ArrayList<>(EnumSet.allOf(VehicleType.class)));
    }

    public List<VehicleTypeDTO> parseAllVehicleTypesToDTOList(List<VehicleType> vehicleTypes) {
        List<VehicleTypeDTO> vehicleTypesDTO = new ArrayList<>();
        vehicleTypes.forEach(vehicleType -> vehicleTypesDTO.add(new VehicleTypeDTO(vehicleType.ordinal(), vehicleType.name())));
        return vehicleTypesDTO;
    }
}
