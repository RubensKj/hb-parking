package br.com.hbparking.vagainfo;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FOUND)
public class PeriodoAlreadyExistsException extends Exception {
    public PeriodoAlreadyExistsException(String message) {
        super(message);
    }
}