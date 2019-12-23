package br.com.hbparking.colaborador;

import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/colaborador")
public class ColaboradorController {

    private final ColaboradorService colaboradorService;

    public ColaboradorController(ColaboradorService colaboradorService) {
        this.colaboradorService = colaboradorService;
    }


    @PostMapping("/cadastrar")
    public void cadastrarColaborador(@RequestBody @Valid ColaboradorDTO colaboradorDTO){
        this.colaboradorService.save(colaboradorDTO);
    }

    @GetMapping("/{id}")
    public ColaboradorDTO getColaborador(@PathVariable("id") int id){
        return this.colaboradorService.getColaborador(id);
    }

    @PutMapping("/update/{id}")
    public ColaboradorDTO updateColaborador(@RequestBody @Valid ColaboradorDTO colaboradorDTO, @PathVariable("id") int id){
        return this.colaboradorService.update(colaboradorDTO, id);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable("id") int id){
        this.colaboradorService.delete(id);
    }
}
