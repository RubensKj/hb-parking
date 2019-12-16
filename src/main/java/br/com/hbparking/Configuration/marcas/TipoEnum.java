package br.com.hbparking.Configuration.marcas;

public enum TipoEnum {
    CARRO("CARRO"),
    MOTO("MOTO");

    private final String descricao;

    TipoEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
