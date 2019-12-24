package br.com.hbparking.marcas;

import lombok.*;

import javax.persistence.*;
@Entity
@Table(name = "marcas")
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class Marca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Getter @Setter
    private Long id;

    @Column(name = "tipoVeiculo")
    @Enumerated(EnumType.STRING)
    @Getter @Setter
    private TipoVeiculoEnum tipoVeiculo;

    @Column(name = "nome")
    @Getter @Setter
    private  String nome;


    public Marca(TipoVeiculoEnum tipoVeiculo, String nome) {
        this.tipoVeiculo = tipoVeiculo;
        this.nome = nome;
    }


}
