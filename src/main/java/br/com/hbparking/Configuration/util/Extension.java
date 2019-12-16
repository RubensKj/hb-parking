package br.com.hbparking.Configuration.util;

public enum Extension {


    CSV("csv"),
    XML("xml"),
    JSON("json");

    private final String descricao;

    Extension(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
