package br.com.hbparking.vagaInfo;

import br.com.hbparking.tipoveiculo.VehicleType;

public class VagaInfoDTO {

    private Long id;

    private int quantidade;

    private Long idPeriodo;

    private VehicleType vehicleType;

    private double valor;

    public VagaInfoDTO() {
    }

    public VagaInfoDTO(Long id, int quantidade, VehicleType vehicleType, double valor) {
        this.id = id;
        this.quantidade = quantidade;
        this.vehicleType = vehicleType;
        this.valor = valor;
    }

    public VagaInfoDTO(int quantidade, Long idPeriodo, VehicleType vehicleType, double valor) {
        this.quantidade = quantidade;
        this.idPeriodo = idPeriodo;
        this.vehicleType = vehicleType;
        this.valor = valor;
    }

    public static VagaInfoDTO of(VagaInfo vagaInfo) {

        return new VagaInfoDTO(
                vagaInfo.getId(),
                vagaInfo.getQuantidade(),
                vagaInfo.getVehicleType(),
                vagaInfo.getValor()
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public Long getIdPeriodo() {
        return idPeriodo;
    }

    public void setIdPeriodo(Long idPeriodo) {
        this.idPeriodo = idPeriodo;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }
}

