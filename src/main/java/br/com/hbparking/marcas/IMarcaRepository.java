package br.com.hbparking.marcas;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IMarcaRepository extends JpaRepository<Marca, Long> {

    @Query(value = "select * from marcas where tipo_veiculo like :tipo ", nativeQuery = true)
    List<Marca> findMarcaBytipo(@Param("tipo") String tipo);

    @Query(value = "select * from marcas where nome like :nome ", nativeQuery = true)
    Optional<Marca> findMarcaByNome(@Param("nome") String nome);




}
