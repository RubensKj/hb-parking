package br.com.hbparking.tipoveiculo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = {"http://localhost:4200", "http://192.168.33.216:4200", "https://192.168.33.216:4200"})
@RestController
@RequestMapping("/api")
public class VehicleTypeController {

    private final VehicleTypeService vehicleTypeService;

    @Autowired
    public VehicleTypeController(VehicleTypeService vehicleTypeService) {
        this.vehicleTypeService = vehicleTypeService;
    }

    @GetMapping("/tipos-veiculos")
    public List<VehicleType> getAllTypes() {
        return vehicleTypeService.parseAllVehicleTypesToList();
    }

    @GetMapping("/tipos-veiculos-completos")
    public List<VehicleTypeDTO> getAllCompletedTypes() {
        return vehicleTypeService.getAllVehicleTypesInDTO();
    }
}
