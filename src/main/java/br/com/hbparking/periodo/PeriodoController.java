package br.com.hbparking.periodo;

import br.com.hbparking.tipoveiculo.VehicleType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"http://localhost:4200", "http://192.168.33.216:4200", "https://192.168.33.216:4200", "*", "http://192.168.32.28:4200", "https://192.168.32.28:4200"})
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

    @GetMapping("/periodo/buscar-tipo/{tipoVeiculo}/{page}/{limit}")
    public Page<Periodo> findAllByVehicleType(@PathVariable VehicleType tipoVeiculo, @PathVariable int page, @PathVariable int limit) {
        Pageable pageable = PageRequest.of(page, limit);

        return this.periodoService.findPeriodoByVehicleType(tipoVeiculo, pageable);
    }
}
