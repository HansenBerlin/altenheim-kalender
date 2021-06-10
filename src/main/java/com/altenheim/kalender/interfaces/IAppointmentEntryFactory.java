package com.altenheim.kalender.interfaces;

import java.time.LocalTime;

import com.calendarfx.model.Entry;

public interface IAppointmentEntryFactory 
{
    public void createRandomEntrys(String calendarName);
    public void createTestCalendar();
    public Entry<String> createUserSettingsEntry(LocalTime startSearchTime, LocalTime endSearchTime);
}
