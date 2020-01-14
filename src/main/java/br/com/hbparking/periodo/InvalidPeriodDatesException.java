package br.com.hbparking.periodo;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FOUND)
public class InvalidPeriodDatesException extends Exception {
    public InvalidPeriodDatesException(String message) {
        super(message);
    }
}
