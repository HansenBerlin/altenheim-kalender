package models;

import java.util.GregorianCalendar;
import interfaces.ICalendarEntryModel;

public class CalendarEntryModel implements ICalendarEntryModel
{
    private GregorianCalendar startDate;
    private GregorianCalendar endDate;
    private String appointmentEntryName;
    private int travelTime;

    public CalendarEntryModel(int travelTime)
    {
    	this.travelTime = travelTime;
    }

    public CalendarEntryModel(GregorianCalendar startDate2, GregorianCalendar endDate2, String appointmentEntryName)
    {
        this.startDate = startDate2;
        this.endDate = endDate2;
        this.appointmentEntryName = appointmentEntryName;
    }


    public void resetDates(GregorianCalendar startDate, GregorianCalendar endDate)
    {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void resetAppointmentEntryName(String entry)
    {
        appointmentEntryName = entry;
    }

    public GregorianCalendar getStartDate()
    {
        return startDate;        
    }

    public GregorianCalendar getEndDate()
    {        
            return endDate;
    }

    public String getAppointmentEntryName()
    {
        return appointmentEntryName;
    }    
}
