package com.altenheim.calendar.models;

import java.time.LocalDate;
import java.time.LocalTime;
import com.altenheim.calendar.interfaces.ICalendarEntryModel;
import com.calendarfx.model.Entry;

public class CalendarEntryModel extends Entry<Entry<?>> implements ICalendarEntryModel
{    
    private String appointmentEntryName;
    private int travelTime;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime startTime;
    private LocalTime endTime;

    public CalendarEntryModel()
    {        
    }

    public CalendarEntryModel(int travelTime)
    {
    	this.travelTime = travelTime;
    }

    public CalendarEntryModel(LocalDate startDate, LocalDate endDate,
        LocalTime startTime, LocalTime endTime, String appointmentEntryName)
    {
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.appointmentEntryName = appointmentEntryName;
    }   

    public String getAppointmentEntryName()
    {
        return appointmentEntryName;
    } 

    public int getTravelTime()
    {
        return travelTime;
    }

    public void setTravelTime(int travelTime)
    {
        this.travelTime = travelTime;
    }

    public Entry<Entry<?>> getCalendarObject()
    {
        var newEntry = new Entry<Entry<?>>();
        newEntry.changeStartDate(this.startDate);
        newEntry.changeEndDate(this.endDate);
        newEntry.changeStartTime(this.startTime);
        newEntry.changeStartTime(this.endTime);
        return newEntry;
    }

    public void resetDates(LocalDate startDate, LocalDate endDate)
    {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void resetTime(LocalTime startTime, LocalTime endTime)
    {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public void resetAppointmentEntryName(String entry)
    {
        appointmentEntryName = entry;
    }       
}
