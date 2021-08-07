package com.altenheim.kalender.interfaces.models;

import java.time.LocalDateTime;
import java.util.List;
import com.calendarfx.model.Calendar;
import com.calendarfx.model.Entry;

public interface CalendarEntriesModel
{   
    void clearCalendarsSelectedByUser();
    void addEntryToCalendarWithName(String name, Entry<String> entry);
    void addToAllCalendarsSelectedByUser(Calendar calendar);
    void addToAllCalendarsSelectedByUserByCalendarName(String calendarName);
    String[] getAllCalendarNames();
    List<Calendar> getAllCalendars();
    List<Calendar> getAllCalendarsSelectedByUser();
    List<Entry<String>> getEntrysWithStartInSpecificRange(LocalDateTime start, LocalDateTime end);
}