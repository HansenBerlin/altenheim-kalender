package com.altenheim.kalender.interfaces;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public interface IDateFormatController {
    String formatDate(LocalDate date);
    String formatTime(LocalTime time);
    String formatDateTime(LocalDateTime dateTime);
}
