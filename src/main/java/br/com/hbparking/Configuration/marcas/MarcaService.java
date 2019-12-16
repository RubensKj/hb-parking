package br.com.hbparking.Configuration.marcas;

import br.com.hbparking.Configuration.util.Extension;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.enums.EnumUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
        LOGGER.info("Validando Fornecedor");

        if (marcaDTO == null) {
            throw new IllegalArgumentException("MarcaDTO não deve ser nulo");
        }

        if (StringUtils.isEmpty(marcaDTO.getNome())) {
            throw new IllegalArgumentException("Nome não deve ser nulao/vazio");
        }

    }
    public MarcaDTO findById(Long id) {

        Optional<Marca> marca = this.iMarcaRepository.findById(id);

        if (marca.isPresent()) {
            return MarcaDTO.of(marca.get());
        }

        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public List<Marca> findAll() {

        List<Marca> marcas = iMarcaRepository.findAll();

        return marcas;
    }

    public MarcaDTO update(MarcaDTO marcaDTO, Long id) {
        Optional<Marca> marcaOptional = this.iMarcaRepository.findById(id);
        if (marcaOptional.isPresent()) {
            Marca marcaExistente = marcaOptional.get();
            validate(marcaDTO);
            LOGGER.info("Atualizando marca... id: [{}]", marcaExistente.getId());
            LOGGER.debug("Payload: {}", marcaDTO);
            LOGGER.debug("Marca Existente: {}", marcaExistente);
            TipoEnum.CARRO.ordinal();
            marcaExistente.setNome(marcaDTO.getNome());
            marcaExistente.setTipoVeiculo(marcaDTO.getTipoVeiculo());

            marcaExistente = this.iMarcaRepository.save(marcaExistente);

            return marcaDTO.of(marcaExistente);
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public void delete(Long id) {
        LOGGER.info("Executando delete para marca de ID: [{}]", id);

        this.iMarcaRepository.deleteById(id);
    }

    public void saveDataFromUploadFile(MultipartFile file, String tipo) throws Exception {

        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        if (Extension.CSV.getDescricao().equalsIgnoreCase(extension)) {
            readDataFromCsv(file, tipo);
        } else {
            throw new Exception("Formato de arquivo invalido");
        }

    }

    private void readDataFromCsv(MultipartFile file, String tipo) throws IOException {

        InputStreamReader reader = new InputStreamReader(file.getInputStream());
        CSVParser parser = new CSVParserBuilder().withSeparator(';').build();
        CSVReader csvReader = new CSVReaderBuilder(reader)
                .withCSVParser(parser)
                .withSkipLines(1)
                .build();

        List<String[]> linhas = csvReader.readAll();

        for (String[] linha : linhas) {

            Marca marca = new Marca(TipoEnum.valueOf(tipo), linha[1]);

            validate(MarcaDTO.of(marca));

            this.iMarcaRepository.save(marca);
        }

    }
}

