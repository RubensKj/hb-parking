package br.com.hbparking.vagadegaragem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/vagas")
public class VagaGaragemRest {

    private static final Logger LOGGER = LoggerFactory.getLogger(VagaGaragemRest.class);

    private final VagaGaragemService vagaGaragemService;

    public VagaGaragemRest(VagaGaragemService vagaGaragemService) {
        this.vagaGaragemService = vagaGaragemService;
    }

    @PostMapping
    public VagaGaragemDTO save(@RequestBody VagaGaragemDTO vagaGaragemDTO) throws Exception {
        LOGGER.info("Recebendo solicitação de persistência de vaga de garagem...");
        LOGGER.debug("Payaload: {}", vagaGaragemDTO);
        return this.vagaGaragemService.save(vagaGaragemDTO);
    }

    @RequestMapping("/allVagasByPage")
    public Page<VagaGaragem> findAllVagas(Pageable pageable) {
        LOGGER.info("Recebendo requisição para buscar todas as vagas em paginas");
        return vagaGaragemService.findAllByTipoPage(pageable);
    }
    @GetMapping("/{id}")
    public VagaGaragemDTO find(@PathVariable("id") Long id) {

        LOGGER.info("Recebendo find by ID... id: [{}]", id);

        return this.vagaGaragemService.findById(id);
    }

    @PutMapping("/{id}")
    public VagaGaragemDTO udpate(@PathVariable("id") Long id, @RequestBody VagaGaragemDTO vagaGaragemDTO) {
        LOGGER.info("Recebendo Update para vaga de ID: {}", id);
        LOGGER.debug("Payload: {}", vagaGaragemDTO);

        return this.vagaGaragemService.update(vagaGaragemDTO, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        LOGGER.info("Recebendo Delete para vaga de ID: {}", id);

        this.vagaGaragemService.delete(id);
    }

}
