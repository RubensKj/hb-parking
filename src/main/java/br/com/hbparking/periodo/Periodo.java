package br.com.hbparking.periodo;

import br.com.hbparking.marcas.TipoVeiculoEnum;
import br.com.hbparking.tipoveiculo.VehicleType;

import javax.persistence.*;
import java.time.LocalDate;

@Entity(name = "periodo_locacao")
public class Periodo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_veiculo", nullable = false)
    public VehicleType tipoVeiculo;

    @Column(name = "data_inicial", nullable = false)
    public LocalDate dataInicial;

    @Column(name = "data_final", nullable = false)
    public LocalDate dataFinal;

    public Periodo() {
    }

    public Periodo(VehicleType tipoVeiculo, LocalDate dataInicial, LocalDate dataFinal) {
        this.tipoVeiculo = tipoVeiculo;
        this.dataInicial = dataInicial;
        this.dataFinal = dataFinal;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public VehicleType getTipoVeiculo() {
        return tipoVeiculo;
    }

    public void setTipoVeiculo(VehicleType tipoVeiculo) {
        this.tipoVeiculo = tipoVeiculo;
    }

    public LocalDate getDataInicial() {
        return dataInicial;
    }

    public void setDataInicial(LocalDate dataInicial) {
        this.dataInicial = dataInicial;
    }

    public LocalDate getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(LocalDate dataFinal) {
        this.dataFinal = dataFinal;
    }
}
