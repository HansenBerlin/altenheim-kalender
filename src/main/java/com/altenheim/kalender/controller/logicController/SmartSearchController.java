package com.altenheim.kalender.controller.logicController;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import com.calendarfx.model.Entry;
import com.altenheim.kalender.interfaces.ICalendarEntriesModel;
import com.altenheim.kalender.interfaces.ISmartSearchController;

public class SmartSearchController implements ISmartSearchController {

	private ICalendarEntriesModel administrateEntries;

	public SmartSearchController(ICalendarEntriesModel administrateEntries) 
	{
		this.administrateEntries = administrateEntries;
	}


	public ArrayList<Entry<?>> findPossibleTimeSlotsNew(Entry<?> input, int duration, boolean[] weekdays, 
		ArrayList<ArrayList<Entry<?>>> openingHours, int timeBefore, int timeAfter, int maxNumberOfReturnEntrys, int intervalDays){

		var output = new ArrayList<Entry<?>>(); 
		var start = input.getStartTime();
		var end = input.getEndTime();
		var intervalNumber = 1;

		var i = -1;
		while (output.size()<maxNumberOfReturnEntrys) {
		i++;
			var date = input.getStartDate().plusDays(i);
			if(!weekdays[date.getDayOfWeek().getValue()-1])
				continue;
			
			for (var day : openingHours.get(i%7))
			{
				var entry = createEntry(date, start, end);
				if (end.isBefore(day.getStartTime()) || start.isAfter(day.getEndTime()))
					continue;
				if (start.isBefore(day.getStartTime()))
					entry.changeStartTime(day.getStartTime());
				if (end.isAfter(day.getEndTime()))
					entry.changeEndTime(day.getEndTime());
				output.addAll(findAvailableTimeSlot(entry, duration, timeBefore, timeAfter));
				
				if (intervalDays !=0 && intervalNumber >= output.size()) {
					reduceListLenght(output, intervalNumber);
					date = date.plusDays(intervalDays);
					intervalNumber++;
				}
			}			
		}
		if (output.size()>=maxNumberOfReturnEntrys) 
			reduceListLenght(output, maxNumberOfReturnEntrys);
		
		return output;
	}


	public ArrayList<Entry<?>> findPossibleTimeSlots(Entry<?> input, int duration, boolean[] weekdays, 
		ArrayList<ArrayList<Entry<?>>> openingHours, int timeBefore, int timeAfter, int maxNumberOfReturnEntrys)
	{
		var daysduration = (int) (input.getEndDate().toEpochDay() - input.getStartDate().toEpochDay());
		var output = new ArrayList<Entry<?>>(); 
		var start = input.getStartTime();
		var end = input.getEndTime();
		for (int i = 0; i <= daysduration; i++) 
		{
			var date = input.getStartDate().plusDays(i);

			if(!weekdays[date.getDayOfWeek().getValue()-1])
				continue;
			
			for (var day : openingHours.get(i%7))
			{
				var entry = createEntry(date, start, end);
				if (end.isBefore(day.getStartTime()) || start.isAfter(day.getEndTime())) {
					continue;
				}
				if (start.isBefore(day.getStartTime()))
					entry.changeStartTime(day.getStartTime());
				if (end.isAfter(day.getEndTime()))
					entry.changeEndTime(day.getEndTime());
				output.addAll(findAvailableTimeSlot(entry, duration, timeBefore, timeAfter));

				if (output.size()>=maxNumberOfReturnEntrys) {
					reduceListLenght(output, maxNumberOfReturnEntrys);
					return output;
				}

			}			
		}
		return output;
	}	

	private void reduceListLenght(ArrayList<Entry<?>> list, int maxNumberOfEntrys) {
		while (list.size()>maxNumberOfEntrys) {
			list.remove(list.size()-1);
		}
	}

	private Entry<?> createEntry(LocalDate startAndEnd, LocalTime start, LocalTime end)
	{
		var entry = new Entry();				
		entry.changeStartTime(start);
		entry.changeEndTime(end);
		entry.changeStartDate(startAndEnd);
		entry.changeEndDate(startAndEnd);
		return entry;
	}	


	public ArrayList<Entry<?>> findAvailableTimeSlot(Entry<?> input, int duration, int before, int after) {			
		
		var result = administrateEntries.getSpecificCalendarByIndex(0).findEntries(
			input.getStartDate(), input.getEndDate(), ZoneId.systemDefault()).values();		
		var output = new ArrayList<Entry<?>>();
		long start = input.getStartMillis() + before * 60000;
		long end = input.getEndMillis() - after * 60000; 
		long userStart = start;
		long userEnd = end;

		for (var entries : result) 				
			for (int i = 0; i <= entries.size(); i++) 
			{
				if (i >= 0 && i < entries.size())
					end = entries.get(i).getStartMillis();
				if (i > 0)				
					start = entries.get(i-1).getEndMillis();			
				if (i == entries.size())				
					end = userEnd;
				if (end < start)
					continue;	
				if ((end - start)/60000 >= duration && !((end-userStart)/60000 <= duration || (userEnd-start)/60000 <= duration))
					output.add(createEntryFromMillis(start, end));
				if (checkForDuplicates(output))
					output.remove(output.size()-1);							
			}
		if (result.isEmpty())
			output.add(input);
						
		return output;
	}

	private Entry<?> createEntryFromMillis(long start, long end)
	{
		var entry = new Entry();
		var dateStart = LocalDateTime.ofInstant(Instant.ofEpochMilli(start), ZoneId.systemDefault());
		var dateEnd = LocalDateTime.ofInstant(Instant.ofEpochMilli(end), ZoneId.systemDefault());		
		entry.changeStartTime(dateStart.toLocalTime());
		entry.changeStartDate(dateStart.toLocalDate());
		entry.changeEndTime(dateEnd.toLocalTime());
		entry.changeEndDate(dateEnd.toLocalDate());
		return entry;
	}

	private boolean checkForDuplicates(ArrayList<Entry<?>> currentEntries)
	{
		if (currentEntries.size() < 2)
			return false;
		return (currentEntries.get(currentEntries.size()-2).getStartMillis() 
		== currentEntries.get(currentEntries.size()-1).getStartMillis());
	}
	
}

