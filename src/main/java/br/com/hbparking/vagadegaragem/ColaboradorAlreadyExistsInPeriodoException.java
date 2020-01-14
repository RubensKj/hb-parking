package br.com.hbparking.vagadegaragem;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FOUND)
public class ColaboradorAlreadyExistsInPeriodoException extends Exception {
    public ColaboradorAlreadyExistsInPeriodoException(String message) {
        super(message);
    }
}
