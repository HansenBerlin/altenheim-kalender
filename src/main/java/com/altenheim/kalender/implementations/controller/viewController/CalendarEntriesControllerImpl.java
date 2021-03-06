package com.altenheim.kalender.implementations.controller.viewController;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import com.altenheim.kalender.interfaces.viewController.CalendarEntriesController;
import com.calendarfx.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CalendarEntriesControllerImpl implements CalendarEntriesController
{
    private final List<Calendar> calendarsSelectedByUser;
    private final CustomViewOverride calendarView;
    public static final ObservableList<String> calendarsComboBox = FXCollections.observableArrayList();

    public CalendarEntriesControllerImpl(CustomViewOverride calendarView)
    {
        calendarsSelectedByUser = new ArrayList<>();
        this.calendarView = calendarView;
    }
    
    public List<Calendar> getAllCalendarsSelectedByUser() { return calendarsSelectedByUser; }
    public void clearCalendarsSelectedByUser() { calendarsSelectedByUser.clear(); }
    public void addToAllCalendarsSelectedByUser(Calendar calendar) { calendarsSelectedByUser.add(calendar); }

    public List<Calendar> getAllCalendars() 
    { 
        var allCalendars = new ArrayList<Calendar>();
        for (var source : calendarView.getCalendarSources()) 
        {
            allCalendars.addAll(source.getCalendars());
        }
        return allCalendars; 
    }

    public void addEntryToCalendarWithName(String name, Entry<String> entry)
    {
        for (var calendar : getAllCalendars()) 
        {
            if (calendar.getName().equals(name))
            {
                calendar.addEntry(entry);     
                return;
            }
        }
        calendarView.getCalendarSources().get(0).getCalendars().get(0).addEntry(entry);
    }

    public void addToAllCalendarsSelectedByUserByCalendarName(String calendarName)
    {
        for (var calendar : getAllCalendars()) 
        {
            if (calendar.getName().equals(calendarName))
                calendarsSelectedByUser.add(calendar);            
        }
    }

    public List<Entry<String>> getEntrysWithStartInSpecificRange(LocalDateTime start, LocalDateTime end) {
        var returnValue = new ArrayList<Entry<String>>();
        var allEntries = new ArrayList<List<Entry<?>>>();
        for (Calendar calendar : getAllCalendars())
            allEntries.addAll(calendar.findEntries(start.toLocalDate(), end.toLocalDate(), 
                                ZoneId.systemDefault()).values());
        
        for (var entries : allEntries)
            for (var entry : entries)
                if (entry.getStartAsLocalDateTime().isAfter(start.minusSeconds(1))
                        && entry.getStartAsLocalDateTime().isBefore(end.plusSeconds(1)))
                    returnValue.add((Entry<String>) entry);
        return returnValue;
    }

}
