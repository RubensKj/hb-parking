package br.com.hbparking.marcas;

public class MarcaDTO {

    private Long id;
    private TipoVeiculoEnum tipoVeiculo;
    private String nome;

    public MarcaDTO() {
    }

    public MarcaDTO(Long id, TipoVeiculoEnum tipoVeiculo, String nome) {
        this.id = id;
        this.tipoVeiculo = tipoVeiculo;
        this.nome = nome;
    }

    public MarcaDTO(TipoVeiculoEnum tipoVeiculo, String nome) {
        this.tipoVeiculo = tipoVeiculo;
        this.nome = nome;
    }

    public static MarcaDTO of(Marca marca) {
        return new MarcaDTO(
                marca.getId(),
                marca.getTipoVeiculo(),
                marca.getNome()
        );
    }

    public Long getId() {
        return id;
    }

    public TipoVeiculoEnum getTipoVeiculo() {
        return tipoVeiculo;
    }

    public void setTipoVeiculo(TipoVeiculoEnum tipoVeiculo) {
        this.tipoVeiculo = tipoVeiculo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
