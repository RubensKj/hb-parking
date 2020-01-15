package br.com.hbparking.vagadegaragem;

import br.com.hbparking.periodo.PeriodoDTO;
import br.com.hbparking.vagainfo.VagaInfoDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public class VagasContent {

    private Page<VagaGaragem> vagaGaragemPage;

    private PeriodoDTO periodoDTO;

    private VagaInfoDTO vagaInfoDTO;

    private List<PeriodoDTO> periodosDTOOfVehicleType;

    public VagasContent() {
    }

    public VagasContent(Page<VagaGaragem> vagaGaragemPage, PeriodoDTO periodoDTO, VagaInfoDTO vagaInfoDTO, List<PeriodoDTO> periodosDTOOfVehicleType) {
        this.vagaGaragemPage = vagaGaragemPage;
        this.periodoDTO = periodoDTO;
        this.vagaInfoDTO = vagaInfoDTO;
        this.periodosDTOOfVehicleType = periodosDTOOfVehicleType;
    }

    public Page<VagaGaragem> getVagaGaragemPage() {
        return vagaGaragemPage;
    }

    public PeriodoDTO getPeriodoDTO() {
        return periodoDTO;
    }

    public VagaInfoDTO getVagaInfoDTO() {
        return vagaInfoDTO;
    }

    public List<PeriodoDTO> getPeriodosDTOOfVehicleType() {
        return periodosDTOOfVehicleType;
    }
}
