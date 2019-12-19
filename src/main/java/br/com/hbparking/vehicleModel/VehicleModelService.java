package br.com.hbparking.vehicleModel;

import br.com.hbparking.csv.VehicleGroupDTO;
import br.com.hbparking.marcas.Marca;
import br.com.hbparking.vehicleException.EmptyMapException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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
    public Map<Long, List<VehicleGroupDTO>> getVehicleModelsOrderedByBrand() throws Exception {
        List<VehicleGroupDTO> list = this.vehicleModelRepository.findAllGroupByFkMarca();

        Map<Long, List<VehicleGroupDTO>> mapModelsOrderedByMarca = list.stream().collect(Collectors.groupingBy(VehicleGroupDTO::getCodigoMarca));

        return mapModelsOrderedByMarca;
    }
}
