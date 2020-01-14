package br.com.hbparking.periodo;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PeriodosNotFoundException extends Exception {
    public PeriodosNotFoundException(String message) {
        super(message);
    }
}
