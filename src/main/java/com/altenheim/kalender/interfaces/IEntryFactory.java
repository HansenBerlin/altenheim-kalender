package com.altenheim.kalender.interfaces;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.calendarfx.model.Calendar;
import com.calendarfx.model.Entry;

public interface IEntryFactory 
{
    void initCalendarList();
    void createRandomCalendarList();
    HashMap<String, List<Entry<String>>> createEntryListForEachCalendar();
    Entry<String> createUserEntry(LocalDate dateStart, LocalDate dateEnd, LocalTime timeStart, LocalTime timeEnd);
    ArrayList<ArrayList<Entry<String>>> createOpeningHoursWithLunchBreak();
    void addCalendarToView(Calendar calendar, String name);
    void createNewUserEntryIncludingTravelTimes(LocalDate dateStart, LocalDate dateEnd,
        LocalTime timeStart, LocalTime timeEnd, String title, int timeTravel);

}
