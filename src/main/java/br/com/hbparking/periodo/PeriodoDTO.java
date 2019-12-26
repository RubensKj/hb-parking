package br.com.hbparking.periodo;

import br.com.hbparking.tipoveiculo.VehicleType;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public class PeriodoDTO {

    public Long id;

    public VehicleType vehicleType;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    public LocalDate dataInicial;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    public LocalDate dataFinal;

    public PeriodoDTO() {
    }

    public PeriodoDTO(VehicleType vehicleType, LocalDate dataInicial, LocalDate dataFinal) {
        this.vehicleType = vehicleType;
        this.dataInicial = dataInicial;
        this.dataFinal = dataFinal;
    }

    public PeriodoDTO(VehicleType tipoVeiculo, LocalDate dataFinal, LocalDate dataInicial, Long id) {
        this.id = id;
        this.vehicleType = tipoVeiculo;
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

    public static PeriodoDTO of(Periodo periodo) {
        return new PeriodoDTO(
                periodo.getTipoVeiculo(),
                periodo.getDataFinal(),
                periodo.getDataInicial(),
                periodo.getId()
        );
    }
}
