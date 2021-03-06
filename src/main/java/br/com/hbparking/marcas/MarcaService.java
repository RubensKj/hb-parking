package br.com.hbparking.marcas;

import br.com.hbparking.util.Extension;
import br.com.hbparking.vehicleException.ContentDispositionException;
import com.opencsv.*;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.EnumUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class MarcaService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MarcaService.class);
    private final IMarcaRepository iMarcaRepository;

    public MarcaService(IMarcaRepository iMarcaRepository) {
        this.iMarcaRepository = iMarcaRepository;
    }

    public MarcaDTO save(MarcaDTO marcaDTO) {
        this.validate(marcaDTO);
        LOGGER.info("Salvando Marca");
        LOGGER.debug("Marca: {}", marcaDTO);
        Marca marcaSave = new Marca(
                marcaDTO.getTipoVeiculo(),
                marcaDTO.getNome()
        );

        marcaSave = this.iMarcaRepository.save(marcaSave);
        return MarcaDTO.of(marcaSave);
    }

    private void validate(MarcaDTO marcaDTO) {
        LOGGER.info("Validando Marca");
        if (marcaDTO == null) {
            throw new IllegalArgumentException("MarcaDTO não deve ser nulo");
        }
        if (StringUtils.isEmpty(marcaDTO.getNome())) {
            throw new IllegalArgumentException("Nome não deve ser nulo/vazio");
        }
    }

    public Marca findById(Long id) {
        Optional<Marca> marca = this.iMarcaRepository.findById(id);
        if (marca.isPresent()) {
            return marca.get();
        }
        throw new IllegalArgumentException(String.format("ID %s de marca não existe", id));
    }

    @Transactional(readOnly = true)
    public void exportFromData(HttpServletResponse response, String tipo) throws IOException {
        if (EnumUtils.isValidEnum(TipoVeiculoEnum.class, tipo)) {
            final String[] headerCSV = {"ID", "NOME_MARCA"};
            String filename = "marcas.csv";
            response.setContentType("text/csv");
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"" + filename + "\"");
            PrintWriter writer1 = response.getWriter();
            ICSVWriter icsvWriter = new CSVWriterBuilder(writer1).
                    withSeparator(';').
                    withEscapeChar(CSVWriter.DEFAULT_ESCAPE_CHARACTER).
                    withLineEnd(CSVWriter.DEFAULT_LINE_END).
                    build();
            icsvWriter.writeNext(headerCSV);
            try {
                Stream<Marca> marcaRow = iMarcaRepository.streamAll(TipoVeiculoEnum.valueOf(tipo));
                marcaRow.forEach(marca -> icsvWriter.writeNext(new String[]{
                        String.valueOf(marca.getId()), marca.getNome()})
                );
            } catch (Exception e) {
                LOGGER.error("Erro: ", e);
                throw new RuntimeException("Exceção ocorrida no Stream da base ", e);
            }

        } else {
            throw new IllegalArgumentException("Tipo de veiculo inválido");
        }

    }

    public Page<Marca> findAllByTipoPage(String tipo, Pageable pageable) {
        if (EnumUtils.isValidEnum(TipoVeiculoEnum.class, tipo)) {
            LOGGER.info("Retornando marcas em paginas");
            return iMarcaRepository.findAllBytipoVeiculo(TipoVeiculoEnum.valueOf(tipo), pageable);
        } else {
            throw new IllegalArgumentException("Tipo de veiculo inválido");
        }
    }

    public MarcaDTO update(MarcaDTO marcaDTO, Long id) {
        Optional<Marca> marcaOptional = this.iMarcaRepository.findById(id);
        if (marcaOptional.isPresent()) {
            Marca marcaExistente = marcaOptional.get();
            validate(marcaDTO);
            LOGGER.info("Atualizando marca... id: [{}]", marcaExistente.getId());
            LOGGER.debug("Payload: {}", marcaDTO);
            LOGGER.debug("Marca Existente: {}", marcaExistente);
            marcaExistente.setNome(marcaDTO.getNome());
            marcaExistente.setTipoVeiculo(marcaDTO.getTipoVeiculo());
            marcaExistente = this.iMarcaRepository.save(marcaExistente);
            return MarcaDTO.of(marcaExistente);
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public void delete(Long id) {
        LOGGER.info("Executando delete para marca de ID: [{}]", id);
        this.iMarcaRepository.deleteById(id);
    }

    public void saveDataFromUploadFile(MultipartFile file, String tipo) throws Exception {
        if (EnumUtils.isValidEnum(TipoVeiculoEnum.class, tipo)) {
            String extension = FilenameUtils.getExtension(file.getOriginalFilename());
            if (Extension.CSV.getDescricao().equalsIgnoreCase(extension)) {
                readDataFromCsv(file, tipo);
            } else {
                throw new Exception("Formato de arquivo invalido");
            }
        } else {
            LOGGER.info("TIPO DE VEICULO INVÁLIDO");
            throw new IllegalArgumentException("Tipo de veiculo inválido");
        }

    }

    private void readDataFromCsv(MultipartFile file, String tipo) throws IOException, ContentDispositionException {
        InputStreamReader reader = new InputStreamReader(file.getInputStream(), StandardCharsets.ISO_8859_1);
        CSVParser parser = new CSVParserBuilder().build();
        CSVReader csvReader = new CSVReaderBuilder(reader)
                .withCSVParser(parser)
                .withSkipLines(1)
                .build();
        List<String[]> linhas = lerLinhasCsv(csvReader);
        deleteAllByTipoIsNotIn(linhas, tipo);
        saveMarcasFromCsv(linhas, tipo);

    }

    public void saveMarcasFromCsv(List<String[]> linhas, String tipo) {
        int qtdRegistros = 0;
        for (String[] linha : linhas) {
            Marca marca = new Marca(TipoVeiculoEnum.valueOf(tipo), linha[1]);
            try {
                this.iMarcaRepository.save(marca);
                qtdRegistros++;
            } catch (Exception e) {
                LOGGER.error("Erro: {}", e.toString());
            }
        }
        LOGGER.info("Quantidade de marcas novas cadastradas: {}", qtdRegistros);
    }

    public void deleteAllByTipoIsNotIn(List<String[]> nomeMarca, String tipo) {
        List<String> nomes = new ArrayList<>();
        for (String[] nome : nomeMarca) {
            nomes.add(nome[1]);
        }
        try {
            iMarcaRepository.deleteAllByNomeIsNotInByTipo(nomes, TipoVeiculoEnum.valueOf(tipo));
        } catch (Exception e) {
            LOGGER.error("Erro ao deletar marcas");
            LOGGER.error(e.toString());
        }
    }

    public Marca findEntityById(Long id) throws CannotFindAnyMarcaWithId {
        return this.iMarcaRepository.findById(id).orElseThrow(() -> new CannotFindAnyMarcaWithId("Não foi possivel encontrar nenhuma marca com esse id. [" + id + "]"));
    }

    public List<String[]> lerLinhasCsv(CSVReader csvReader) throws ContentDispositionException, IOException {
        List<String[]> linhas = new ArrayList<>();
        String[] linha;
        while ((linha = csvReader.readNext()) != null) {
            if (linha[0].contains(";")) {
                linha = linha[0].split(";");
                if (linha.length > 0) {
                    linhas.add(linha);
                }
            } else {
                throw new ContentDispositionException("Separador do arquivo CSV Inválido, separador esperado:(;)");
            }
        }
        return linhas;

    }

    public List<Marca> findAllByTipoVeiculo(String tipo){
        return iMarcaRepository.findAllByTipoVeiculo(TipoVeiculoEnum.valueOf(tipo));
    }


}