package com.altenheim.kalender.models;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import com.altenheim.kalender.interfaces.ICalendarEntriesModel;
import com.altenheim.kalender.controller.logicController.CalendarSer;


public class CalendarEntriesModel implements ICalendarEntriesModel
{    
    private List<CalendarSer> calendars;   
    
    public CalendarEntriesModel()
    {
        calendars = new ArrayList<CalendarSer>();
    }

    public void addCalendar(CalendarSer calendar)
    {
        calendars.add(calendar);
    }

    public List<CalendarSer> getAllCalendars()
    {
        return calendars;
    }

    public List<EntrySer> getSpecificRange(LocalDate startDate, LocalDate endDate)
    {
        var calendar = calendars.get(0);
        var result = calendar.findEntries(startDate, endDate, ZoneId.systemDefault());
        var allEntries = result.values();
        var returnValue = new ArrayList<EntrySer>();
        for (var entries : allEntries) 
        {
            for (var entry : entries) 
            {
                returnValue.add((EntrySer)entry);
            }            
        }
        return returnValue;        
    }

    public CalendarSer getSpecificCalendarByName(String calendarName)
    {
        return new CalendarSer();
    }

    public CalendarSer getSpecificCalendarByIndex(int index)
    {
        return calendars.get(index);
    }
}
