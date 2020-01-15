package br.com.hbparking.vagadegaragem;

import br.com.hbparking.periodo.Periodo;
import br.com.hbparking.tipoveiculo.VehicleType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IVagaGaragemRepository extends JpaRepository<VagaGaragem, Long> {

    List<VagaGaragem> findAllByStatusVagaAndTipoVeiculo(StatusVaga statusVaga, VehicleType tipoVeiculo);

    Page<VagaGaragem> findByPeriodo(Periodo periodo, Pageable pageable);

    List<VagaGaragem> findByPeriodo(Periodo periodo);

    Page<VagaGaragem> findByPeriodoAndStatusVagaAndTipoVeiculoAndColaborador_TrabalhoNoturno(Periodo periodo, StatusVaga statusVaga, VehicleType vehicleType, boolean trabalho_noturno, Pageable pageable);

    Page<VagaGaragem> findAllByTipoVeiculo(VehicleType vehicleType, Pageable pageable);

    List<VagaGaragem> findAllByTipoVeiculoAndColaborador_TrabalhoNoturnoAndPeriodo_IdAndStatusVaga(VehicleType vehicleType, boolean trabalhoNoturno, Long periodoId, StatusVaga statusVaga);

    List<VagaGaragemDTO> findAllByPlacaAndPeriodoAndStatusVaga(String placa, Periodo periodo, StatusVaga statusVaga);
}
