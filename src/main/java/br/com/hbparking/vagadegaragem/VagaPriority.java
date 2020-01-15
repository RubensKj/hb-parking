package br.com.hbparking.vagadegaragem;

public class VagaPriority {
    VagaGaragem vagaGaragem;
    Integer priority;

    public VagaPriority(VagaGaragem vagaGaragem, Integer priority) {
        this.vagaGaragem = vagaGaragem;
        this.priority = priority;
    }

    public VagaGaragem getVagaGaragem() {
        return vagaGaragem;
    }

    public void setVagaGaragem(VagaGaragem vagaGaragem) {
        this.vagaGaragem = vagaGaragem;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }
}
