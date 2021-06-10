package com.altenheim.kalender.interfaces;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import com.altenheim.kalender.controller.logicController.CalendarSer;
import com.altenheim.kalender.models.EntrySer;


public interface ICalendarEntriesModel extends Serializable
{
    public CalendarSer getSpecificCalendarByIndex(int index);
    public void addCalendar(CalendarSer calendar);
    public List<EntrySer> getSpecificRange(LocalDate startDate, LocalDate endDate); 
    public List<CalendarSer> getAllCalendars();
}
