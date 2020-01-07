package br.com.hbparking.colaborador;

import br.com.hbparking.file.FileNotSupportedException;
import br.com.hbparking.security.jwt.JwtProvider;
import br.com.hbparking.vehicleException.ContentDispositionException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/colaborador")
public class ColaboradorController {

    private final ColaboradorService colaboradorService;
    private final JwtProvider jwtProvider;

    public ColaboradorController(ColaboradorService colaboradorService, JwtProvider jwtProvider) {
        this.colaboradorService = colaboradorService;
        this.jwtProvider = jwtProvider;
    }


    @PostMapping("/cadastrar")
    public ColaboradorDTO cadastrarColaborador(@RequestBody @Valid ColaboradorDTO colaboradorDTO) throws EmailAlreadyExistsException {
        return this.colaboradorService.save(colaboradorDTO);
    }

    @GetMapping("/{id}")
    public ColaboradorDTO getColaborador(@PathVariable("id") Long id) {
        return this.colaboradorService.getColaborador(id);
    }

    @GetMapping
    public ColaboradorDTO getColaboradorByUserLogged(HttpServletRequest request) throws ColaboradorNotFoundException {
        String jwt = this.jwtProvider.getJwt(request);
        String emailFromUserAuthenticated = this.jwtProvider.getEmailFromUserAuthenticated(jwt);
        return ColaboradorDTO.of(this.colaboradorService.findByEmail(emailFromUserAuthenticated));
    }

    @PutMapping("/update/{id}")
    public ColaboradorDTO updateColaborador(@RequestBody @Valid ColaboradorDTO colaboradorDTO, @PathVariable("id") Long id) {
        return this.colaboradorService.update(colaboradorDTO, id);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable("id") Long id) {
        this.colaboradorService.delete(id);
    }

    @PostMapping("/cadastrar/csv")
    public void cadastrarCsv(@RequestParam("file") MultipartFile multipartFile) throws FileNotSupportedException, IOException, ContentDispositionException {
        this.colaboradorService.importColaborador(multipartFile);
    }
}
