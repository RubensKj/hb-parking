package br.com.hbparking.termoLocacao;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CannotFoundAnyRentalTermWithStatusAtivo extends Exception {
    public CannotFoundAnyRentalTermWithStatusAtivo(String message) {
        super(message);
    }
}
