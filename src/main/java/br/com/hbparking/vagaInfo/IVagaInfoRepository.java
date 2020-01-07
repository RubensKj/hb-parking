package br.com.hbparking.vagaInfo;

import br.com.hbparking.periodo.Periodo;
import br.com.hbparking.tipoveiculo.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IVagaInfoRepository extends JpaRepository<VagaInfo, Long> {

    Optional<VagaInfo> findByPeriodoAndVehicleTypeIs(Periodo periodo, VehicleType vehicleType);
}