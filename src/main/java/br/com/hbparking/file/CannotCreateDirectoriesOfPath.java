package br.com.hbparking.file;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class CannotCreateDirectoriesOfPath extends RuntimeException {
    public CannotCreateDirectoriesOfPath(String message, Throwable cause) {
        super(message, cause);
    }
}
