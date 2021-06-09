package com.altenheim.kalender.controller.logicController;

import com.altenheim.kalender.interfaces.IAppointmentEntryFactory;
import com.altenheim.kalender.interfaces.ICalendarEntriesModel;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import com.calendarfx.model.Calendar;
import com.calendarfx.model.Entry;


public class AppointmentEntryFactory extends IOController implements IAppointmentEntryFactory
{    
    private ICalendarEntriesModel allCalendars;

    public AppointmentEntryFactory(ICalendarEntriesModel allCalendars)
    {
        this.allCalendars = allCalendars;
    }
    
    public void createRandomEntrys(String calendarName) 
    {
        var calendar = new Calendar(calendarName);
        int dayOfMonth;
        for (int i = 1; i <= 12; i++) 
        {
            if (Arrays.asList(new int[]{1, 3, 5, 7, 8, 10, 12}).contains(i))
                dayOfMonth = 31;
            else if (Arrays.asList(new int[]{4, 6, 9, 11}).contains(i))
                dayOfMonth = 30;
            else
                dayOfMonth = 28;

            for (int j = 1; j <= dayOfMonth; j += rG(1,4)) 
            {                
                var startAndEndDate = LocalDate.of(2021, i, j);
                var startTime = LocalTime.of(rG(1,18), rG(1,59));
                var endTime = LocalTime.of(rG(19,23), rG(1,59));
                var entry = new Entry<String>("Test" + rG(1, 100000));
                entry.changeStartDate(startAndEndDate);
                entry.changeEndDate(startAndEndDate);
                entry.changeStartTime(startTime);
                entry.changeEndTime(endTime);
                calendar.addEntries(entry);                 
            }            
        }
        allCalendars.addCalendar(calendar);
    }

    public void createTestCalendar()
	{
		var calendar = new Calendar();

		for (int i = 0; i < 18; i+=3) 
		{
			var startAndEndDate = LocalDate.of(2021, 1, 1);
        	var startTime = LocalTime.of(1 + i, i);
        	var endTime = LocalTime.of(2 + i, i*2);
        	var entry = new Entry<String>("Test");
        	entry.changeStartDate(startAndEndDate);
        	entry.changeEndDate(startAndEndDate);
        	entry.changeStartTime(startTime);
        	entry.changeEndTime(endTime);
			calendar.addEntry(entry);
			System.out.printf("Neuer Eintrag von %s bis %s\n", entry.getStartTime(), entry.getEndTime());
		}
        allCalendars.addCalendar(calendar);
	}	

	public Entry<String> createUserSettingsEntry(LocalTime startSearchTime, LocalTime endSearchTime)
	{
		var startAndEndDate = LocalDate.of(2021, 1, 1);        
        var entry = new Entry<String>("Test");
        entry.changeStartDate(startAndEndDate);
        entry.changeEndDate(startAndEndDate);
        entry.changeStartTime(startSearchTime);
        entry.changeEndTime(endSearchTime);

		return entry;
	}

    private int rG(int startInclusive, int endInclusive)
    {
        return ThreadLocalRandom.current().nextInt(startInclusive, endInclusive + 1);
    }    
}
