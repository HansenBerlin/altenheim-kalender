package com.altenheim.kalender.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import com.altenheim.kalender.interfaces.ICalendarEntriesModel;
import com.calendarfx.model.Calendar;
import com.calendarfx.model.Entry;


public class CalendarEntriesModel implements ICalendarEntriesModel
{    
    private List<Calendar> calendars;   
    
    public CalendarEntriesModel()
    {
        calendars = new ArrayList<Calendar>();
    }

    public void addCalendar(Calendar calendar)
    {
        calendars.add(calendar);
    }

    public List<Calendar> getAllCalendars()
    {
        return calendars;
    }

    public List<Entry<?>> getSpecificRange(LocalDate startDate, LocalDate endDate)
    {
        var calendar = calendars.get(0);
        var result = calendar.findEntries(startDate, endDate, ZoneId.systemDefault());
        var allEntries = result.values();
        var returnValue = new ArrayList<Entry<?>>();
        for (var entries : allEntries) 
        {
            for (var entry : entries) 
            {
                returnValue.add((Entry<?>)entry);
            }            
        }
        return returnValue;        
    }

    public Calendar getSpecificCalendarByName(String calendarName)
    {
        return new Calendar();
    }

    public Calendar getSpecificCalendarByIndex(int index)
    {
        return calendars.get(index);
    }

    public List<Entry<?>> getEntrysWithStartInSpecificRange(LocalDateTime start, LocalDateTime end) {
        var returnValue = new ArrayList<Entry<?>>();
        for (Calendar calendar : calendars) {
            var result = calendar.findEntries(start.toLocalDate(), end.toLocalDate(), ZoneId.systemDefault());
            var allEntries = result.values();
        
            for (var entries : allEntries) 
            {
                for (var entry : entries) 
                {
                    if (entry.getStartAsLocalDateTime().isAfter(start.minusSeconds(1)) && entry.getStartAsLocalDateTime().isBefore(end.plusSeconds(1))) {
                        returnValue.add((Entry<?>)entry); 
                    }
                    
                }            
            }
        }
        return returnValue; 
    }
}
