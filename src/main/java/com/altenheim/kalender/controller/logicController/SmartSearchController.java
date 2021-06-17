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
			for (int i = 0; i <= entries.size(); i++) 
			{		
				if (i >= 0 && i < entries.size())
					end = entries.get(i).getStartMillis();
				if (i > 0)				
					start = entries.get(i-1).getEndMillis();				
				if (i == entries.size())				
					end = userEnd;								
				if (end - start >= duration*59000 && !(end-userStart <= duration*59000 
					|| userEnd-start <= duration*59000))
					output.add(createEntryFromMillis(start, end));
				if (checkForDuplicates(output))
					output.remove(output.size()-1);							
			}		
		}			
		return output;
	}

	private Entry<String> createEntryFromMillis(long start, long end)
	{
		var entry = new Entry<String>();
		var dateStart = LocalDateTime.ofInstant(Instant.ofEpochMilli(start), ZoneId.systemDefault());
		var dateEnd = LocalDateTime.ofInstant(Instant.ofEpochMilli(end), ZoneId.systemDefault());		
		entry.changeStartTime(dateStart.toLocalTime());
		entry.changeStartDate(dateStart.toLocalDate());
		entry.changeEndTime(dateEnd.toLocalTime());
		entry.changeEndDate(dateEnd.toLocalDate());
		return entry;
	}

	public boolean checkForDuplicates(ArrayList<Entry<String>> currentEntries)
	{
		if (currentEntries.size() < 2)
			return false;
		return (currentEntries.get(currentEntries.size()-2).getStartMillis() 
			== currentEntries.get(currentEntries.size()-1).getStartMillis());
	}
}