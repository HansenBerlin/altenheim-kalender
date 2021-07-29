package com.altenheim.kalender.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import com.altenheim.kalender.controller.viewController.CustomViewOverride;
import com.altenheim.kalender.interfaces.ICalendarEntriesModel;
import com.calendarfx.model.*;


public class CalendarEntriesModel implements ICalendarEntriesModel 
{
    //private List<Calendar> calendars;
    private List<Calendar> calendarsSelectedByUser;
    private CustomViewOverride calendarView;

    public CalendarEntriesModel(CustomViewOverride calendarView) 
    {
        //calendars = new ArrayList<Calendar>();
        calendarsSelectedByUser = new ArrayList<Calendar>();
        this.calendarView = calendarView;
    }

    public List<Calendar> getAllCalendars() 
    { 
        calendarView.setShowSourceTray(false);
        var allCalendars = new ArrayList<Calendar>();
        for (var source : calendarView.getCalendarSources()) 
        {
            for (var calendar : source.getCalendars()) 
            {
                allCalendars.add(calendar);                
            }            
        }

        return allCalendars; 
    }

    public void addCalendar(Calendar calendar) 
    {        
        //calendars.add(calendar);
    }
    
    public List<Calendar> getAllCalendarsSelectedByUser() { return calendarsSelectedByUser; }
    
       /*


    public void addToAllCalendarsSelectedByUser(Calendar calendar)
    {
        calendarsSelectedByUser.add(calendar);
    }

    public void clearCalendarsSelectedByUser()
    {
        calendarsSelectedByUser.clear();
    }

    public void addToAllCalendarsSelectedByUserByCalendarName(String calendarName)
    {
        for (var calendar : calendars) 
        {
            if (calendar.getName().equals(calendarName))
                calendarsSelectedByUser.add(calendar);            
        }
    }*/
/*
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
                returnValue.add((Entry<String>) entry);
            }
        }
        return returnValue;
    }*/

    

    public List<Entry<String>> getEntrysWithStartInSpecificRange(LocalDateTime start, LocalDateTime end) {
        var returnValue = new ArrayList<Entry<String>>();
        var allEntries = new ArrayList<List<Entry<?>>>();
        for (Calendar calendar : getAllCalendars()) {
            allEntries.addAll(
                    calendar.findEntries(start.toLocalDate(), end.toLocalDate(), ZoneId.systemDefault()).values());
        }
        for (var entries : allEntries)
            for (var entry : entries)
                if (entry.getStartAsLocalDateTime().isAfter(start.minusSeconds(1))
                        && entry.getStartAsLocalDateTime().isBefore(end.plusSeconds(1)))
                    returnValue.add((Entry<String>) entry);
        return returnValue;
    }
}