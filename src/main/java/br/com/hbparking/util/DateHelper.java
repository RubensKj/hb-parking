package br.com.hbparking.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateHelper {

    public static String formatDate(LocalDate date) {
        return DateTimeFormatter.ofPattern("dd/MM/yyyy").format(date);
    }

}
