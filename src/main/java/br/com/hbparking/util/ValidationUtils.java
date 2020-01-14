package br.com.hbparking.util;

public interface ValidationUtils {

    static void validateNotNull(Object object, String errorMessage) {
        if (object == null) {
            throw new IllegalArgumentException(errorMessage);
        }
    }
}
