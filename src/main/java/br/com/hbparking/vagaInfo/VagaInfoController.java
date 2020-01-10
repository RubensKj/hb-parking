package br.com.hbparking.vagaInfo;

import br.com.hbparking.periodo.Periodo;
import br.com.hbparking.tipoveiculo.VehicleType;
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

    @GetMapping("/findBy/{turno}/{tipo}")
    public VagaInfoDTO findByPeriodoTurnoTipo(@PathVariable("turno") Turno turno, @PathVariable("tipo") VehicleType tipo, @RequestBody Periodo periodo) throws VagaInfoNotFoundException {
        return VagaInfoDTO.of(this.vagaInfoService.findByPeriodoAndVehicleTypeAndTurno(periodo, tipo, turno));
    }
}
