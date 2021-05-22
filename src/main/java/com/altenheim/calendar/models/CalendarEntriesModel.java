package com.altenheim.calendar.models;

import java.util.Calendar;
import com.altenheim.calendar.interfaces.*;

public class CalendarEntriesModel implements ICalendarEntriesModel
{    
    private ICalendarEntryModel[][][] year;
    private IAppointmentEntryFactory entryFactory;   

    public CalendarEntriesModel(IAppointmentEntryFactory entryFactory)
    {
        this.entryFactory = entryFactory;  
    }    

    public void initializeYear()
    {
        this.year = entryFactory.createRandomDatesForOneYear();
    }

    public ICalendarEntryModel[][][] getYear()
    {
        return year;
    }    

    public ICalendarEntryModel getSpecificDate(int day, int hour, int minute)
    {
        try 
        {            
            return year[day][hour][minute];
        } 
        catch (Exception e) 
        {
            return null;    
        }
    }

    public void saveDate(ICalendarEntryModel newEntry)
    {
       // nach Schema der Logik in der Factory implementieren  
    }

    public void printCalendarDates(int numberOfEntriesToPrint)
    {
        String tempEntryName = "";

        for (ICalendarEntryModel[][] day : year) 
        {            
            if (day != null)
            {     
                for (ICalendarEntryModel[] hour : day) 
                {                    
                    if (hour != null)
                    {
                        for (var entry : hour) 
                        {
                            if (entry != null)
                            {
                                if (!entry.getAppointmentEntryName().equals(tempEntryName))
                                {
                                    System.out.println("--------------------------------");
                                    System.out.println("Eintrag: " + entry.getAppointmentEntryName());
                                    System.out.println("Start: Tag/Jahr:      " + entry.getStartDate().get(Calendar.DAY_OF_YEAR));
                                    System.out.println("Start: Tag/Woche:     " + entry.getStartDate().get(Calendar.DAY_OF_WEEK));
                                    System.out.println("Ende: Tag/Woche:      " + entry.getEndDate().get(Calendar.DAY_OF_WEEK));
                                    System.out.println(String.format("Start: %s:%s Uhr", entry.getStartDate().get(Calendar.HOUR_OF_DAY), entry.getStartDate().get(Calendar.MINUTE)));
                                    System.out.println(String.format("Ende:  %s:%s Uhr", entry.getEndDate().get(Calendar.HOUR_OF_DAY), entry.getEndDate().get(Calendar.MINUTE)));
                                    
                                    numberOfEntriesToPrint--;
                                }
                                tempEntryName = entry.getAppointmentEntryName();    
                            }
                        }  
                    } 
                } 
            } 
            if (numberOfEntriesToPrint <= 0)
                break;
                     
        }        
    }
}
