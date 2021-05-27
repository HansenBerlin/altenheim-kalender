package com.altenheim.calendar.models;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import com.altenheim.calendar.interfaces.*;
import com.calendarfx.model.Calendar;
import com.calendarfx.model.Entry;


public class CalendarEntriesModel implements ICalendarEntriesModel
{    
    private List<Calendar> calendars;    

    public void addCalendar(Calendar calendar)
    {
        calendars.add(calendar);
    }

    public List<Entry<String>> getSpecificDay(LocalDate startDate, LocalDate endDate)
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

    public void saveDate(ICalendarEntryModel newEntry)
    {
       // nach Schema der Logik in der Factory implementieren  
    }
}
