package com.statistics.corona.service;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Component
public class DateFormat {

    private static final String DATE_PATTERN = "dd.MM.yyy";

    public String formatLastUpdateToDate(String lastUpdate) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.GERMAN);
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern(DATE_PATTERN, Locale.GERMAN);
        LocalDate date = LocalDate.parse(lastUpdate, inputFormatter);
        return outputFormatter.format(date);
    }

    public String formatLastUpdateToTime(String lastUpdate) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.GERMAN);
        DateTimeFormatter outputFormatterTime = DateTimeFormatter.ofPattern("HH:mm", Locale.GERMAN);
        LocalTime time = LocalTime.parse(lastUpdate, inputFormatter);
        return outputFormatterTime.format(time);
    }

    public String formatLastUpdateToDateDaily(String lastUpdate) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.GERMAN);
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern(DATE_PATTERN, Locale.GERMAN);
        LocalDate date = LocalDate.parse(lastUpdate, inputFormatter);
        return outputFormatter.format(date);
    }

    public String formatLastUpdateToTimeDaily(String lastUpdate) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.GERMAN);
        DateTimeFormatter outputFormatterTime = DateTimeFormatter.ofPattern("HH:mm", Locale.GERMAN);
        LocalTime time = LocalTime.parse(lastUpdate, inputFormatter);
        return outputFormatterTime.format(time);
    }

    public String formatUnixToDate(long unixTime) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd.MM.yyy HH:mm", Locale.GERMAN);
        return Instant.ofEpochMilli(unixTime)
                .atZone(ZoneId.of("GMT+1"))
                .format(inputFormatter);
    }
}