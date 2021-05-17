package interfaces;

import models.CalendarEntryModel;

public interface IAppointmentEntryFactory 
{
    public CalendarEntryModel[] createRandomDates();
    public CalendarEntryModel[] createFixedDates();    
}
