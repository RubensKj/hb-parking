package br.com.hbparking.vehicleModel;

import br.com.hbparking.vehicleException.EmptyMapException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VehicleModelService {
    private final IVehicleModelRepository vehicleModelRepository;

    @Autowired
    public VehicleModelService(IVehicleModelRepository vehicleModelRepository) {
        this.vehicleModelRepository = vehicleModelRepository;
    }

    //clear table entries
    public void clearTable() {
        this.vehicleModelRepository.deleteAll();
    }

    //save the vehicle model
    public void saveVehicle(List<VehicleModel> vehicleModelList) {
        this.vehicleModelRepository.saveAll(vehicleModelList);
    }

    //return all models grouped by brand
    public Map<Long, List<String>> getVehicleModelsOrderedByBrand() throws Exception {
        Map<Long, List<String>> mapOfModelsByBrand = new HashMap<>();



        if (!mapOfModelsByBrand.isEmpty()) {
            return mapOfModelsByBrand;
        }
        throw new EmptyMapException("O banco não contem informações para serem exportadas.");
    }
}
