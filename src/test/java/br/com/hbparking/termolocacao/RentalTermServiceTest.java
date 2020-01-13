package br.com.hbparking.termolocacao;

import br.com.hbparking.file.FileNotSupportedException;
import br.com.hbparking.file.FileService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class RentalTermServiceTest {

    private RentalTermRepository rentalTermRepository = Mockito.mock(RentalTermRepository.class);
    private FileService fileService = Mockito.mock(FileService.class);

    private RentalTermService rentalTermService;

    @Before
    public void setUp() {
        rentalTermService = new RentalTermService(rentalTermRepository, fileService);
    }

    @Test
    public void persistRentalTermTest() throws IOException, FileNotSupportedException {
        MockMultipartFile file = new MockMultipartFile("file", "termFileTest.docx", null, "test data".getBytes());
        RentalTermDTO rentalTermDTO = new RentalTermDTO("Termo de locação HBSIS", file.getOriginalFilename(), "ATIVO");

        when(fileService.storeFile(file)).thenReturn("termFileTest.docx");
        given(rentalTermRepository.save(any(RentalTerm.class))).willReturn(new RentalTerm(1L, "Termo de locação HBSIS", "termFileTest.docx", RentalTermStatus.ATIVO));
        RentalTerm rentalTerm = this.rentalTermService.save(rentalTermDTO, file);

        assertThat(rentalTerm).isNotNull();
        assertThat(rentalTerm.getClass()).isEqualTo(RentalTerm.class);
        assertThat(rentalTerm.getTitle()).isEqualTo(rentalTermDTO.getTitle());
        assertThat(rentalTerm.getFileName()).isEqualTo(rentalTermDTO.getFileName());
        assertThat(rentalTerm.getRentalTermStatus().name()).isEqualTo(rentalTermDTO.getRentalTermStatus());
    }

    @Test
    public void findByRentalStatusAtivoTest() throws CannotFoundAnyRentalTermWithStatusAtivo {
        when(rentalTermRepository.findRentalTermByRentalTermStatusIs(RentalTermStatus.ATIVO)).thenReturn(Optional.of(new RentalTerm("Termo de Locação HBSIS", "termo-2.0.docx", RentalTermStatus.ATIVO)));

        RentalTerm lastRentalTerm = this.rentalTermService.getLastRentalTerm();

        assertThat(lastRentalTerm).isNotNull();
        assertThat(lastRentalTerm.getRentalTermStatus()).isEqualTo(RentalTermStatus.ATIVO);
    }
}
