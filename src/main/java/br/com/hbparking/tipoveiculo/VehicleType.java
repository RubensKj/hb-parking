package br.com.hbparking.tipoveiculo;

public enum VehicleType {
    CARRO("CARRO"),
    BICICLETA("BICICLETA"),
    MOTO("MOTO"),
    PATINETE("PATINETE");

    private final String descricao;

    VehicleType(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
