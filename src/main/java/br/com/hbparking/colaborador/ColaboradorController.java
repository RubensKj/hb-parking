package br.com.hbparking.colaborador;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/colaborador")
public class ColaboradorController {

    private final ColaboradorService colaboradorService;

    public ColaboradorController(ColaboradorService colaboradorService) {
        this.colaboradorService = colaboradorService;
    }


    @PostMapping("/cadastrar")
    public void cadastrarColaborador(@RequestBody @Valid ColaboradorDTO colaboradorDTO) throws Exception {
        this.colaboradorService.save(colaboradorDTO);
    }

    @GetMapping("/{id}")
    public ColaboradorDTO getColaborador(@PathVariable("id") Long id) {
        return this.colaboradorService.getColaborador(id);
    }

    @PutMapping("/update/{id}")
    public ColaboradorDTO updateColaborador(@RequestBody @Valid ColaboradorDTO colaboradorDTO, @PathVariable("id") int id){
        return this.colaboradorService.update(colaboradorDTO, id);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable("id") Long id) {
        this.colaboradorService.delete(id);
    }

    @PostMapping("/cadastrar/csv")
    public void cadastrarCsv(@RequestParam("file") MultipartFile multipartFile) throws Exception {
        this.colaboradorService.importColaborador(multipartFile);
    }

    @PostMapping("/configurar/parametros/locacao")
    public ColaboradorDTO configurarParametrosLocacao(@RequestBody ColaboradorDTO colaboradorDTO) {
        return this.colaboradorService.receberParametrosLocacao(colaboradorDTO);
    }
}
