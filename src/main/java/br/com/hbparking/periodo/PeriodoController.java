package br.com.hbparking.periodo;

import br.com.hbparking.tipoveiculo.VehicleType;
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
    public PeriodoDTO save(@RequestBody PeriodoDTO periodoDTO) throws InvalidPeriodDatesException {
        return this.periodoService.create(periodoDTO);
    }

    @GetMapping("/periodo/buscar-tipo/{tipo}")
    public List<PeriodoDTO> findAllByVehicleType(@PathVariable("tipo") VehicleType vehicleType) {
        return this.periodoService.findPeriodosByVehicleType(vehicleType);
    }

    @GetMapping("/periodo/buscar-periodo")
    public List<PeriodoDTO> findAllPeriodos() {
        return this.periodoService.findAllPeriodos();
    }

}
