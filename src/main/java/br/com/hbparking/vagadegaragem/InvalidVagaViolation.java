package br.com.hbparking.vagadegaragem;

public class InvalidVagaViolation extends Exception {
    public InvalidVagaViolation(String message, Exception ex) {
        super(message);
    }
}
