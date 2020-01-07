package br.com.hbparking.termoLocacao;

import br.com.hbparking.file.FileNotSupportedException;
import br.com.hbparking.file.FileService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

@RestController
@RequestMapping("/api")
public class RentalTermController {

    private final RentalTermService rentalTermService;
    private final FileService fileService;

    public RentalTermController(RentalTermService rentalTermService, FileService fileService) {
        this.rentalTermService = rentalTermService;
        this.fileService = fileService;
    }

    @PostMapping("/termo/create")
    public RentalTermDTO saveTerm(@RequestParam("file") MultipartFile file, @RequestParam("title") String title) throws IOException, FileNotSupportedException {
        return RentalTermDTO.of(rentalTermService.setNewTerm(new RentalTermDTO(title, file.getOriginalFilename(), RentalTermStatus.ATIVO.name()), file));
    }

    @GetMapping("/termo")
    public RentalTermToHTML getRentalTermAndConvertToHtml() throws CannotFoundAnyRentalTermWithStatusAtivo, IOException {
        RentalTerm rentalTerm = rentalTermService.getLastRentalTerm();
        return RentalTermToHTML.of(rentalTerm, fileService.loadFileAsResourceByFileName(rentalTerm.getFileName()).getFile());
    }
}
