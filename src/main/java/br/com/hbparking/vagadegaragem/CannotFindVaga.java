package br.com.hbparking.vagadegaragem;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CannotFindVaga extends Exception{
    public CannotFindVaga(String message) {
        super(message);
    }
}
