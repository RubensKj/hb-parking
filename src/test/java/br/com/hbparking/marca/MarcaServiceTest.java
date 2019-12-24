package br.com.hbparking.marca;

import br.com.hbparking.marcas.*;
import com.opencsv.CSVReader;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = MarcaServiceTest.class)
public class MarcaServiceTest {


    @Mock
    private IMarcaRepository iMarcaRepository;

    @Captor
    private ArgumentCaptor<Marca> argumentCaptor;

    @InjectMocks
    private MarcaService marcaService;


    @Test
    public void save() {
        MarcaDTO marcaDTO = new MarcaDTO(TipoVeiculoEnum.CARRO, "meu.nome");

        Marca marcaMock = Mockito.mock(Marca.class);

        when(marcaMock.getNome()).thenReturn(marcaDTO.getNome());
        when(marcaMock.getTipoVeiculo()).thenReturn(marcaDTO.getTipoVeiculo());

        when(this.iMarcaRepository.save(any())).thenReturn(marcaMock);

        this.marcaService.save(marcaDTO);

        verify(this.iMarcaRepository, times(1)).save(this.argumentCaptor.capture());
        Marca createdMarca = argumentCaptor.getValue();

        assertTrue(StringUtils.isNoneEmpty(createdMarca.getNome()), "Nome não deve ser nulo");
        assertTrue(StringUtils.isNoneEmpty(createdMarca.getTipoVeiculo().getDescricao()), "Tipo não deve ser nulo");
        assertTrue(EnumUtils.isValidEnum(TipoVeiculoEnum.class, createdMarca.getTipoVeiculo().getDescricao()), "Enum Inválido");
    }

    @Test
    public void marcaDTONull() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.marcaService.save(null);
        });
    }

    @Test
    public void isValidEnum() {
        assertTrue(EnumUtils.isValidEnum(TipoVeiculoEnum.class, "CARRO"));
        assertTrue(EnumUtils.isValidEnum(TipoVeiculoEnum.class, "MOTO"));
        assertFalse(EnumUtils.isValidEnum(TipoVeiculoEnum.class, "GREEN"));
        assertFalse(EnumUtils.isValidEnum(TipoVeiculoEnum.class, "PURPLE"));
        assertFalse(EnumUtils.isValidEnum(TipoVeiculoEnum.class, null));
    }

    @Test
    public void marcaComNomeVazio() {
        assertThrows(IllegalArgumentException.class, () -> {
            MarcaDTO usuarioDTO = new MarcaDTO(TipoVeiculoEnum.valueOf("MOTO"), "");
            this.marcaService.save(usuarioDTO);
        });
    }

    @Test
    public void readFromFileCARRO() {
        MarcaDTO marcaDTO = new MarcaDTO(TipoVeiculoEnum.valueOf("CARRO"), "meu.nome");
        List<String[]> linhas = new ArrayList<>();
        String[] header = {"ID", "NOME"};
        String[] linha1 = {"ID", "NOME1"};
        String[] linha2 = {"ID", "NOME2"};
        String[] linha3 = {"ID", "NOME3"};
        linhas.add(header);
        linhas.add(linha1);
        linhas.add(linha2);
        linhas.add(linha3);


        Marca marcaMock = Mockito.mock(Marca.class);

        when(marcaMock.getNome()).thenReturn(marcaDTO.getNome());
        when(marcaMock.getTipoVeiculo()).thenReturn(marcaDTO.getTipoVeiculo());

        when(this.iMarcaRepository.save(any())).thenReturn(marcaMock);

        this.marcaService.saveMarcasFromCsv(linhas, "CARRO");

        verify(this.iMarcaRepository, times(4)).save(this.argumentCaptor.capture());
        Marca createdMarca = argumentCaptor.getValue();

        assertTrue(StringUtils.isNoneEmpty(createdMarca.getNome()), "Nome não deve ser nulo");
        assertTrue(StringUtils.isNoneEmpty(createdMarca.getTipoVeiculo().getDescricao()), "Tipo não deve ser nulo");
        assertTrue(EnumUtils.isValidEnum(TipoVeiculoEnum.class, createdMarca.getTipoVeiculo().getDescricao()), "Enum Inválido");
    }

    @Test
    public void readFromFileMOTO() {
        MarcaDTO marcaDTO = new MarcaDTO(TipoVeiculoEnum.valueOf("MOTO"), "meu.nome");
        List<String[]> linhas = new ArrayList<>();
        String[] header = {"ID", "NOME"};
        String[] linha1 = {"ID", "NOME1"};
        String[] linha2 = {"ID", "NOME2"};
        String[] linha3 = {"ID", "NOME3"};
        linhas.add(header);
        linhas.add(linha1);
        linhas.add(linha2);
        linhas.add(linha3);


        Marca marcaMock = Mockito.mock(Marca.class);

        when(marcaMock.getNome()).thenReturn(marcaDTO.getNome());
        when(marcaMock.getTipoVeiculo()).thenReturn(marcaDTO.getTipoVeiculo());

        when(this.iMarcaRepository.save(any())).thenReturn(marcaMock);

        this.marcaService.saveMarcasFromCsv(linhas, "MOTO");

        verify(this.iMarcaRepository, times(4)).save(this.argumentCaptor.capture());
        Marca createdMarca = argumentCaptor.getValue();

        assertTrue(StringUtils.isNoneEmpty(createdMarca.getNome()), "Nome não deve ser nulo");
        assertTrue(StringUtils.isNoneEmpty(createdMarca.getTipoVeiculo().getDescricao()), "Tipo não deve ser nulo");
        assertTrue(EnumUtils.isValidEnum(TipoVeiculoEnum.class, createdMarca.getTipoVeiculo().getDescricao()), "Enum Inválido");
    }



    @Test
    public void readInvalidCSV1() {

        StringBuffer sb = new StringBuffer();

        sb.append("a\tc");

        CSVReader c = new CSVReader(new StringReader(sb.toString()));
        assertThrows(IllegalArgumentException.class, () -> {
             marcaService.lerLinhasCsv(c);
        });

        }

    @Test
    public void readInvalidCSV2() {

        StringBuffer sb = new StringBuffer();

        sb.append("a,c/34/dr");

        CSVReader c = new CSVReader(new StringReader(sb.toString()));
        assertThrows(IllegalArgumentException.class, () -> {
            marcaService.lerLinhasCsv(c);
        });

    }
    @Test
    public void readValidCSV() throws IOException {

        StringBuffer sb = new StringBuffer();

        sb.append("1;Ford");

        CSVReader c = new CSVReader(new StringReader(sb.toString()));
           assertTrue( !marcaService.lerLinhasCsv(c).isEmpty());

    }


}
