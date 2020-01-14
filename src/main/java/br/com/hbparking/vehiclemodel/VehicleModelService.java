package br.com.hbparking.vehiclemodel;

import br.com.hbparking.csv.VehicleGroupDTO;
import br.com.hbparking.marcas.Marca;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VehicleModelService {
    private final IVehicleModelRepository vehicleModelRepository;

    @Autowired
    public VehicleModelService(IVehicleModelRepository vehicleModelRepository) {
        this.vehicleModelRepository = vehicleModelRepository;
    }

    //save the vehicle model
    public void saveVehicle(VehicleModel model) {
        this.vehicleModelRepository.save(model);
    }

    public VehicleModel findById(Long id) {
        Optional<VehicleModel> vehicleModel = this.vehicleModelRepository.findById(id);
        if (vehicleModel.isPresent()) {
            return vehicleModel.get();
        }
        throw new IllegalArgumentException(String.format("ID %s do modelo não existe", id));
    }

    //return all models grouped by brand
    public Map<Long, List<VehicleGroupDTO>> getVehicleModelsOrderedByBrand() throws Exception {
        List<VehicleGroupDTO> list = this.vehicleModelRepository.findAllGroupByFkMarca();

        Map<Long, List<VehicleGroupDTO>> mapModelsOrderedByMarca = list.stream().collect(Collectors.groupingBy(VehicleGroupDTO::getCodigoMarca));

        return mapModelsOrderedByMarca;
    }

    public boolean exists(VehicleModel model) {
        return this.vehicleModelRepository.existsByModelo(model.getModelo());
    }

    public void deleteAllByModeloIsNotIn(List<String> nomes) {
        this.vehicleModelRepository.deleteAllByModeloIsNotIn(nomes);
    }

    public List<VehicleModel> findByMarcaAndModelo(Marca fkMarca, String modelo){
        return this.vehicleModelRepository.findByFkMarcaAndModeloContains(fkMarca, modelo);
    }

    /*Remover esse método após uso*/
    public VehicleModel findByModelo(String modelo){

        Optional<VehicleModel> vehicleModel = this.vehicleModelRepository.findByModelo(modelo);

        if(vehicleModel.isPresent()){
            return vehicleModel.get();
        }else{
            throw new IllegalArgumentException("Erro");
        }

    }

}
