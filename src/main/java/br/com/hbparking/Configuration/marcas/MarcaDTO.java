package br.com.hbparking.Configuration.marcas;


public class MarcaDTO {

    private Long id;
    private TipoEnum tipoVeiculo;
    private String nome;

    public MarcaDTO() {
    }

    public MarcaDTO(Long id, TipoEnum tipoVeiculo, String nome) {
        this.id = id;
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
        return "MarcaDTO{" +
                "id=" + id +
                ", Nome ='" + getNome() + '\'' +
                ", Tipo veiculo: ='" + getTipoVeiculo().getDescricao() + '\'' +
                '}';
    }
}
