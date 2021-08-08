package com.altenheim.kalender.interfaces.factorys;

import java.time.LocalDate;
import java.time.LocalTime;
import com.altenheim.kalender.interfaces.logicController.IOController;
import com.calendarfx.model.Calendar;
import com.calendarfx.model.Entry;

public interface EntryFactory
{    
    void clearCalendarSourceList();
    void createRandomCalendarList();
    void addCalendarToView(Calendar calendar, String name);
    void createNewUserEntryIncludingTravelTimes(LocalDate dateStart, LocalDate dateEnd,
        LocalTime timeStart, LocalTime timeEnd, String title, int timeTravel, String calName);
    void addIOController(IOController ioController);
    Entry<String> createEntry(LocalDate startDate, LocalTime startTime, LocalTime endTime);
    Entry<String> createUserEntry(LocalDate dateStart, LocalDate dateEnd, LocalTime timeStart, LocalTime timeEnd);
    Entry<String> createCalendarFXEntryFromMillis(long start, long end);
}
