package br.com.hbparking.marcas;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

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

    @PostMapping(value = "/fileupload")
    public void uploadFile(@RequestParam MultipartFile marcasCSV, @RequestParam String tipo) throws Exception {
        try {
            marcaService.saveDataFromUploadFile(marcasCSV, tipo);
        } catch (Exception e) {
            String erroAoImportarCSV = "Erro ao importar CSV";
            LOGGER.error(erroAoImportarCSV, e);
            throw new Exception(erroAoImportarCSV, e.getCause());
        }

        LOGGER.info("Successmessage", "File Upload Successfully!");
    }

    @GetMapping("/export-marcas/{tipo}")
    public void exportCSV(HttpServletResponse response,@PathVariable("tipo") String tipo) throws Exception {
        LOGGER.info("Exportando marcas {}");
        marcaService.exportFromData(response, tipo);
    }

    @GetMapping("/{id}")
    public MarcaDTO find(@PathVariable("id") Long id) {

        LOGGER.info("Recebendo find by ID... id: [{}]", id);

        return this.marcaService.findById(id);
    }

    @RequestMapping("/all")
    public List<Marca> findMarcas() {

        List<Marca> marcas = marcaService.findAll();
        return marcas;
    }

    @RequestMapping("/allByTipo/{tipo}")
    public List<Marca> findMarcasByTipo(@PathVariable("tipo") String tipo) {

            List<Marca> marcas = marcaService.findByTipo(tipo);
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
