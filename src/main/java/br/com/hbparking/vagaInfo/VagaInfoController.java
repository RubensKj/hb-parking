package br.com.hbparking.vagaInfo;

import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/find/{id}")
    public VagaInfoDTO findById(@PathVariable("id") Long id) throws VagaInfoNotFoundException {
        return VagaInfoDTO.of(this.vagaInfoService.findById(id));
    }
}
