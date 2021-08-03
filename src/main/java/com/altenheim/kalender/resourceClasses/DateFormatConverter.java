package com.altenheim.kalender.resourceClasses;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class DateFormatConverter {
    public static String formatDate(LocalDate date) {
        var dateFormat = new SimpleDateFormat("dd.mm.yyyy");
        dateFormat.format(date);
        return dateFormat.toString();
    }

    public static String formatTime(LocalTime time) {
        var timeFormat = new SimpleDateFormat("hh.mm.ss");
        timeFormat.format(time);
        return timeFormat.toString();        
    }

    public static String formatDateTime(LocalDateTime dateTime) {
        var dateTimeFormat = new SimpleDateFormat("hh.mm.ss dd.mm.yyyy");
        dateTimeFormat.format(dateTime);
        return dateTimeFormat.toString();
    }
}
