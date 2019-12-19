package br.com.hbparking.file;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Service
public class FileService {

    private final Path path;
    private final FileProperties fileProperties;

    public FileService(FileProperties fileProperties) {
        this.fileProperties = fileProperties;
        this.path = Paths.get(fileProperties.getUploadDir()).toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.path);
        } catch (Exception e) {
            throw new CannotCreateDirectoriesOfPath("Não foi possivel criar um diretório para os arquivos", e);
        }
    }

    public String storeFile(MultipartFile file) throws IOException, FileNotSupportedException {
        this.validateFile(file);
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        Path targetLocation = this.path.resolve(fileName);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        return fileName;
    }

    public Resource loadFileAsResourceByFileName(String nameFile) throws MalformedURLException, FileNotFoundException {
        Path filePath = this.path.resolve(nameFile).normalize();
        Resource resource = new UrlResource(filePath.toUri());
        return this.validateIfResoucesExists(resource);
    }

    private Resource validateIfResoucesExists(Resource resource) throws FileNotFoundException {
        if (resource.exists()) {
            return resource;
        }
        throw new FileNotFoundException("Arquivo não foi encontrado");
    }

    public void validateFile(MultipartFile file) throws FileNotFoundException, FileNotSupportedException {
        if (file.isEmpty()) {
            throw new FileNotFoundException("Arquivo não encontrado.");
        }

        if (file.getOriginalFilename() == null || file.getOriginalFilename().equals("")) {
            throw new FileNotSupportedException("Formato do arquivo não suportado.");
        }
    }

}
