package br.com.hbparking.marcas;

import javax.persistence.*;

@Entity
@Table(name = "marcas")
public class Marca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "tipoVeiculo")
    @Enumerated(EnumType.STRING)
    private TipoEnum tipoVeiculo;

    @Column(name = "nome")
    private  String nome;

    public Marca() {
    }

    public Marca( TipoEnum tipoVeiculo, String nome) {
        this.tipoVeiculo = tipoVeiculo;
        this.nome = nome;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoEnum getTipoVeiculo() {
        return tipoVeiculo;
    }

    public void setTipoVeiculo(TipoEnum tipoVeiculo) {
        this.tipoVeiculo = tipoVeiculo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return "Marca{" +
                "id=" + id +
                ", Nome ='" + getNome() + '\'' +
                ", Tipo veiculo ='" + getTipoVeiculo().getDescricao() + '\'' +
                '}';
    }
}
