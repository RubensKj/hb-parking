package br.com.hbparking.marcas;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IMarcaRepository extends JpaRepository<Marca, Long> {

    //@Query(value = "select * from marcas where tipo_veiculo like :tipo ", nativeQuery = true)
    //List<Marca> findMarcaBytipoVeiculo(@Param("tipo") String tipo);
    Page<Marca> findAllBytipoVeiculo(TipoVeiculoEnum tipoVeiculo, Pageable pageable);



}
