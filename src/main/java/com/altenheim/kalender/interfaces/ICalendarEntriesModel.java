package com.altenheim.kalender.interfaces;

import java.time.LocalDateTime;
import java.util.List;
import com.calendarfx.model.Calendar;
import com.calendarfx.model.Entry;

public interface ICalendarEntriesModel 
{
   /* Calendar getSpecificCalendarByIndex(int index);
   void addToAllCalendarsSelectedByUser(Calendar calendar);
   List<Entry<String>> getSpecificRange(LocalDate startDate, LocalDate endDate);*/
    void clearCalendarsSelectedByUser();
    void addEntryToCalendarWithName(String name, Entry<String> entry);
    void addCalendar(Calendar calendar);
    void addToAllCalendarsSelectedByUser(Calendar calendar);
    void addToAllCalendarsSelectedByUserByCalendarName(String calendarName);
    List<Calendar> getAllCalendars();
    List<Calendar> getAllCalendarsSelectedByUser();
    List<Entry<String>> getEntrysWithStartInSpecificRange(LocalDateTime start, LocalDateTime end);
}
