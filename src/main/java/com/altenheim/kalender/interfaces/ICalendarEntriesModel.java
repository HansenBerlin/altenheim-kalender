package com.altenheim.kalender.interfaces;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import com.calendarfx.model.Calendar;
import com.calendarfx.model.Entry;

public interface ICalendarEntriesModel extends Serializable
{
    public Calendar getSpecificCalendarByIndex(int index);
    public void addCalendar(Calendar calendar);
    public List<Entry<String>> getSpecificRange(LocalDate startDate, LocalDate endDate); 
    public List<Calendar> getAllCalendars();

}
