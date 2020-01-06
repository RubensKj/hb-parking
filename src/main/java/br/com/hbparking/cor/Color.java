package br.com.hbparking.cor;

public enum Color {
    BRANCO("BRANCO"),
    PRETO("PRETO"),
    PRATA("PRATA"),
    CINZA("CINZA"),
    VERMELHO("VERMELHO"),
    MARROM("MARROM"),
    BEGE("BEGE"),
    AZUL("AZUL"),
    VERDE("VERDE"),
    AMARELO("AMARELO"),
    DOURADO("DOURADO"),
    OUTROS("OUTROS");

    private final String descricao;

    Color(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}