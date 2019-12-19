package br.com.hbparking.tipoveiculo;

public class VehicleTypeDTO {

    private int codigo;
    private String descricao;

    public VehicleTypeDTO(int codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }
}
