package br.com.hbparking.vagaInfo;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class VagaInfoNotFoundException extends Exception {
    public VagaInfoNotFoundException(String message) {
        super(message);
    }
}
