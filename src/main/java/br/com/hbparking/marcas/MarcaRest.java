package br.com.hbparking.marcas;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@CrossOrigin(origins = {"http://localhost:4200", "http://192.168.33.216:4200", "https://192.168.33.216:4200"})
@RestController
@RequestMapping("api/marcas")
public class MarcaRest {

    private static final Logger LOGGER = LoggerFactory.getLogger(Marca.class);

    private final MarcaService marcaService;

    @Autowired
    public MarcaRest(MarcaService marcaService) {
        this.marcaService = marcaService;
    }

    @PostMapping
    public MarcaDTO save(@RequestBody MarcaDTO marcaDTO) {
        LOGGER.info("Recebendo solicitação de persistência de Marca...");
        LOGGER.debug("Payaload: {}", marcaDTO);
        return this.marcaService.save(marcaDTO);
    }

    @PostMapping(value = "/fileupload/{tipo}")
    public void uploadFile(@RequestParam("file") MultipartFile marcasCSV, @PathVariable("tipo") String tipo) throws Exception {

        marcaService.saveDataFromUploadFile(marcasCSV, tipo);


        LOGGER.info("Successmessage", "File Upload Successfully!");
    }

    @GetMapping("/export-marcas/{tipo}")
    public void exportCSV(HttpServletResponse response, @PathVariable("tipo") String tipo) throws Exception {
        LOGGER.info("Exportando marcas {}");
        marcaService.exportFromData(response, tipo);
    }

    @GetMapping("/{id}")
    public MarcaDTO find(@PathVariable("id") Long id) {

        LOGGER.info("Recebendo find by ID... id: [{}]", id);

        return this.marcaService.findById(id);
    }

    @RequestMapping("/allByTipo/{tipo}")
    public Page<Marca> findMarcasByTipo(@PathVariable("tipo") String tipo) {

        Page<Marca> marcas = marcaService.findAllByTipo(tipo,0 ,5);
        return marcas;

    }

    @PutMapping("/{id}")
    public MarcaDTO udpate(@PathVariable("id") Long id, @RequestBody MarcaDTO marcaDTO) {
        LOGGER.info("Recebendo Update para marca de ID: {}", id);
        LOGGER.debug("Payload: {}", marcaDTO);

        return this.marcaService.update(marcaDTO, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        LOGGER.info("Recebendo Delete para marca de ID: {}", id);

        this.marcaService.delete(id);
    }

}
