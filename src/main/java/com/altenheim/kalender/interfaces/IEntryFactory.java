package com.altenheim.kalender.interfaces;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import com.calendarfx.model.Calendar;
import com.calendarfx.model.Entry;

public interface IEntryFactory 
{    
    void clearCalendarSourceList();
    void createRandomCalendarList();
    void addCalendarToView(Calendar calendar, String name);
    void createNewUserEntryIncludingTravelTimes(LocalDate dateStart, LocalDate dateEnd,
    LocalTime timeStart, LocalTime timeEnd, String title, int timeTravel);
    HashMap<String, List<Entry<String>>> createEntryListForEachCalendar();
    Entry<String> createUserEntry(LocalDate dateStart, LocalDate dateEnd, LocalTime timeStart, LocalTime timeEnd);
}
