package br.com.hbparking.vagadegaragem;

import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.time.LocalDateTime;

@Document(collection = "vaga_garagem_history")
public class VagaGaragemHistory {

    @Id
    private String id;

    private VagaGaragem vagaGaragem;

    private int score;

    private String emailGestor;

    private LocalDateTime horarioAcao;

    private String acao;

    public VagaGaragemHistory() {
    }

    public VagaGaragemHistory(VagaGaragem vagaGaragem) {
        this.vagaGaragem = vagaGaragem;
    }

    public VagaGaragemHistory(VagaGaragem vagaGaragem, int score, LocalDateTime horarioAcao, String acao) {
        this.vagaGaragem = vagaGaragem;
        this.score = score;
        this.horarioAcao = horarioAcao;
        this.acao = acao;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
