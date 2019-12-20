package br.com.hbparking.file;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class FileNotSupportedException extends Exception {
    public FileNotSupportedException(String message) {
        super(message);
    }
}
