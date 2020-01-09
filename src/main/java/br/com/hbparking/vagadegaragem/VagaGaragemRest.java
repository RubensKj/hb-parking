package br.com.hbparking.vagadegaragem;

import br.com.hbparking.email.MailSenderService;
import br.com.hbparking.colaborador.NoConnectionAPIException;
import br.com.hbparking.vagaInfo.Turno;
import br.com.hbparking.vagaInfo.VagaInfoDTO;
import br.com.hbparking.vagaInfo.VagaInfoNotFoundException;
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
@RequestMapping("api/vagas")
public class VagaGaragemRest {

    private static final Logger LOGGER = LoggerFactory.getLogger(VagaGaragemRest.class);

    private final VagaGaragemService vagaGaragemService;
    private final MailSenderService mailSender;

    public VagaGaragemRest(VagaGaragemService vagaGaragemService,  MailSenderService mailSender) {
        this.vagaGaragemService = vagaGaragemService;
        this.mailSender = mailSender;
    }

    @PostMapping
    public VagaGaragemDTO save(@RequestBody VagaGaragemDTO vagaGaragemDTO) throws NoConnectionAPIException, InvalidVagaViolation {
        LOGGER.info("Recebendo solicitação de persistência de vaga de garagem...");
        LOGGER.debug("Payaload: {}", vagaGaragemDTO);
        return this.vagaGaragemService.save(vagaGaragemDTO);
    }

    @GetMapping(value = "/allVagasByPage")
    public Page<VagaGaragem> findAllVagas(Pageable pageable) {
        LOGGER.info("Recebendo requisição para buscar todas as vagas em paginas");
        return vagaGaragemService.findAllByTipoPage(pageable);
    }

    @GetMapping("/{id}")
    public VagaGaragemDTO find(@PathVariable("id") Long id) {

        LOGGER.info("Recebendo find by ID... id: [{}]", id);

        return VagaGaragemDTO.of(this.vagaGaragemService.findById(id));
    }

    @PutMapping("/{id}")
    public VagaGaragemDTO udpate(@PathVariable("id") Long id, @RequestBody VagaGaragemDTO vagaGaragemDTO) {
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

    @GetMapping("/sort/{qtdVagas}/{tipoVeiculo}")
    public List<VagaGaragem> sort(@PathVariable("qtdVagas") int qtdVagas, @PathVariable("tipoVeiculo") String tipoVeiculo){

        List<VagaGaragem> sorteados = this.vagaGaragemService.sorteioVagas(qtdVagas, tipoVeiculo);

        new Thread(() -> {
            this.mailSender.successListToBeEmailed(sorteados);
        }).start();

        return sorteados;
    }

    @PostMapping("/approve/{turno}")
    public VagaGaragemDTO approve(@RequestBody VagaGaragemDTO vagaGaragemDTO, @PathVariable("turno") Turno turno) throws VagaInfoNotFoundException {
        return this.vagaGaragemService.approveVaga(vagaGaragemDTO, turno);
    }

    @PostMapping("/approveAll/{turno}")
    public void approveAll(@RequestBody List<VagaGaragemDTO> vagaGaragemDTOList, @PathVariable("turno") Turno turno) {
        this.vagaGaragemService.approveAllVagas(vagaGaragemDTOList, turno);
    }

    @GetMapping("/find/{idPeriodo}/{page}/{size}")
    public Page<VagaGaragem> findAllByPeriodo(@PathVariable("idPeriodo") Long idPeriodo, @PathVariable("page") int page, @PathVariable("size") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);

        return this.vagaGaragemService.findAllFromPeriodo(idPeriodo, pageRequest);
    }

    @GetMapping("/export/{idPeriodo}")
    public void export(HttpServletResponse response, @PathVariable("idPeriodo") Long idPeriodo) throws IOException, VagaInfoNotFoundException {
        this.vagaGaragemService.exportVagaGaragemCSVfromPeriodo(idPeriodo, response);
    }
  
    /*Remover esse endpoint após uso*/
    @PostMapping("/import/cadastrao")
    public void cadastrao(@RequestParam("file")MultipartFile arquivo) throws Exception {

        new Thread(() -> {
            try {
                this.vagaGaragemService.importRemoverDepois(arquivo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

}
