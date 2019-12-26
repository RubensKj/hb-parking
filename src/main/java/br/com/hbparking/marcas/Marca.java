package br.com.hbparking.marcas;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "marcas")
@Data
@NoArgsConstructor
public class Marca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "tipoVeiculo", length = 15, nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoVeiculoEnum tipoVeiculo;

    @Column(name = "nome", length = 100, nullable = false)
    private String nome;

    public Marca(TipoVeiculoEnum tipoVeiculo, String nome) {
        this.tipoVeiculo = tipoVeiculo;
        this.nome = nome;
    }


}
