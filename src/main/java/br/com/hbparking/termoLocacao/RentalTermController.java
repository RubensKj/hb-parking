package br.com.hbparking.termoLocacao;

import br.com.hbparking.file.FileNotSupportedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@CrossOrigin(origins = {"http://localhost:4200", "http://192.168.33.216:4200", "https://192.168.33.216:4200"})
@RestController
@RequestMapping("/api")
public class RentalTermController {

    private final RentalTermService rentalTermService;

    public RentalTermController(RentalTermService rentalTermService) {
        this.rentalTermService = rentalTermService;
    }

    @PostMapping("/termo/create")
    public RentalTermDTO saveTerm(@RequestParam("file") MultipartFile file, @RequestParam("title") String title) throws IOException, FileNotSupportedException {
        return RentalTermDTO.of(rentalTermService.setNewTerm(new RentalTermDTO(title, file.getOriginalFilename(), RentalTermStatus.ATIVO.name()), file));
    }
}
