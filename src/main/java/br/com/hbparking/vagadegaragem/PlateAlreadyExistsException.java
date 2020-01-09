package br.com.hbparking.vagadegaragem;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FOUND)
public class PlateAlreadyExistsException extends Exception {
    public PlateAlreadyExistsException(String message) {
        super(message);
    }
}
