package br.com.hbparking.vagadegaragem;

public enum StatusVaga {
    VIGENTE("VIGENTE"),
    EMAPROVACAO("EM APROVACAO"),
    FILADEESPERA("FILA DE ESPERA"),
    APROVADA("APROVADA");

    private final String descricao;

    StatusVaga(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
