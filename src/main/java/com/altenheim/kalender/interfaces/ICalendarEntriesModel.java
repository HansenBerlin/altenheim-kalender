package com.altenheim.kalender.interfaces;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import com.altenheim.kalender.models.SerializableEntry;
import com.calendarfx.model.Calendar;

public interface ICalendarEntriesModel {
    Calendar getSpecificCalendarByIndex(int index);

    void addCalendar(Calendar calendar);

    List<SerializableEntry> getSpecificRange(LocalDate startDate, LocalDate endDate);

    List<Calendar> getAllCalendars();

    List<SerializableEntry> getEntrysWithStartInSpecificRange(LocalDateTime start, LocalDateTime end);
}
