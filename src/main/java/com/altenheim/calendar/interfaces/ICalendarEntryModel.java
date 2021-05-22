package com.altenheim.calendar.interfaces;

import java.util.GregorianCalendar;

public interface ICalendarEntryModel 
{    
    public void resetDates(GregorianCalendar startDate, GregorianCalendar endDate);  
    public void resetAppointmentEntryName(String entry);
    public GregorianCalendar getStartDate();
    public GregorianCalendar getEndDate();
    public String getAppointmentEntryName();
}
