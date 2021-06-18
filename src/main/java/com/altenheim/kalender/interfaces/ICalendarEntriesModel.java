package com.altenheim.kalender.interfaces;

import java.time.LocalDate;
import java.util.List;
import com.calendarfx.model.Calendar;
import com.calendarfx.model.Entry;

public interface ICalendarEntriesModel
{
    Calendar getSpecificCalendarByIndex(int index);
    void addCalendar(Calendar calendar);
    List<Entry<?>> getSpecificRange(LocalDate startDate, LocalDate endDate);
    List<Calendar> getAllCalendars();
}
