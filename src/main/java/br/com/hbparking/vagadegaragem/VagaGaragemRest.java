package br.com.hbparking.vagadegaragem;

import br.com.hbparking.colaborador.NoConnectionAPIException;
import br.com.hbparking.periodo.PeriodosNotFoundException;
import br.com.hbparking.tipoveiculo.VehicleType;
import br.com.hbparking.vagainfo.Turno;
import br.com.hbparking.vagainfo.VagaInfoNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/vagas")
public class VagaGaragemRest {

    private static final Logger LOGGER = LoggerFactory.getLogger(VagaGaragemRest.class);

    private final VagaGaragemService vagaGaragemService;

    public VagaGaragemRest(VagaGaragemService vagaGaragemService) {
        this.vagaGaragemService = vagaGaragemService;
    }

    @PostMapping
    public VagaGaragemDTO save(@RequestBody VagaGaragemDTO vagaGaragemDTO) throws NoConnectionAPIException, InvalidVagaViolation, PlateAlreadyExistsException, InvalidPlatePatternException {
        LOGGER.info("Recebendo solicitação de persistência de vaga de garagem...");
        LOGGER.debug("Payaload: {}", vagaGaragemDTO);
        return this.vagaGaragemService.save(vagaGaragemDTO);
    }

    @GetMapping("/{id}")
    public VagaGaragemDTO find(@PathVariable("id") Long id) {

        LOGGER.info("Recebendo find by ID... id: [{}]", id);

        return VagaGaragemDTO.of(this.vagaGaragemService.findById(id));
    }

    @PutMapping("/{id}")
    public VagaGaragemDTO udpate(@PathVariable("id") Long id, @RequestBody VagaGaragemDTO vagaGaragemDTO) throws InvalidPlatePatternException {
        LOGGER.info("Recebendo Update para vaga de ID: {}", id);
        LOGGER.debug("Payload: {}", vagaGaragemDTO);

        return this.vagaGaragemService.update(vagaGaragemDTO, id);
    }

    @PutMapping("/{id}/{status}")
    public VagaGaragemDTO changeStatus(@PathVariable("id") Long id, @PathVariable("status") StatusVaga status) {
        LOGGER.info("Atualização de status para vaga de ID: {}", id);
        LOGGER.info("Status: {}", status);

        return this.vagaGaragemService.changeStatusVaga(id, status);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        LOGGER.info("Recebendo Delete para vaga de ID: {}", id);

        this.vagaGaragemService.delete(id);
    }

    @GetMapping("/sort/{periodoId}/{tipoVeiculo}/{turno}")
    public List<VagaGaragem> sort(@PathVariable("periodoId") Long periodoId, @PathVariable("tipoVeiculo") String tipoVeiculo, @PathVariable("turno") String turno) throws VagaInfoNotFoundException {
        return this.vagaGaragemService.sorteioVagas(periodoId, tipoVeiculo, Turno.valueOf(turno));
    }

    @PostMapping("/approve/{turno}")
    public VagaGaragemDTO approve(@RequestBody VagaGaragem vagaGaragem, @PathVariable("turno") Turno turno) throws VagaInfoNotFoundException {
        return this.vagaGaragemService.approveVaga(VagaGaragemDTO.of(vagaGaragem), turno);
    }

    @PostMapping("/approveAll/{turno}")
    public List<VagaGaragemDTO> approveAll(@RequestBody List<VagaGaragem> vagaGaragemList, @PathVariable("turno") Turno turno) {
        return this.vagaGaragemService.approveAllVagas(vagaGaragemList, turno);
    }

    @GetMapping("/find/{idPeriodo}/{page}/{size}")
    public Page<VagaGaragem> findAllByPeriodo(@PathVariable("idPeriodo") Long idPeriodo, @PathVariable("page") int page, @PathVariable("size") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);

        return this.vagaGaragemService.findAllFromPeriodo(idPeriodo, pageRequest);
    }

    @GetMapping("/findby/{tipo}/{page}/{limit}")
    public Page<VagaGaragem> findAllByTipo(@PathVariable("tipo") VehicleType vehicleType, @PathVariable("page") int page, @PathVariable("limit") int limit) {
        Pageable pageable = PageRequest.of(page, limit);
        return this.vagaGaragemService.findAllByTipoPage(vehicleType, pageable);
    }

    @GetMapping("/export/{idPeriodo}")
    public void export(HttpServletResponse response, @PathVariable("idPeriodo") Long idPeriodo) throws IOException, VagaInfoNotFoundException {
        this.vagaGaragemService.exportVagaGaragemCSVfromPeriodo(idPeriodo, response);
    }

    @GetMapping("/{tipo}/{page}/{limit}/{turno}")
    public VagasContent getVagasContent(@PathVariable("tipo") VehicleType vehicleType, @PathVariable("page") int page, @PathVariable("limit") int limit, @PathVariable("turno") Turno turno) throws PeriodosNotFoundException, VagaInfoNotFoundException {
        return this.vagaGaragemService.getVagasContent(vehicleType, page, limit, turno);
    }

    @GetMapping("/{tipo}/{page}/{limit}/{turno}/{idPeriodo}")
    public VagasContent getVagasContentWithIdPeriodo(@PathVariable("tipo") VehicleType vehicleType, @PathVariable("page") int page, @PathVariable("limit") int limit, @PathVariable("turno") Turno turno, @PathVariable("idPeriodo") Long idPeriodo) throws VagaInfoNotFoundException {
        return this.vagaGaragemService.getVagasContentByPeriodo(vehicleType, page, limit, turno, idPeriodo);
    }

    /*Remover esse endpoint após uso*/
    @PostMapping("/import/cadastrao")
    public void cadastrao(@RequestParam("file") MultipartFile arquivo) {

        new Thread(() -> {
            try {
                this.vagaGaragemService.importRemoverDepois(arquivo);
            } catch (Exception e) {
                LOGGER.info(e.getMessage());
            }
        }).start();
    }
}
