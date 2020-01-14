package br.com.hbparking.vagainfo;


public class VagaInfoDTO {

    private Long id;

    private int quantidade;

    private Long idPeriodo;

    private double valor;

    private Turno turno;

    public VagaInfoDTO() {
    }

    public VagaInfoDTO(Long id, int quantidade, double valor, Turno turno, Long idPeriodo) {
        this.id = id;
        this.quantidade = quantidade;
        this.valor = valor;
        this.turno = turno;
        this.idPeriodo = idPeriodo;
    }

    public VagaInfoDTO(int quantidade, Long idPeriodo, double valor) {
        this.quantidade = quantidade;
        this.idPeriodo = idPeriodo;
        this.valor = valor;
    }

    public static VagaInfoDTO of(VagaInfo vagaInfo) {

        return new VagaInfoDTO(
                vagaInfo.getId(),
                vagaInfo.getQuantidade(),
                vagaInfo.getValor(),
                vagaInfo.getTurno(),
                vagaInfo.getPeriodo().getId()
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public Long getIdPeriodo() {
        return idPeriodo;
    }

    public void setIdPeriodo(Long idPeriodo) {
        this.idPeriodo = idPeriodo;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public Turno getTurno() {
        return turno;
    }

    public void setTurno(Turno turno) {
        this.turno = turno;
    }
}

