package controller;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.ThreadLocalRandom;
import interfaces.IAppointmentEntryFactory;
import interfaces.ICalendarEntryModel;
import models.CalendarEntryModel;

public class AppointmentEntryFactory implements IAppointmentEntryFactory
{    

    public AppointmentEntryFactory()
    {
    }


    public ICalendarEntryModel[][][] createRandomDatesForOneYear() 
    {
        var year = new ICalendarEntryModel[365][24][60];

        var createdEntries = createEntrys();
        for (var entry : createdEntries) 
        {
            int startHours = entry.getStartDate().get(Calendar.HOUR_OF_DAY);
            int endHours = entry.getEndDate().get(Calendar.HOUR_OF_DAY);
            int startMinutes = entry.getStartDate().get(Calendar.MINUTE);
            int endMinutes = entry.getEndDate().get(Calendar.MINUTE);
            int day = entry.getEndDate().get(Calendar.DAY_OF_YEAR);

            for (int hour = 0; hour < 24; hour++) 
            {
                if (startHours == endHours)
                {
                    for (int minute = startMinutes; minute < endMinutes; minute++)
                    {
                        year[day-1][hour][minute] = entry;
                    }
                    break;
                }                    

                else if (hour == startHours)                
                    for (int minute = startMinutes; minute < 60; minute++)
                    {
                        year[day-1][hour][minute] = entry;
                    }                                         

                else if (hour == endHours)                
                    for (int minute = 0; minute < endMinutes; minute++)    
                    {
                        year[day-1][hour][minute] = entry; 
                    }                                        
                  
                else if (hour > startHours && hour < endHours)                
                    for (int minute = 0; minute < 60; minute++)    
                    {
                        year[day-1][hour][minute] = entry;
                    }                
            }            
        }
        return year;
    }
    
    
    public CalendarEntryModel[] createEntrys() 
    {
        int position = 0;
        var allEntrys = new CalendarEntryModel[120];
        for (int i = 0; i < 12; i++) 
        {
            for (int j = 1; j <= 28; j+=rG(1,4)) 
            {
                if (position >= 120)
                    return allEntrys;
                var startDate = new GregorianCalendar(rG(2021,2021), rG(i,i), rG(j,j), rG(1,18), rG(0,59));
                var endDate = new GregorianCalendar(rG(2021,2021), rG(i,i), rG(j,j), rG(19,23), rG(0,59));               
                allEntrys[position] = new CalendarEntryModel(startDate, endDate, "Test" + rG(1, 100000));     
                position++;   
            }            
        }
        return allEntrys;
    }


    public ICalendarEntryModel createDefinedEntry(int[] startDate, int[] endDate, int[] startTime, 
        int[] endTime, String entryName, int travelTime) 
    {
        if (startDate.length != 3 || endDate.length != 3 || startTime.length != 2 || endTime.length != 2)
            return null;
        var startAppointment = new GregorianCalendar(startDate[0], startDate[1]-1, startDate[2], startTime[0], startTime[1]);
        var endAppointment = new GregorianCalendar(endDate[0], endDate[1]-1, endDate[2], endTime[0], endTime[1]);
       
        ICalendarEntryModel newEntry = new CalendarEntryModel(travelTime);
        newEntry.resetDates(startAppointment, endAppointment);
        newEntry.resetAppointmentEntryName(entryName);
        return newEntry;
    }


    private int rG(int startInclusive, int endInclusive)
    {
        return ThreadLocalRandom.current().nextInt(startInclusive, endInclusive + 1);
    }
    
}
