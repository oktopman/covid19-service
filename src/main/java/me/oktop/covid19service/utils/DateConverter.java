package me.oktop.covid19service.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateConverter {

    public static LocalDate toLocalDate(String stdDay) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");
        return LocalDate.parse(stdDay.substring(0, 13), formatter);
    }

    public static String toStringDate(String stdDay) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");
        return String.valueOf(LocalDate.parse(stdDay.substring(0, 13), formatter));
    }
}
