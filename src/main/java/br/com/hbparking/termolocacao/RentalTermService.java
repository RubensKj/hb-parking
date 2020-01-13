package br.com.hbparking.termolocacao;

import br.com.hbparking.file.FileNotSupportedException;
import br.com.hbparking.file.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class RentalTermService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RentalTermService.class);

    private final RentalTermRepository rentalTermRepository;
    private final FileService fileService;

    @Autowired
    public RentalTermService(RentalTermRepository rentalTermRepository, FileService fileService) {
        this.rentalTermRepository = rentalTermRepository;
        this.fileService = fileService;
    }

    public RentalTerm setNewTerm(RentalTermDTO rentalTermDTO, MultipartFile file) throws IOException, FileNotSupportedException {
        this.validate(rentalTermDTO);

        LOGGER.info("Setando novo termo");

        this.disactiveLastRentalTerm();
        return this.save(rentalTermDTO, file);
    }

    public RentalTerm save(RentalTermDTO rentalTermDTO, MultipartFile file) throws IOException, FileNotSupportedException {
        this.validate(rentalTermDTO);

        LOGGER.info("Salvando arquivo de termo.");
        LOGGER.debug("Arquivo termo [{}]", file);
        String fileName = this.fileService.storeFile(file);

        LOGGER.info("Salvando termo no banco.");
        return this.rentalTermRepository.save(new RentalTerm(rentalTermDTO.getTitle(), fileName, RentalTermStatus.valueOf(rentalTermDTO.getRentalTermStatus().toUpperCase())));
    }

    public void disactiveLastRentalTerm() {
        try {
            RentalTerm lastRentalTerm = this.getLastRentalTerm();

            LOGGER.info("Inativando o último termo.");
            LOGGER.debug("Último Termo de Locação [{}]", lastRentalTerm);

            lastRentalTerm.setRentalTermStatus(RentalTermStatus.INATIVO);

            this.rentalTermRepository.save(lastRentalTerm);
        } catch (CannotFoundAnyRentalTermWithStatusAtivo ex) {
            LOGGER.info("Não existe nenhum termo ativo.");
            LOGGER.debug("Error -> []", ex);
        }
    }

    public RentalTerm getLastRentalTerm() throws CannotFoundAnyRentalTermWithStatusAtivo {
        return this.rentalTermRepository.findRentalTermByRentalTermStatusIs(RentalTermStatus.ATIVO).orElseThrow(() -> new CannotFoundAnyRentalTermWithStatusAtivo("Não foi possivel encontrar nenhum termo de locação em ativo"));
    }

    private void validate(RentalTermDTO rentalTermDTO) {
        LOGGER.info("Validando Termo de Locação");

        if (rentalTermDTO == null) {
            throw new RentalTermCannotBeNull("Termo de locação não pode ser nulo/vázio");
        }

        if (StringUtils.isEmpty(rentalTermDTO.getTitle())) {
            throw new IllegalArgumentException("Titulo do termo não deve ser nulo/vázio");
        }

        if (StringUtils.isEmpty(rentalTermDTO.getRentalTermStatus())) {
            throw new IllegalArgumentException("Status não deve ser nulo/vázio");
        }

        if (existsByFileName(rentalTermDTO.getFileName())) {
            throw new IllegalArgumentException("Nome de arquivo não pode ser igual.");
        }
    }

    private boolean existsByFileName(String fileName) {
        return this.rentalTermRepository.existsByFileName(fileName);
    }
}
