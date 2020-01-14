package br.com.hbparking.vagadegaragem;

import br.com.hbparking.periodo.Periodo;
import br.com.hbparking.vagainfo.VagaInfo;
import org.springframework.data.domain.Page;

public class VagasContent {

    private Page<VagaGaragem> vagaGaragemPage;

    private Periodo periodo;

    private VagaInfo vagaInfo;

    public VagasContent() {
    }

    public VagasContent(Page<VagaGaragem> vagaGaragemPage, Periodo periodo, VagaInfo vagaInfo) {
        this.vagaGaragemPage = vagaGaragemPage;
        this.periodo = periodo;
        this.vagaInfo = vagaInfo;
    }

    public Page<VagaGaragem> getVagaGaragemPage() {
        return vagaGaragemPage;
    }

    public Periodo getPeriodo() {
        return periodo;
    }

    public VagaInfo getVagaInfo() {
        return vagaInfo;
    }
}
