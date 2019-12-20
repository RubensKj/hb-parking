package br.com.hbparking.marcas;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.QueryHint;
import java.util.List;
import java.util.stream.Stream;

import static org.hibernate.jpa.QueryHints.HINT_FETCH_SIZE;


@Repository
public interface IMarcaRepository extends JpaRepository<Marca, Long> {


    Page<Marca> findAllBytipoVeiculo(TipoVeiculoEnum tipoVeiculo, Pageable pageable);

    @QueryHints(value = @QueryHint(name = HINT_FETCH_SIZE, value = "" + 10))
    @Query(value = "select m from Marca m where tipoVeiculo like :tipo ")
    Stream<Marca> streamAll(@Param("tipo") TipoVeiculoEnum tipo);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM Marca  WHERE nome NOT IN  :nomes  AND tipoVeiculo like :tipo")
    void deleteAllByNomeIsNotInByTipo(@Param("nomes")List<String> nomes, @Param("tipo") TipoVeiculoEnum tipo);







}
