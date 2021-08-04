package com.altenheim.kalender.controller.Factories;

import com.altenheim.kalender.interfaces.*;
import com.altenheim.kalender.models.CalendarEntriesModel;
import com.altenheim.kalender.models.SettingsModel;
import com.calendarfx.model.*;
import javafx.event.EventHandler;
import com.altenheim.kalender.controller.viewController.CustomViewOverride;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class EntryFactory implements IEntryFactory 
{
    private ICalendarEntriesModel allCalendars;
    private CustomViewOverride calendarView;
    private IIOController ioController;
    private SettingsModel settings;
    private IExportController exportController;

    public EntryFactory(ICalendarEntriesModel allCalendars, CustomViewOverride calendarView, 
        IIOController ioController, SettingsModel settings, IExportController exportController) 
    {
        this.allCalendars = allCalendars;
        this.calendarView = calendarView;
        this.ioController = ioController;
        this.exportController = exportController;
        this.settings = settings;
    }

    public static Entry<String> createCalendarFXEntryFromMillis(long start, long end) 
    {
        var entry = new Entry<String>();
        var dateStart = LocalDateTime.ofInstant(Instant.ofEpochMilli(start), ZoneId.systemDefault());
        var dateEnd = LocalDateTime.ofInstant(Instant.ofEpochMilli(end), ZoneId.systemDefault());
        entry.changeStartTime(dateStart.toLocalTime());
        entry.changeStartDate(dateStart.toLocalDate());
        entry.changeEndTime(dateEnd.toLocalTime());
        entry.changeEndDate(dateEnd.toLocalDate());
        return entry;
    } 

    public HashMap<String, List<Entry<String>>> createEntryListForEachCalendar() 
    {
        var result = allCalendars.getAllCalendars();
        var output = new HashMap<String, List<Entry<String>>>();
        var zoneId = ZoneId.systemDefault();

        for (var calendar : result) 
        {
            var tempList = new ArrayList<Entry<String>>();
            var firstEntry = LocalDate.ofInstant(calendar.getEarliestTimeUsed(), zoneId);
            var lastEntry = LocalDate.ofInstant(calendar.getLatestTimeUsed(), zoneId);
            var entries = calendar.findEntries(firstEntry, lastEntry, zoneId);

            for (var entry : entries.values()) {
                for (var singleEntry : entry)
                    tempList.add((Entry<String>) singleEntry);
            }
            output.put(calendar.getName(), tempList);
        }
        return output;
    }

    public void createRandomCalendarList() 
    {
        int dayOfMonth;
        var calendar = new Calendar("TestKalender");
        calendar.setName(calendar.getName());
        for (int i = 1; i <= 12; i++) {
            if (Arrays.asList(new int[] { 1, 3, 5, 7, 8, 10, 12 }).contains(i))
                dayOfMonth = 31;
            else if (Arrays.asList(new int[] { 4, 6, 9, 11 }).contains(i))
                dayOfMonth = 30;
            else
                dayOfMonth = 28;

            for (int j = 1; j <= dayOfMonth; j += rG(1, 4)) {
                for (int k = 8; k < 20; k += 2) {
                    var entry = createRandomEntry(j, i, k, k + rG(1, 3));
                    calendar.addEntries(entry);
                }
            }
        }
        addCalendarToView(calendar, "TestKalender");
        ioController.saveCalendar(calendar, exportController);
    }

    public void addCalendarToView(Calendar calendar, String name) 
    {
        calendar.setName(name);       
        EventHandler<CalendarEvent> eventHandler = event -> handleEvent(event);
        calendar.addEventHandler(eventHandler);
        calendarView.getCalendarSources().get(0).getCalendars().add(calendar);  
        CalendarEntriesModel.calendarsComboBox.add(calendar.getName());
    }  

    public void clearCalendarSourceList()
    {
        calendarView.getCalendarSources().clear();
        var calSource = new CalendarSource("Alle Kalender");
        calendarView.getCalendarSources().add(calSource);
    }
    
    public void handleEvent(CalendarEvent event)
    {
        ioController.saveCalendar(event.getCalendar(), exportController);
    }

    private Entry<String> createRandomEntry(int day, int month, int startT, int endT)
    {
        var startAndEndDate = LocalDate.of(2021, month, day);
        var startTime = LocalTime.of(startT, 0);
        var endTime = LocalTime.of(endT, 0);
        var entry = new Entry<String>();
        entry.changeStartDate(startAndEndDate);
        entry.changeEndDate(startAndEndDate);
        entry.changeStartTime(startTime);
        entry.changeEndTime(endTime);
        return entry;
    }

    public Entry<String> createUserEntry(LocalDate dateStart, LocalDate dateEnd, LocalTime timeStart, LocalTime timeEnd) 
    {
        var entry = new Entry<String>();
        entry.changeStartTime(timeStart);
        entry.changeStartDate(dateStart);
        entry.changeEndTime(timeEnd);
        entry.changeEndDate(dateEnd);
        return entry;
    }
    
    private int rG(int startInclusive, int endInclusive) 
    {
        return ThreadLocalRandom.current().nextInt(startInclusive, endInclusive + 1);
    }

    public void createNewUserEntryIncludingTravelTimes(LocalDate dateStart, LocalDate dateEnd,
            LocalTime timeStart, LocalTime timeEnd, String title, int timeTravel) 
    {
        String calName = settings.defaultCalendarForSearchView;
        if (timeTravel > 0) 
        {
            var startAt = LocalDateTime.of(dateStart, timeStart);
            startAt = startAt.minusMinutes(timeTravel);
            var endAt = LocalDateTime.of(dateStart, timeStart);
            var entry = createUserEntry(startAt.toLocalDate(), endAt.toLocalDate(), startAt.toLocalTime(), endAt.toLocalTime());          
            entry.setTitle("Anfahrtzeit für " + title);
            allCalendars.addEntryToCalendarWithName(calName, entry);
        }
        if (timeTravel > 0) 
        {
            var startAt = LocalDateTime.of(dateEnd, timeEnd);
            var endAt = LocalDateTime.of(dateEnd, timeEnd);
            endAt = endAt.plusMinutes(timeTravel);            
            var entry = createUserEntry(startAt.toLocalDate(), endAt.toLocalDate(), startAt.toLocalTime(), endAt.toLocalTime()); 
            entry.setTitle("Anfahrtzeit für " + title);
            allCalendars.addEntryToCalendarWithName(calName, entry);
        }
        var entry = createUserEntry(dateStart, dateEnd,timeStart, timeEnd); 
        entry.setTitle(title);
        allCalendars.addEntryToCalendarWithName(calName, entry);
    }    
}