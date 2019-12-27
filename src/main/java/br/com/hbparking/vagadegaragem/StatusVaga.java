package br.com.hbparking.vagadegaragem;

public enum StatusVaga {
    APROVADO("APROVADO"),
    EMAPROVACAO("EM APROVACAO"),
    NEGADO("NEGADO");

    private final String descricao;

    StatusVaga(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
