package com.altenheim.kalender.controller.logicController;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import com.calendarfx.model.Entry;
import org.threeten.extra.Days;
import com.altenheim.kalender.interfaces.ICalendarEntriesModel;
import com.altenheim.kalender.interfaces.ISmartSearchController;

public class SmartSearchController implements ISmartSearchController {

	private ICalendarEntriesModel administrateEntries;

	public SmartSearchController(ICalendarEntriesModel administrateEntries) 
	{
		this.administrateEntries = administrateEntries;
	}


	public ArrayList<Entry<String>> findPossibleTimeSlots(Entry<String> input, int duration, boolean[] weekdays, ArrayList<ArrayList<Entry<String>>> openingHours, int timeBefore, int timeAfter, int maxNumberOfReturnEntrys){
		duration += timeAfter + timeBefore;

		if (openingHours!= null && !openingHours.isEmpty()) {
			openingHours = modifyOpeningHours(openingHours, input, timeBefore, timeAfter);
		}else{
			openingHours = new ArrayList<ArrayList<Entry<String>>>();
			for (var i = 0; i < 7; i++) {
				var day = new ArrayList<Entry<String>>();
				var entry = new Entry<String>();
				entry.changeStartTime(input.getStartTime());
				entry.changeEndTime(input.getEndTime());
				day.add(entry);
				openingHours.add(day);
			}
		}
		
		var output = new ArrayList<Entry<String>>(); 
		for (Entry<String> entryDay : findSelectedWeekdays(input, weekdays)) 
			for (Entry<String> entry : encloseEntryDayTimes(entryDay, openingHours)) {
				output.addAll(findAvailableTimeSlot(entry, duration));
				if (output.size()>= maxNumberOfReturnEntrys ) {
					while (output.size()>maxNumberOfReturnEntrys) 
						output.remove(output.size()-1);
					break;
				}
			}
		return output;
	}


	public ArrayList<Entry<String>> findAvailableTimeSlot(Entry<String> input, int duration) {			
		var result = administrateEntries.getSpecificCalendarByIndex(0).findEntries(
			input.getStartDate(), input.getEndDate(), ZoneId.systemDefault()).values();
		long start = input.getStartMillis();
		long end = input.getEndMillis();
		long userStart = start;
		long userEnd = end;
		var output = new ArrayList<Entry<String>>();

		for (var entries : result) 				
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
		if (output.isEmpty()) {
			output.add(input);
		}		
		return output;
	}
	
	public ArrayList<ArrayList<Entry<String>>> modifyOpeningHours(ArrayList<ArrayList<Entry<String>>> inputOpeningHours, Entry<String> selectedHours, int timeBefore, int timeAfter) {
		var output = new ArrayList<ArrayList<Entry<String>>>();
		for (ArrayList<Entry<String>> day : inputOpeningHours) {
			if (day != null && !day.isEmpty()) {
				var outputDay = new ArrayList<Entry<String>>();
				for (Entry<String> entry : day) {
					entry.changeStartTime(entry.getStartTime().minusMinutes(timeBefore));
					entry.changeEndTime(entry.getEndTime().plusMinutes(timeAfter));

					Entry<String> workEntry = new Entry<>();
					if (entry.getStartTime().isAfter(selectedHours.getStartTime())) {
						if (entry.getStartTime().isAfter(selectedHours.getEndTime())) 
							continue;
						else 
							workEntry.changeStartTime(entry.getStartTime());					
					} else
						workEntry.changeStartTime(selectedHours.getStartTime());
					if (entry.getEndTime().isBefore(selectedHours.getEndTime())) {
						if (entry.getEndTime().isBefore(selectedHours.getStartTime()))
							continue;
						else
							workEntry.changeEndTime(entry.getEndTime());
					} else 
						workEntry.changeEndTime(selectedHours.getEndTime());
					outputDay.add(workEntry);
				}
				output.add(outputDay);
			} else 
				output.add(null);
		}
		return output;
	}
	
	public ArrayList<Entry<String>> findSelectedWeekdays(Entry<String> input, boolean[] weekdays) {
		var output = new ArrayList<Entry<String>>();
		var start = input.getStartDate();
		var end = input.getEndDate();
		LocalDate now = input.getStartDate();
		
		while (now.isBefore(end) || now.equals(end)) {
			if (weekdays[now.getDayOfWeek().getValue()-1]) {
				if (now.equals(end)) {
					output.add(createEntryFormStartAndEndDate(start, end));
					return output;
				} else 
					now = now.plusDays(1);
			} else {
				if (start.equals(now)) {
					if (now.equals(end))
						return output;
					else {
						start = start.plusDays(1);
						now = now.plusDays(1);
					}
				}else{
					if (now.equals(end)) {
						output.add(createEntryFormStartAndEndDate(start, end));
						return output;
					} else {
						output.add(createEntryFormStartAndEndDate(start, now.minusDays(1)));
						start = now.plusDays(1);
						now = now.plusDays(1);
					}
				}
			}
		}
		return output;
	}
	
	public ArrayList<Entry<String>> encloseEntryDayTimes(Entry<String> input, ArrayList<ArrayList<Entry<String>>> openingHours) {
		var output = new ArrayList<Entry<String>>();
		int periode = Days.between(input.getStartDate(), input.getEndDate()).getAmount();
		
		for (var i = 0; i <= periode; i++) {
			var day = input.getStartDate().plusDays(i);
			var daynumber = day.getDayOfWeek().getValue()-1; //Weekdays don´t start bye 0
			var selectedHoursDay = openingHours.get(daynumber);
			if (selectedHoursDay != null && !selectedHoursDay.isEmpty()) {
				for (Entry<String> entry : selectedHoursDay)
					output.add(createEntryFormStartAndEndDateAndTime(day, day, entry.getStartTime(), entry.getEndTime()));
			} 
			
		}
		return output;
	}
	
	
	private Entry<String> createEntryFormStartAndEndDate(LocalDate start, LocalDate end) {
		var entry = new Entry<String>();
		entry.changeStartDate(start);
		entry.changeEndDate(end);
		return entry;
	}
	
	private Entry<String> createEntryFormStartAndEndDateAndTime(LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
		var entry = new Entry<String>();
		entry.changeStartDate(startDate);
		entry.changeEndDate(endDate);
		entry.changeStartTime(startTime);
		entry.changeEndTime(endTime);
		return entry;
	}
	
	private Entry<String> createEntryFromMillis(long start, long end){
		var entry = new Entry<String>();
		var dateStart = LocalDateTime.ofInstant(Instant.ofEpochMilli(start), ZoneId.systemDefault());
		var dateEnd = LocalDateTime.ofInstant(Instant.ofEpochMilli(end), ZoneId.systemDefault());		
		entry.changeStartTime(dateStart.toLocalTime());
		entry.changeStartDate(dateStart.toLocalDate());
		entry.changeEndTime(dateEnd.toLocalTime());
		entry.changeEndDate(dateEnd.toLocalDate());
		return entry;
	}
	
	private boolean checkForDuplicates(ArrayList<Entry<String>> currentEntries)	{
		if (currentEntries.size() < 2)
			return false;
		return (currentEntries.get(currentEntries.size()-2).getStartMillis() 
			== currentEntries.get(currentEntries.size()-1).getStartMillis());
	}
}
