package br.com.hbparking.vagaInfo;

public enum Turno {
    INTEGRAL("INTEGRAL"),
    NOTURNO("NOTURNO");

    private final String descricao;

    Turno(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
