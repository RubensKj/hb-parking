package br.com.hbparking.periodo;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
