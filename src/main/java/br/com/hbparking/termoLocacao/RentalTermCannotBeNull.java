package br.com.hbparking.termoLocacao;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RentalTermCannotBeNull extends RuntimeException {
    public RentalTermCannotBeNull(String message) {
        super(message);
    }
}
