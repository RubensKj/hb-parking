package br.com.hbparking.util;

import br.com.hbparking.vagadegaragem.InvalidPlatePatternException;

import java.util.regex.Pattern;

public class PlacaUtils {

    public static void validatePlate(String plate) throws InvalidPlatePatternException {
        if (!Pattern.compile("[A-Z]{3}[0-9][A-Z][0-9]{2}|[A-Z]{3}[0-9]{4}").matcher(plate).matches()) {
            throw new InvalidPlatePatternException("Placa informada não bate com os padrões atuais");
        }
    }

    public static String transformPlate(String plate) {
        return plate.toUpperCase().replace("-", "");
    }
}
