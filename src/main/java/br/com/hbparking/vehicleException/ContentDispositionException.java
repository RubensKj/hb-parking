package br.com.hbparking.vehicleException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ContentDispositionException extends Exception {
    public ContentDispositionException(String message) {
        super(message);
    }
}
