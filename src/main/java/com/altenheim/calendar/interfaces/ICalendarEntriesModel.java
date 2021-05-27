package com.altenheim.calendar.interfaces;

import java.time.LocalDate;
import java.util.List;
import com.calendarfx.model.Calendar;
import com.calendarfx.model.Entry;

public interface ICalendarEntriesModel 
{
    public Calendar getSpecificCalendarByIndex(int index);
    public void addCalendar(Calendar calendar);
    public List<Entry<String>> getSpecificDay(LocalDate startDate, LocalDate endDate);    
}
