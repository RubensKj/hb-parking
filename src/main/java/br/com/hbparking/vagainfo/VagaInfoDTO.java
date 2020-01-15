package br.com.hbparking.vagainfo;


import br.com.hbparking.tipoveiculo.VehicleType;

public class VagaInfoDTO {

    private Long id;

    private int quantidade;

    private Long idPeriodo;

    private double valor;

    private VehicleType vehicleType;

    private Turno turno;

    public VagaInfoDTO() {
    }

    public VagaInfoDTO(Long id, int quantidade, Long idPeriodo, double valor, VehicleType vehicleType, Turno turno) {
        this.id = id;
        this.quantidade = quantidade;
        this.idPeriodo = idPeriodo;
        this.valor = valor;
        this.vehicleType = vehicleType;
        this.turno = turno;
    }

    public static VagaInfoDTO of(VagaInfo vagaInfo) {
        return new VagaInfoDTO(
                vagaInfo.getId(),
                vagaInfo.getQuantidade(),
                vagaInfo.getPeriodo().getId(),
                vagaInfo.getValor(),
                vagaInfo.getVehicleType(),
                vagaInfo.getTurno()
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

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public Turno getTurno() {
        return turno;
    }

    public void setTurno(Turno turno) {
        this.turno = turno;
    }
}

