package com.altenheim.kalender.controller.logicController;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import com.calendarfx.model.Entry;
import com.altenheim.kalender.interfaces.ICalendarEntriesModel;
import com.altenheim.kalender.interfaces.ISmartSearchController;

public class SmartSearchController implements ISmartSearchController 
{

	private ICalendarEntriesModel administrateEntries;

	public SmartSearchController(ICalendarEntriesModel administrateEntries) 
	{
		this.administrateEntries = administrateEntries;
	}
	
	public ArrayList<Entry<String>> findAvailableTimeSlot(Entry<String> input, int duration) 
	{			
		var result = administrateEntries.getSpecificCalendarByIndex(0).findEntries(
			input.getStartDate(), input.getEndDate(), ZoneId.systemDefault()).values();
		long start = input.getStartMillis();
		long end = input.getEndMillis();
		long userStart = start;
		long userEnd = end;
		var output = new ArrayList<Entry<String>>();

		for (var entries : result) 		
		{		
			for (int i = 0; i < entries.size(); i++) 
			{		
				if (i == 0)
					end = entries.get(i).getStartMillis();
				if (i < entries.size()-1)				
					end = entries.get(i+1).getStartMillis();				
				else				
					end = userEnd;				
				start = entries.get(i).getEndMillis();
				if (end-userStart <= duration*60000 || userEnd-start <= duration*60000)
					continue;				
				if (end - start >= duration*60000)				
					output.add(createEntryFromMillis(start, end));				
			}		
		}			
		return output;
	}

	private Entry<String> createEntryFromMillis(long start, long end)
	{
		var entry = new Entry<String>();
		LocalDateTime dateStart = LocalDateTime.ofInstant(Instant.ofEpochMilli(start), ZoneId.systemDefault());
		LocalDateTime dateEnd = LocalDateTime.ofInstant(Instant.ofEpochMilli(end), ZoneId.systemDefault());		
		entry.changeStartTime(dateStart.toLocalTime());
		entry.changeStartDate(dateStart.toLocalDate());
		entry.changeEndTime(dateEnd.toLocalTime());
		entry.changeEndDate(dateEnd.toLocalDate());
		return entry;
	}
}
