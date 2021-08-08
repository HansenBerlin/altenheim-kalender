package com.altenheim.kalender.resourceClasses;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateFormatConverter 
{
    private DateFormatConverter() {}

    public static String formatDate(LocalDate date) 
    {
        return date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    public static String formatTime(LocalTime time) 
    {
        return time.format(DateTimeFormatter.ofPattern("HH:mm"));        
    }

}
