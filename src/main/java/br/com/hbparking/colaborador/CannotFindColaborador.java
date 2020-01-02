package br.com.hbparking.colaborador;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CannotFindColaborador extends  Exception {
    public CannotFindColaborador(String message) {
        super(message);
    }
}
