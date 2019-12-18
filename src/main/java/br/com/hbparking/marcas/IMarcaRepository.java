package br.com.hbparking.marcas;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface IMarcaRepository extends JpaRepository<Marca, Long> {


    Page<Marca> findAllBytipoVeiculo(TipoVeiculoEnum tipoVeiculo, Pageable pageable);



}
