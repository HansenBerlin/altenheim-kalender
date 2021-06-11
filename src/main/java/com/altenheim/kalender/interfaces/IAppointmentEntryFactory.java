package com.altenheim.kalender.interfaces;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;

import com.calendarfx.model.Entry;

public interface IAppointmentEntryFactory 
{
    public void createRandomCalendarList(String calendarName);
    public Entry<String> createUserSettingsEntry(LocalTime startSearchTime, LocalTime endSearchTime);
    public HashMap<String, List<Entry<?>>> createEntryListForEachCalendar();
}
