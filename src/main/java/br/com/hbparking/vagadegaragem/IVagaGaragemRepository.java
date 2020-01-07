package br.com.hbparking.vagadegaragem;

import br.com.hbparking.tipoveiculo.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IVagaGaragemRepository extends JpaRepository<VagaGaragem, Long> {

    List<VagaGaragem> findAllByStatusVagaAndTipoVeiculo(StatusVaga statusVaga, VehicleType tipoVeiculo);

}
