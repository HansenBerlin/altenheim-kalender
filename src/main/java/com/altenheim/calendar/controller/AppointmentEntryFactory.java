package com.altenheim.calendar.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import com.altenheim.calendar.interfaces.IAppointmentEntryFactory;
import com.altenheim.calendar.interfaces.ICalendarEntriesModel;
import com.altenheim.calendar.interfaces.ICalendarEntryModel;
import com.altenheim.calendar.models.CalendarEntryModel;

public class AppointmentEntryFactory implements IAppointmentEntryFactory
{    
    private ICalendarEntriesModel savedEntries;
    
    public AppointmentEntryFactory(ICalendarEntriesModel savedEntries)
    {
        this.savedEntries = savedEntries;
        //hgsdfhgsdfjhsgdf
    }

    public ICalendarEntriesModel getSavedEntries()
    {
        return savedEntries;
    }    
    
    public List<ICalendarEntryModel> createEntrys() 
    {
        int dayOfMonth;
        var allEntrys = new ArrayList<ICalendarEntryModel>();
        for (int i = 1; i <= 12; i++) 
        {
            if (Arrays.asList(new int[]{1, 3, 5, 7, 8, 10, 12}).contains(i))
                dayOfMonth = 31;
            else if (Arrays.asList(new int[]{4, 6, 9, 11}).contains(i))
                dayOfMonth = 30;
            else
                dayOfMonth = 28;

            for (int j = 1; j <= dayOfMonth; j+=rG(1,4)) 
            {                
                var startAndEndDate = LocalDate.of(2021, i, j);
                var startTime = LocalTime.of(rG(1,18), rG(1,59));
                var endTime = LocalTime.of(rG(19,23), rG(1,59));
              
                allEntrys.add(new CalendarEntryModel(startAndEndDate, startAndEndDate, 
                    startTime, endTime, "Test" + rG(1, 100000)));     
            }            
        }
        return allEntrys;
    }

    private int rG(int startInclusive, int endInclusive)
    {
        return ThreadLocalRandom.current().nextInt(startInclusive, endInclusive + 1);
    }    
}
