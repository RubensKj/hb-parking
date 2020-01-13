package br.com.hbparking.vagadegaragem;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidPlatePatternException extends Exception {
    public InvalidPlatePatternException(String message) {
        super(message);
    }
}
