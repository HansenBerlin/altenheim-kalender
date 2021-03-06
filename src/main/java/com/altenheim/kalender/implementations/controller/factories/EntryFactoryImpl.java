package com.altenheim.kalender.implementations.controller.factories;

import com.altenheim.kalender.interfaces.factorys.EntryFactory;
import com.altenheim.kalender.interfaces.logicController.IOController;
import com.altenheim.kalender.interfaces.viewController.CalendarEntriesController;
import com.altenheim.kalender.implementations.controller.viewController.CalendarEntriesControllerImpl;
import com.calendarfx.model.*;
import javafx.event.EventHandler;
import com.altenheim.kalender.implementations.controller.viewController.CustomViewOverride;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class EntryFactoryImpl implements EntryFactory
{
    private final CalendarEntriesController allCalendars;
    private final CustomViewOverride calendarView;
    private IOController ioController;

    public EntryFactoryImpl(CalendarEntriesController allCalendars, CustomViewOverride calendarView)
    {
        this.allCalendars = allCalendars;
        this.calendarView = calendarView;
    }

    public void addIOController(IOController ioController)
    {
        this.ioController = ioController;
    }

    public void handleCalendarPropertyChangedEvent(CalendarEvent event)
    {
        ioController.saveCalendar(event.getCalendar());
    }

    public Entry<String> createCalendarFXEntryFromMillis(long start, long end)
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

    public void createRandomCalendarList()
    {
        int dayOfMonth;
        var calendar = new Calendar("TestKalender");
        calendar.setName(calendar.getName());
        for (int i = 1; i <= 12; i++) 
        {
            if (Objects.equals(new int[]{1, 3, 5, 7, 8, 10, 12}, i))
                dayOfMonth = 31;
            else if (Collections.singletonList(new int[]{4, 6, 9, 11}).contains(i))
                dayOfMonth = 30;
            else
                dayOfMonth = 28;

            for (int j = 1; j <= dayOfMonth; j += rG(4)) 
            {
                for (int k = 8; k < 20; k += 2) 
                {
                    var entry = createRandomEntry(j, i, k, k + rG(3));
                    calendar.addEntries(entry);
                }
            }
        }
        addCalendarToView(calendar, "TestKalender");
        ioController.saveCalendar(calendar);
    }

    public void addCalendarToView(Calendar calendar, String name) 
    {
        if (shouldProcessBeInterrupted(name))
            return;
        calendar.setName(name);       
        EventHandler<CalendarEvent> eventHandler = this::handleCalendarPropertyChangedEvent;
        calendar.addEventHandler(eventHandler);
        calendarView.getCalendarSources().get(0).getCalendars().add(calendar);  
        CalendarEntriesControllerImpl.calendarsComboBox.add(calendar.getName());
        ioController.saveCalendar(calendar);
    }

    public void clearCalendarSourceList()
    {
        calendarView.getCalendarSources().clear();
        var calSource = new CalendarSource("Alle Kalender");
        calendarView.getCalendarSources().add(calSource);
    }

    private Entry<String> createRandomEntry(int day, int month, int startT, int endT)
    {
        var startAndEndDate = LocalDate.of(2021, month, day);
        var startTime = java.time.LocalTime.of(startT, 0);
        var endTime = java.time.LocalTime.of(endT, 0);
        var entry = new Entry<String>();
        entry.changeStartDate(startAndEndDate);
        entry.changeEndDate(startAndEndDate);
        entry.changeStartTime(startTime);
        entry.changeEndTime(endTime);
        return entry;
    }

    public Entry<String> createEntry(LocalDate startAndEnd, LocalTime start, LocalTime end) 
    {
		var entry = new Entry<String>();
		entry.changeStartTime(start);
		entry.changeEndTime(end);
		entry.changeStartDate(startAndEnd);
		entry.changeEndDate(startAndEnd);
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
    
    private int rG(int endInclusive)
    {
        return ThreadLocalRandom.current().nextInt(1, endInclusive + 1);
    }

    public void createNewUserEntryIncludingTravelTimes(LocalDate dateStart, LocalDate dateEnd,
            LocalTime timeStart, LocalTime timeEnd, String title, int timeTravel, String calName) 
    {
        if (timeTravel > 0) 
        {
            var startAt = LocalDateTime.of(dateStart, timeStart);
            startAt = startAt.minusMinutes(timeTravel);
            var endAt = LocalDateTime.of(dateStart, timeStart);
            var entry = createUserEntry(startAt.toLocalDate(), endAt.toLocalDate(), startAt.toLocalTime(), endAt.toLocalTime());          
            entry.setTitle("Anfahrtzeit f??r " + title);
            allCalendars.addEntryToCalendarWithName(calName, entry);
        }
        if (timeTravel > 0) 
        {
            var startAt = LocalDateTime.of(dateEnd, timeEnd);
            var endAt = LocalDateTime.of(dateEnd, timeEnd);
            endAt = endAt.plusMinutes(timeTravel);            
            var entry = createUserEntry(startAt.toLocalDate(), endAt.toLocalDate(), startAt.toLocalTime(), endAt.toLocalTime()); 
            entry.setTitle("Abreisezeit f??r " + title);
            allCalendars.addEntryToCalendarWithName(calName, entry);
        }
        var entry = createUserEntry(dateStart, dateEnd,timeStart, timeEnd); 
        entry.setTitle(title);
        allCalendars.addEntryToCalendarWithName(calName, entry);
    }

    private boolean shouldProcessBeInterrupted(String name)
    {
        if (isHwrCalendar(name))
        {
            deleteCalendar(name);
            return false;
        }
        return isCalendarDuplicate(name);
    }

    private void deleteCalendar(String calendarName)
    {
        
        for (var calendar : calendarView.getCalendarSources().get(0).getCalendars())
        {
            if (calendar.getName().equals(calendarName))
            {
                calendarView.getCalendarSources().get(0).getCalendars().remove(calendar);
                return;
            }
        }
    }

    private boolean isCalendarDuplicate(String calendarName)
    {
        for (var calendar : calendarView.getCalendarSources().get(0).getCalendars())
        {
            if (calendar.getName().equals(calendarName))
                return true;
        }
        return false;
    }

    private boolean isHwrCalendar(String name)
    {
        return name.contains("HWR-Kalender");
    }
    
}
