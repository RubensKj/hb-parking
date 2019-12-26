package br.com.hbparking.vehicleModel;

import br.com.hbparking.csv.VehicleGroupDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IVehicleModelRepository extends JpaRepository<VehicleModel, Long> {
    //get the model by brand
    @Query("select new br.com.hbparking.csvModelVehicle.VehicleGroupDTO(vm.fkMarca.id, vm.modelo) from VehicleModel vm order by fkMarca")
    List<VehicleGroupDTO> findAllGroupByFkMarca();

    void deleteAllByModeloIsNotIn(List<String> modeloNome);

    boolean existsByModelo(String modelo);
}
