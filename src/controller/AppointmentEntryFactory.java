package controller;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.ThreadLocalRandom;
import interfaces.IAppointmentEntryFactory;
import interfaces.ICalendarEntriesModel;
import interfaces.ICalendarEntryModel;
import models.CalendarEntryModel;

public class AppointmentEntryFactory implements IAppointmentEntryFactory
{
    private ICalendarEntryModel newEntry;

    public AppointmentEntryFactory(ICalendarEntryModel newEntry)
    {
        this.newEntry = newEntry;
    }

    public ICalendarEntryModel[] createRandomDates() 
    {
        ICalendarEntryModel[] calendarEntries = new CalendarEntryModel[365];
        for (int i = 0; i < calendarEntries.length; i++) 
        {
            if (rG(1,4) == 1)
                calendarEntries[i] = createRandomEntry();            
        }
        return calendarEntries;        
    }

    public ICalendarEntryModel[] createFixedDates() 
    {
        ICalendarEntryModel[] calendarEntries = new CalendarEntryModel[365];
        for (int i = 0; i < calendarEntries.length; i++) 
        {
            if (i%7 == 0)
                calendarEntries[i] = createRandomEntry();            
        }
        return calendarEntries;         
    }

    
    private ICalendarEntryModel createRandomEntry() 
    {
        var startDate = new GregorianCalendar(rG(1,31), rG(1,12), rG(2021,2021), rG(1,18), rG(0,59), rG(0,59));
        var endDate = startDate;
        endDate.set(Calendar.HOUR, rG(19, 23));        
        ICalendarEntryModel newEntry = new CalendarEntryModel(startDate, endDate, "Test" + rG(1, 100000));
        return newEntry;
    }


    public boolean createDefinedEntry(int[] startDate, int[] endDate, int[] startTime, 
        int[] endTime, String entryName, ICalendarEntriesModel savedEntries) 
    {
        if (startDate.length != 3 || endDate.length != 3 || startTime.length != 2 || endTime.length != 2)
            return false;
        var startAppointment = new GregorianCalendar(startDate[0], startDate[1], startDate[2], startTime[0], startTime[1]);
        var endAppointment = new GregorianCalendar(endDate[0], endDate[1], endDate[2], startTime[0], startTime[1]);
        newEntry.resetDates(startAppointment, endAppointment);
        newEntry.resetAppointmentEntryName(entryName);
        savedEntries.saveDate(true, newEntry);
        return true;
    }


    private int rG(int startInclusive, int endInclusive)
    {
        return ThreadLocalRandom.current().nextInt(startInclusive, endInclusive + 1);
    }
    
}
