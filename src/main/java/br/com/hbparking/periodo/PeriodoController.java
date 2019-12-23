package br.com.hbparking.periodo;

import br.com.hbparking.tipoveiculo.VehicleType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PeriodoController {

    private final PeriodoService periodoService;

    public PeriodoController(PeriodoService periodoService) {
        this.periodoService = periodoService;
    }

    @PostMapping("/periodo/criar")
    public PeriodoDTO save(@RequestBody PeriodoDTO periodoDTO) {
        return this.periodoService.create(periodoDTO);
    }

    @GetMapping("/periodo/buscar-tipo/{tipoVeiculo}")
    public List<PeriodoDTO> findAllByVehicleType(@PathVariable VehicleType tipoVeiculo) {
        return this.periodoService.findPeriodoByVehicleType(tipoVeiculo);
    }
}
