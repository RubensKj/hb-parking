package br.com.hbparking.periodo;

import br.com.hbparking.tipoveiculo.VehicleType;
import br.com.hbparking.util.DateHelper;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

import static br.com.hbparking.util.DateHelper.formatDate;

public class PeriodoDTO {

    public Long id;

    public VehicleType vehiclvehieType;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    public LocalDate dataInicial;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    public LocalDate dataFinal;

    public String descricao;

    public PeriodoDTO() {
    }

    public PeriodoDTO(VehicleType vehicleType, LocalDate dataInicial, LocalDate dataFinal) {
        this.vehicleType = vehicleType;
        this.dataInicial = dataInicial;
        this.dataFinal = dataFinal;
    }

    public PeriodoDTO(Long id, VehicleType vehicleType, String descricao, LocalDate dataFinal, LocalDate dataInicial) {
        this.id = id;
        this.vehicleType = vehicleType;
        this.descricao = descricao;
        this.dataFinal = dataFinal;
        this.dataInicial = dataInicial;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public static PeriodoDTO of(Periodo periodo) {
        return new PeriodoDTO(
                periodo.getId(),
                periodo.getTipoVeiculo(),
                "De " + formatDate(periodo.getDataInicial()) + " até " + formatDate(periodo.getDataFinal()),
                periodo.getDataFinal(),
                periodo.getDataInicial()
        );
    }
}
