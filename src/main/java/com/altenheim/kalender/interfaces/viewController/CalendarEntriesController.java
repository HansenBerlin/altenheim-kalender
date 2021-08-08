package com.altenheim.kalender.interfaces.viewController;

import java.time.LocalDateTime;
import java.util.List;
import com.calendarfx.model.Calendar;
import com.calendarfx.model.Entry;

public interface CalendarEntriesController
{   
    void clearCalendarsSelectedByUser();
    void addEntryToCalendarWithName(String name, Entry<String> entry);
    void addToAllCalendarsSelectedByUser(Calendar calendar);
    void addToAllCalendarsSelectedByUserByCalendarName(String calendarName);
    List<Calendar> getAllCalendars();
    List<Calendar> getAllCalendarsSelectedByUser();
    List<Entry<String>> getEntrysWithStartInSpecificRange(LocalDateTime start, LocalDateTime end);
}
