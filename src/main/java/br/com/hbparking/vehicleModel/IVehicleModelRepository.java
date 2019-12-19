package br.com.hbparking.vehicleModel;

import br.com.hbparking.csv.VehicleGroupDTO;
import br.com.hbparking.marcas.Marca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface IVehicleModelRepository extends JpaRepository<VehicleModel, Long> {
    //get the model by brand
    @Query("select new br.com.hbparking.csv.VehicleGroupDTO(vm.fkMarca.id, vm.modelo) from VehicleModel vm order by fkMarca")
    List<VehicleGroupDTO> findAllGroupByFkMarca();

}
