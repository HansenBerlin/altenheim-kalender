package com.altenheim.kalender.controller.logicController;

import com.altenheim.kalender.TempTestClasses.CreateDummyEntries;
import com.altenheim.kalender.TempTestClasses.ICreateDummyEntries;
import com.altenheim.kalender.controller.viewController.CalendarViewOverride;
import com.altenheim.kalender.interfaces.IAppointmentEntryFactory;
import com.altenheim.kalender.interfaces.ICalendarEntriesModel;
import com.altenheim.kalender.models.ContactModel;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;


public class AppointmentEntryFactory extends CreateDummyEntries implements IAppointmentEntryFactory
{    
    private ICalendarEntriesModel allCalendars;
    private CalendarViewOverride calendarView;

    public AppointmentEntryFactory(ICalendarEntriesModel allCalendars, CalendarViewOverride calendarView, List<ContactModel> contacts)
    {
        super(contacts);
        this.allCalendars = allCalendars;
        this.calendarView = calendarView;
    }    

    public ICalendarEntriesModel getEntriesModel()
    {
        return allCalendars;
    }

    public HashMap<String, List<Entry<?>>> createEntryListForEachCalendar() 
	{			
		var result = allCalendars.getAllCalendars();		
		var output = new HashMap<String, List<Entry<?>>>();
        var zoneId = ZoneId.systemDefault();

		for (var calendar : result) 		
		{	
            var tempList = new ArrayList<Entry<?>>();
            var firstEntry = LocalDate.ofInstant(calendar.getEarliestTimeUsed(), zoneId);
            var lastEntry = LocalDate.ofInstant(calendar.getLatestTimeUsed(), zoneId);
            var entries = calendar.findEntries(firstEntry, lastEntry, zoneId);

			for (var entry : entries.values())
			{		
                for (var singleEntry : entry)
                    tempList.add(singleEntry);					
			}	
            output.put(calendar.getName(), tempList);	
		}			
		return output;
	}
    
    public void createRandomCalendarList(String calendarName) 
    {
        int dayOfMonth;
        var calendar = new Calendar();
        var calendearSource = new CalendarSource("Saved Calendars");
        calendar.setName(String.format("%d", calendar.hashCode()));
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
                for (int k = 8; k < 20 ; k+=2) 
                {
                    var entry = createRandomEntry(j, i, k, k+rG(1, 3));
                    calendar.addEntries(entry);                   
                }
            }            
        }
        allCalendars.addCalendar(calendar);
        calendearSource.getCalendars().addAll(allCalendars.getAllCalendars());
        calendarView.getCalendarSources().addAll(calendearSource);  
    }

    private Entry<String> createRandomEntry(int day, int month, int startT, int endT)
    {
        var startAndEndDate = LocalDate.of(2021, month, day);
        var startTime = LocalTime.of(startT, 0);
        var endTime = LocalTime.of(endT, 0);
        var entry = new Entry<String>();
        entry.changeStartDate(startAndEndDate);
        entry.changeEndDate(startAndEndDate);
        entry.changeStartTime(startTime);
        entry.changeEndTime(endTime);
        return entry;
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
