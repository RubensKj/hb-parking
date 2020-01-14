package br.com.hbparking.vagainfo;

import br.com.hbparking.periodo.PeriodoService;
import br.com.hbparking.tipoveiculo.VehicleType;
import br.com.hbparking.vagadegaragem.StatusVaga;
import br.com.hbparking.vagadegaragem.VagaGaragemService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vaga-info")
public class VagaInfoController {

    private final VagaInfoService vagaInfoService;
    private final PeriodoService periodoService;
    private final VagaGaragemService vagaGaragemService;

    public VagaInfoController(VagaInfoService vagaInfoService, PeriodoService periodoService, VagaGaragemService vagaGaragemService) {
        this.vagaInfoService = vagaInfoService;
        this.periodoService = periodoService;
        this.vagaGaragemService = vagaGaragemService;
    }

    @PostMapping("/cadastrar")
    public VagaInfoDTO create(@RequestBody VagaInfoDTO vagaInfoDTO) throws VagaInfoAlreadyExistsException {
        return this.vagaInfoService.cadastrar(vagaInfoDTO);
    }

    @GetMapping("/find/{id}")
    public VagaInfoDTO findById(@PathVariable("id") Long id) throws VagaInfoNotFoundException {
        return VagaInfoDTO.of(this.vagaInfoService.findById(id));
    }

    @GetMapping("/findBy/{turno}/{tipo}/{idPeriodo}")
    public VagaInfoDTO findByPeriodoTurnoTipo(@PathVariable("turno") Turno turno, @PathVariable("tipo") VehicleType tipo, @PathVariable("idPeriodo") Long idPeriodo) throws VagaInfoNotFoundException {
        return VagaInfoDTO.of(this.vagaInfoService.findByPeriodoAndVehicleTypeAndTurno(this.periodoService.findById(idPeriodo), tipo, turno));
    }

    @GetMapping("/findAll/{turno}/{tipo}/{idPeriodo}/{status}")
    public int findAllAndReturnedFiltrada(@PathVariable("turno") Turno turno, @PathVariable("tipo") VehicleType tipo, @PathVariable("idPeriodo") Long idPeriodo, @PathVariable("status") StatusVaga statusVaga) {
        if (turno == Turno.NOTURNO) {
            return this.vagaGaragemService.getTotalElementsFilter(tipo, true, idPeriodo, statusVaga);
        } else {
            return this.vagaGaragemService.getTotalElementsFilter(tipo, false, idPeriodo, statusVaga);
        }
    }
}