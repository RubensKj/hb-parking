package br.com.hbparking.marcas;


import lombok.*;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class MarcaDTO {

    private Long id;
    private TipoVeiculoEnum tipoVeiculo;
    private String nome;


    public MarcaDTO(TipoVeiculoEnum tipoVeiculo, String nome) {
        this.tipoVeiculo = tipoVeiculo;
        this.nome = nome;
    }

    public static MarcaDTO of(Marca marca){
        return new MarcaDTO(
                marca.getId(),
                marca.getTipoVeiculo(),
                marca.getNome()
                );
    }


}
