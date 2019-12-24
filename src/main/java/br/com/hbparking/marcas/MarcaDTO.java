package br.com.hbparking.marcas;


import lombok.*;

@NoArgsConstructor
@EqualsAndHashCode
@ToString
@AllArgsConstructor
public class MarcaDTO {

    @Getter @Setter
    private Long id;
    @Getter @Setter
    private TipoVeiculoEnum tipoVeiculo;
    @Getter @Setter
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
