package com.altenheim.kalender.models;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.spi.CalendarDataProvider;

import com.altenheim.kalender.interfaces.*;
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

    public List<Entry<String>> getSpecificRange(LocalDate startDate, LocalDate endDate)
    {
        var calendar = calendars.get(0);
        var result = calendar.findEntries(startDate, endDate, ZoneId.systemDefault());
        var allEntries = result.values();
        var returnValue = new ArrayList<Entry<String>>();
        for (var entries : allEntries) 
        {
            for (var entry : entries) 
            {
                returnValue.add((Entry<String>)entry);
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
    
}
