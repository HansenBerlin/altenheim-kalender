package com.altenheim.kalender.controller.Factories;

import com.altenheim.kalender.interfaces.*;
import com.calendarfx.model.*;

import javafx.collections.FXCollections;

import com.altenheim.kalender.controller.viewController.CustomViewOverride;
import com.altenheim.kalender.models.SerializableEntry;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class EntryFactory implements IEntryFactory {
    private ICalendarEntriesModel allCalendars;
    private CustomViewOverride calendarView;

    public EntryFactory(ICalendarEntriesModel allCalendars, CustomViewOverride calendarView) {
        this.allCalendars = allCalendars;
        this.calendarView = calendarView;
    }
/*
    public EntryFactory(ICalendarEntriesModel allCalendars) {
        this.allCalendars = allCalendars;

    }

    public ICalendarEntriesModel getEntriesModel() {
        return allCalendars;
    }*/

    public HashMap<String, List<SerializableEntry>> createEntryListForEachCalendar() {
        var result = allCalendars.getAllCalendars();
        var output = new HashMap<String, List<SerializableEntry>>();
        var zoneId = ZoneId.systemDefault();

        for (var calendar : result) {
            var tempList = new ArrayList<SerializableEntry>();
            var firstEntry = LocalDate.ofInstant(calendar.getEarliestTimeUsed(), zoneId);
            var lastEntry = LocalDate.ofInstant(calendar.getLatestTimeUsed(), zoneId);
            var entries = calendar.findEntries(firstEntry, lastEntry, zoneId);

            for (var entry : entries.values()) {
                for (var singleEntry : entry)
                    tempList.add((SerializableEntry) singleEntry);
            }
            output.put(calendar.getName(), tempList);
        }
        return output;
    }

    public void createRandomCalendarList() {
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
    }
/*
    public void addCalendarToView(Calendar calendar) {
        allCalendars.addCalendar(calendar);
        boolean inCalendarsSources = false;
        var calendarSource = new CalendarSource("Saved Calendars");
        for (var calSource : calendarView.getCalendarSources()) {
            if (calSource.getName().equals("Saved Calendars")) {
                calendarSource = calSource;
                inCalendarsSources = true;
                break;
            }
        }
        calendarSource.getCalendars().addAll(calendar);
        if (!inCalendarsSources)
            calendarView.getCalendarSources().addAll(calendarSource);
    }*/

    public void addCalendarToView(Calendar calendar, String name) 
    {
        calendar.setName(name);
        allCalendars.getAllCalendars().clear();
        var calendarList = calendarView.getCalendars();
        //var allCalendarsInCollection = calendarSource.getCalendars();
        //calendarView.getCalendars().clear();
        
        for (var cal : calendarList) 
        {
            if (cal.getName().equals(name))            
                cal = calendar;                
             
            calendarList.add(cal);
            allCalendars.addCalendar(cal);           
        }
        calendarList.add(calendar);
        allCalendars.addCalendar(calendar);
        calendarView.getCalendars().addAll(calendarList);
    }    

    private SerializableEntry createRandomEntry(int day, int month, int startT, int endT) {
        var startAndEndDate = LocalDate.of(2021, month, day);
        var startTime = LocalTime.of(startT, 0);
        var endTime = LocalTime.of(endT, 0);
        var entry = new SerializableEntry();
        entry.changeStartDate(startAndEndDate);
        entry.changeEndDate(startAndEndDate);
        entry.changeStartTime(startTime);
        entry.changeEndTime(endTime);
        return entry;
    }

    public SerializableEntry createUserEntry(LocalDate dateStart, LocalDate dateEnd, LocalTime timeStart,
            LocalTime timeEnd) {
        var entry = new SerializableEntry();
        entry.changeStartTime(timeStart);
        entry.changeStartDate(dateStart);
        entry.changeEndTime(timeEnd);
        entry.changeEndDate(dateEnd);
        return entry;
    }

    public ArrayList<ArrayList<SerializableEntry>> createOpeningHoursWithLunchBreak() {
        ArrayList<ArrayList<SerializableEntry>> openingHours = new ArrayList<ArrayList<SerializableEntry>>();
        for (int i = 0; i < 6; i++) {
            var day1 = new ArrayList<SerializableEntry>();
            if (i % 2 == 0) {
                day1.add(createEntryDummy(10, 13, 1, 1));
                day1.add(createEntryDummy(16, 22, 1, 1));
            } else {
                day1.add(createEntryDummy(10, 22, 1, 1));
            }

            openingHours.add(day1);
        }
        openingHours.add(new ArrayList<SerializableEntry>());
        return openingHours;
    }

    private SerializableEntry createEntryDummy(int startTime, int EndTime, int startDay, int endDay) {
        var entryUser = new SerializableEntry();
        entryUser.setTitle("User Preference");
        var startDate = LocalDate.of(2021, 1, startDay);
        var endDate = LocalDate.of(2021, 1, endDay);
        entryUser.changeStartDate(startDate);
        entryUser.changeEndDate(endDate);
        entryUser.changeStartTime(LocalTime.of(startTime, 00, 00));
        entryUser.changeEndTime(LocalTime.of(EndTime, 00, 00));
        return entryUser;
    }

    private int rG(int startInclusive, int endInclusive) {
        return ThreadLocalRandom.current().nextInt(startInclusive, endInclusive + 1);
    }

    public void createNewUserEntry(LocalDate dateStart, LocalDate dateEnd, LocalTime timeStart,
            LocalTime timeEnd, String title) {
        var entry = new SerializableEntry();
        entry.changeStartTime(timeStart);
        entry.changeStartDate(dateStart);
        entry.changeEndTime(timeEnd);
        entry.changeEndDate(dateEnd);
        entry.setTitle(title);
        allCalendars.getAllCalendars().get(0).addEntries(entry);
    }

    public void createNewUserEntryIncludingTravelTimes(LocalDate dateStart, LocalDate dateEnd,
            LocalTime timeStart, LocalTime timeEnd, String title, int timeTravel) {
        if (timeTravel > 0) {
            var startAt = LocalDateTime.of(dateStart, timeStart);
            startAt = startAt.minusMinutes(timeTravel);
            var endAt = LocalDateTime.of(dateStart, timeStart);

            var entry = new SerializableEntry();
            entry.changeStartTime(startAt.toLocalTime());
            entry.changeStartDate(startAt.toLocalDate());
            entry.changeEndTime(endAt.toLocalTime());
            entry.changeEndDate(endAt.toLocalDate());
            entry.setTitle("Anfahrtzeit für " + title);
            allCalendars.getAllCalendars().get(0).addEntries(entry);
        }
        if (timeTravel > 0) {
            var startAt = LocalDateTime.of(dateEnd, timeEnd);
            var endAt = LocalDateTime.of(dateEnd, timeEnd);
            endAt = endAt.plusMinutes(timeTravel);
            var entry = new SerializableEntry();

            entry.changeStartTime(startAt.toLocalTime());
            entry.changeStartDate(startAt.toLocalDate());
            entry.changeEndTime(endAt.toLocalTime());
            entry.changeEndDate(endAt.toLocalDate());
            entry.setTitle("Anfahrtzeit für " + title);
            allCalendars.getAllCalendars().get(0).addEntries(entry);
        }

        var entry = new SerializableEntry();
        entry.changeStartTime(timeStart);
        entry.changeStartDate(dateStart);
        entry.changeEndTime(timeEnd);
        entry.changeEndDate(dateEnd);
        entry.setTitle(title);
        allCalendars.getAllCalendars().get(0).addEntries(entry);
    }  
    
    
}