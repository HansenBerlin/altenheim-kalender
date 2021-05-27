package com.altenheim.kalender.interfaces;

import java.time.LocalDate;
import java.time.LocalTime;
import com.calendarfx.model.Entry;

public interface ICalendarEntryModel 
{    
    public void resetAppointmentEntryName(String entry);    
    public String getAppointmentEntryName();
    public Entry<Entry<?>> getCalendarObject();
    public void resetDates(LocalDate startDate, LocalDate endDate);
    public void resetTime(LocalTime startTime, LocalTime endTime);
}
