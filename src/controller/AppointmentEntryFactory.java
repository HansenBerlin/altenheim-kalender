package controller;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.ThreadLocalRandom;

import interfaces.IAppointmentEntryFactory;
import models.CalendarEntryModel;

public class AppointmentEntryFactory implements IAppointmentEntryFactory
{
    public CalendarEntryModel[] createRandomDates() 
    {
        CalendarEntryModel[] calendarEntries = new CalendarEntryModel[365];
        for (int i = 0; i < calendarEntries.length; i++) 
        {
            if (rG(1,4) == 1)
                calendarEntries[i] = createRandomEntry();            
        }
        return calendarEntries;        
    }

    public CalendarEntryModel[] createFixedDates() 
    {
        CalendarEntryModel[] calendarEntries = new CalendarEntryModel[365];
        for (int i = 0; i < calendarEntries.length; i++) 
        {
            if (i%7 == 0)
                calendarEntries[i] = createRandomEntry();            
        }
        return calendarEntries;         
    }

    
    private CalendarEntryModel createRandomEntry() 
    {
        var startDate = new GregorianCalendar(rG(1,31), rG(1,12), rG(2021,2021), rG(1,18), rG(0,59), rG(0,59));
        var endDate = startDate;
        endDate.set(Calendar.HOUR, rG(19, 23));        
        var newEntry = new CalendarEntryModel(startDate, endDate, "Test" + rG(1, 100000));
        return newEntry;
    }

    private int rG(int startInclusive, int endInclusive)
    {
        return ThreadLocalRandom.current().nextInt(startInclusive, endInclusive + 1);
    }
    
}
