package br.com.hbparking.periodo;

import br.com.hbparking.tipoveiculo.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IPeriodoRepository extends JpaRepository<Periodo, Long> {
    List<Periodo> findAllByTipoVeiculo(VehicleType tipoVeiculo);

}
