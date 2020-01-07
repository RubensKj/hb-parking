package br.com.hbparking.vagaInfo;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/vaga-info")
public class VagaInfoController {

    private final VagaInfoService vagaInfoService;

    public VagaInfoController(VagaInfoService vagaInfoService) {
        this.vagaInfoService = vagaInfoService;
    }

    @PostMapping("/cadastrar")
    public VagaInfoDTO create(@RequestBody VagaInfoDTO vagaInfoDTO) throws PeriodoAlreadyExistsException {
        return this.vagaInfoService.cadastrar(vagaInfoDTO);
    }
}
