package br.com.hbparking.vagadegaragem;

import br.com.hbparking.periodo.Periodo;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("api/reports/locatarios")
public class VagaGaragemHistoryController {

    private final VagaGaragemHistoryService vagaGaragemHistoryService;

    public VagaGaragemHistoryController(VagaGaragemHistoryService vagaGaragemHistoryService) {
        this.vagaGaragemHistoryService = vagaGaragemHistoryService;
    }

    @GetMapping("/export/columns/{idPeriodo}/{marca}/{email}/{tipo}/{cor}")
    public void exportByColumns(HttpServletResponse response, @PathVariable("idPeriodo") Long idPeriodo, @PathVariable("marca") boolean marca, @PathVariable("email") boolean email, @PathVariable("tipo") boolean tipo, @PathVariable("cor") boolean cor) throws IOException {
        this.vagaGaragemHistoryService.exportByColumns(response, idPeriodo, marca, email, tipo, cor);
    }

}
