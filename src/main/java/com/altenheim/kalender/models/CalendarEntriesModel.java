package com.altenheim.kalender.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import com.altenheim.kalender.interfaces.ICalendarEntriesModel;
import com.calendarfx.model.*;

import javafx.event.EventHandler;

public class CalendarEntriesModel implements ICalendarEntriesModel 
{
    private List<Calendar> calendars;
    private List<Calendar> calendarsSelectedByUser;

    public CalendarEntriesModel() {
        calendars = new ArrayList<Calendar>();
    }

    public void addCalendar(Calendar calendar) {
        calendar.addEventHandler(new EventHandler<CalendarEvent>() {

            @Override
            public void handle(CalendarEvent event) {
                // TODO Auto-generated method stub
                // System.out.println(event);
            }

        });
        calendars.add(calendar);
    }

    public List<Calendar> getAllCalendars() { return calendars; }
    public List<Calendar> getAllCalendarsSelectedByUser() { return calendarsSelectedByUser; }

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
    }



    public List<SerializableEntry> getSpecificRange(LocalDate startDate, LocalDate endDate) {
        var calendar = calendars.get(0);
        var result = calendar.findEntries(startDate, endDate, ZoneId.systemDefault());
        var allEntries = result.values();
        var returnValue = new ArrayList<SerializableEntry>();
        for (var entries : allEntries) {
            for (var entry : entries) {
                returnValue.add((SerializableEntry) entry);
            }
        }
        return returnValue;
    }

    public Calendar getSpecificCalendarByName(String calendarName) {
        return new Calendar();
    }

    public Calendar getSpecificCalendarByIndex(int index) {
        return calendars.get(index);
    }

    public List<SerializableEntry> getEntrysWithStartInSpecificRange(LocalDateTime start, LocalDateTime end) {
        var returnValue = new ArrayList<SerializableEntry>();
        var allEntries = new ArrayList<List<Entry<?>>>();
        for (Calendar calendar : calendars) {
            allEntries.addAll(
                    calendar.findEntries(start.toLocalDate(), end.toLocalDate(), ZoneId.systemDefault()).values());
        }
        for (var entries : allEntries)
            for (var entry : entries)
                if (entry.getStartAsLocalDateTime().isAfter(start.minusSeconds(1))
                        && entry.getStartAsLocalDateTime().isBefore(end.plusSeconds(1)))
                    returnValue.add((SerializableEntry) entry);
        return returnValue;
    }
    
}
