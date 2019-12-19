package br.com.hbparking.marcas;

public enum TipoVeiculoEnum {
    CARRO("CARRO"),
    MOTO("MOTO");

    private final String descricao;

    TipoVeiculoEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
