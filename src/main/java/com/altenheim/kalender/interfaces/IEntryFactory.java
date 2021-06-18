package com.altenheim.kalender.interfaces;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.Entry;

public interface IEntryFactory extends IContactFactory
{
    void createRandomCalendarList();
    HashMap<String, List<Entry<?>>> createEntryListForEachCalendar();
    Entry<String> createUserEntry (LocalDate dateStart, LocalDate dateEnd, LocalTime timeStart, LocalTime timeEnd);
    ArrayList<ArrayList<Entry<?>>> createOpeningHoursWithLunchBreak();
    void addCalendarToView(Calendar calendar);
}
