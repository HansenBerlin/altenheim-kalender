package interfaces;

import java.util.GregorianCalendar;

public interface ICalendarEntryModel 
{
    public void resetDates(GregorianCalendar startDate, GregorianCalendar endDate);  
    public void resetAppointmentEntryName(String entry);
    public GregorianCalendar getDate(boolean getStartDate);
    public String getAppointmentEntryName();
}
