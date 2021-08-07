package com.altenheim.kalender.interfaces.factorys;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import com.calendarfx.model.Calendar;
import com.calendarfx.model.Entry;

public interface EntryFactory
{    
    void clearCalendarSourceList();
    void createRandomCalendarList();
    void addCalendarToView(Calendar calendar, String name);
    void createNewUserEntryIncludingTravelTimes(LocalDate dateStart, LocalDate dateEnd,
        LocalTime timeStart, LocalTime timeEnd, String title, int timeTravel, String calName);
    HashMap<String, List<Entry<String>>> createEntryListForEachCalendar();
    Entry<String> createUserEntry(LocalDate dateStart, LocalDate dateEnd, LocalTime timeStart, LocalTime timeEnd);
}
