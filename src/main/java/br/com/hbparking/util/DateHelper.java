package br.com.hbparking.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateHelper {

    private DateHelper() {
        throw new IllegalStateException("Utility class");
    }

    public static String formatDate(LocalDate date) {
        return DateTimeFormatter.ofPattern("dd/MM/yyyy").format(date);
    }

    public static String formatDateToPassword(LocalDate birthDate) {
        return DateTimeFormatter.ofPattern("dd/MM/yyyy").format(birthDate).replace("/", "");
    }

    public static LocalDate formatDateFromCSVToLocalDate(String[] dateSplitted) {
        int day = Integer.parseInt(dateSplitted[0]);
        int month = Integer.parseInt(dateSplitted[1]);
        int year = Integer.parseInt(dateSplitted[2]);
        return LocalDate.of(year, month, day);
    }

    public static void validateDateIsAfter(LocalDate initialDate, LocalDate finalDate) {
        if (initialDate.isAfter(finalDate)) {
            throw new IllegalArgumentException("Data inicial não pode ser igual a final");
        }
    }

    public static void validateDateIsEqual(LocalDate initialDate, LocalDate finalDate) {
        if (initialDate.isEqual(finalDate)) {
            throw new IllegalArgumentException("Datas não podem ser iguais");
        }
    }
}
