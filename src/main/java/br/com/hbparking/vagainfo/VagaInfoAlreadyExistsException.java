package br.com.hbparking.vagainfo;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FOUND)
class VagaInfoAlreadyExistsException extends Exception {
    VagaInfoAlreadyExistsException(String message) {
        super(message);
    }
}
