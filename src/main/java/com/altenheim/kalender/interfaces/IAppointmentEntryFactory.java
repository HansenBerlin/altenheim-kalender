package com.altenheim.kalender.interfaces;

import com.calendarfx.model.Calendar;

public interface IAppointmentEntryFactory 
{
    public Calendar createEntrys(String calendarName);
}
