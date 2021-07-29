package com.altenheim.kalender.interfaces;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import com.calendarfx.model.Calendar;
import com.calendarfx.model.Entry;

public interface ICalendarEntriesModel 
{
   /* Calendar getSpecificCalendarByIndex(int index);
    void clearCalendarsSelectedByUser();
    void addToAllCalendarsSelectedByUser(Calendar calendar);
    void addToAllCalendarsSelectedByUserByCalendarName(String calendarName);
    List<Entry<String>> getSpecificRange(LocalDate startDate, LocalDate endDate);*/
    List<Calendar> getAllCalendarsSelectedByUser();
    List<Entry<String>> getEntrysWithStartInSpecificRange(LocalDateTime start, LocalDateTime end);
    void addCalendar(Calendar calendar);
    List<Calendar> getAllCalendars();
}
