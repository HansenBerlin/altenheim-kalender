package models;

import java.util.GregorianCalendar;

import interfaces.ICalendarEntryModel;

public class CalendarEntryModel implements ICalendarEntryModel
{
    private GregorianCalendar startDate;
    private GregorianCalendar endDate;
    private String appointmentEntryName;

    public CalendarEntryModel()
    {

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

    public GregorianCalendar getDate(boolean getStartDate)
    {
        if (getStartDate)
            return startDate;
        else
            return endDate;
    }

    public String getAppointmentEntryName()
    {
        return appointmentEntryName;
    }
    
}
