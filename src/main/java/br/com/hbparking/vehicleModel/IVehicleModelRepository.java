package br.com.hbparking.vehicleModel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface IVehicleModelRepository extends JpaRepository<VehicleModel, Long> {
    //get the model by brand
    Map<Long, List<String>> findAllGroupByIdMarca(Long idMarca);

}
