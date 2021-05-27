package com.altenheim.calendar.interfaces;

import com.calendarfx.model.Calendar;

public interface IAppointmentEntryFactory 
{
    public Calendar getSavedEntries();
    public void createEntrys(String calendarName);
}
