package br.com.hbparking.util;

import br.com.hbparking.file.FileNotSupportedException;
import br.com.hbparking.vehicleException.ContentDispositionException;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Component
public class ReadFileCSV {
    public List<String[]> read(MultipartFile multipartFile) throws IOException, FileNotSupportedException, ContentDispositionException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(multipartFile.getInputStream()));
        String line;

        if (!FilenameUtils.getExtension(multipartFile.getOriginalFilename()).equalsIgnoreCase("csv")) {
            throw new FileNotSupportedException("O tipo de arquivo enviado não é suportado pela API, favor enviar um arquivo CSV.");
        }

        if (multipartFile.isEmpty()) {
            throw new IllegalArgumentException("O arquivo enviado está vazio");
        }

        List<String[]> dataArray = new ArrayList<>();
        bufferedReader.readLine();

        while ((line = bufferedReader.readLine()) != null) {

            if (!line.contains(";")) {
                throw new ContentDispositionException("O separdaor do arquivo é invalido, verifique se o arquivo é separado por \";\"");
            }
            dataArray.add(line.split(";"));
        }

        bufferedReader.close();

        if (dataArray.isEmpty()) {
            throw new ContentDispositionException("O conteudo possuido pelo arquivo não atende o padrão esperado.");
        }

        return dataArray;
    }
}
