package com.altenheim.kalender.controller.logicController;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.calendarfx.model.Calendar;
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


	public ArrayList<Entry<?>> findPossibleTimeSlots(Entry<?> input, int duration, boolean[] weekdays, ArrayList<ArrayList<Entry<?>>> openingHours, int timeBefore, int timeAfter, int maxNumberOfReturnEntrys){
		duration += timeAfter + timeBefore;

		if (openingHours!= null && !openingHours.isEmpty()) {
			openingHours = modifyOpeningHours(openingHours, input, timeBefore, timeAfter);
		}else{
			openingHours = new ArrayList<ArrayList<Entry<?>>>();
			for (var i = 0; i < 7; i++) {
				var day = new ArrayList<Entry<?>>();
				var entry = new Entry<String>();
				entry.changeStartTime(input.getStartTime());
				entry.changeEndTime(input.getEndTime());
				day.add(entry);
				openingHours.add(day);
			}
		}
		
		var output = new ArrayList<Entry<?>>(); 
		for (Entry<?> entryDay : findSelectedWeekdays(input, weekdays)) 
			for (Entry<?> entry : encloseEntryDayTimes(entryDay, openingHours)) {
				output.addAll(findAvailableTimeSlot(entry, duration));
				if (output.size()>= maxNumberOfReturnEntrys ) {
					while (output.size()>maxNumberOfReturnEntrys) 
						output.remove(output.size()-1);
					break;
				}
			}
		return output;
	}


	public ArrayList<Entry<?>> findAvailableTimeSlot(Entry<?> input, int duration) {			
		var result = administrateEntries.getSpecificCalendarByIndex(0).findEntries(
			input.getStartDate(), input.getEndDate(), ZoneId.systemDefault()).values();
		long start = input.getStartMillis();
		long end = input.getEndMillis();
		long userStart = start;
		long userEnd = end;
		var output = new ArrayList<Entry<?>>();

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
	
	public ArrayList<ArrayList<Entry<?>>> modifyOpeningHours(ArrayList<ArrayList<Entry<?>>> inputOpeningHours, Entry<?> selectedHours, int timeBefore, int timeAfter) {
		var output = new ArrayList<ArrayList<Entry<?>>>();
		for (ArrayList<Entry<?>> day : inputOpeningHours) {
			if (day != null && !day.isEmpty()) {
				var outputDay = new ArrayList<Entry<?>>();
				for (Entry<?> entry : day) {
					entry.changeStartTime(entry.getStartTime().minusMinutes(timeBefore));
					entry.changeEndTime(entry.getEndTime().plusMinutes(timeAfter));

					Entry<?> workEntry = new Entry<>();
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
	
	public ArrayList<Entry<?>> findSelectedWeekdays(Entry<?> input, boolean[] weekdays) {
		var output = new ArrayList<Entry<?>>();
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
	
	public ArrayList<Entry<?>> encloseEntryDayTimes(Entry<?> input, ArrayList<ArrayList<Entry<?>>> openingHours) {
		var output = new ArrayList<Entry<?>>();
		int periode = Days.between(input.getStartDate(), input.getEndDate()).getAmount();
		
		for (var i = 0; i <= periode; i++) {
			var day = input.getStartDate().plusDays(i);
			var daynumber = day.getDayOfWeek().getValue()-1; //Weekdays don´t start bye 0
			var selectedHoursDay = openingHours.get(daynumber);
			if (selectedHoursDay != null && !selectedHoursDay.isEmpty()) {
				for (Entry<?> entry : selectedHoursDay)
					output.add(createEntryFormStartAndEndDateAndTime(day, day, entry.getStartTime(), entry.getEndTime()));
			} 
			
		}
		return output;
	}
	
	
	private Entry<?> createEntryFormStartAndEndDate(LocalDate start, LocalDate end) {
		var entry = new Entry();
		entry.changeStartDate(start);
		entry.changeEndDate(end);
		return entry;
	}
	
	private Entry<?> createEntryFormStartAndEndDateAndTime(LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
		var entry = new Entry();
		entry.changeStartDate(startDate);
		entry.changeEndDate(endDate);
		entry.changeStartTime(startTime);
		entry.changeEndTime(endTime);
		return entry;
	}
	
	private boolean checkForDuplicates(ArrayList<Entry<?>> currentEntries)
	{
		if (currentEntries.size() < 2)
			return false;
		return (currentEntries.get(currentEntries.size()-2).getStartMillis() 
		== currentEntries.get(currentEntries.size()-1).getStartMillis());
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
	
	private Entry<?> createEntry(LocalDate startAndEnd, LocalTime start, LocalTime end)
	{
		var entry = new Entry();				
		entry.changeStartTime(start);
		entry.changeEndTime(end);
		entry.changeStartDate(startAndEnd);
		entry.changeEndDate(startAndEnd);
		return entry;
	}	
	
	////////////////////////////////////////////////////////////////////////////////////////////////

	// Flow: 
	// 1. Aufruf createCalendarFromUserInput mit Übergabe der duration oder der default --> Kalender mit möglichen Einträgen
	//    nach Berücksichtigung der Öffnungszeiten (muss in Kalender eingetragen werden um die Recurrencedates zu nutzen, spart Lauf-
	//    zeit und Redundanz)
	// 2. Wenn Margins/Fahrtzeiten eingegeben werden Aufruf von Update Duration aus der View. --> neue Duration
	// 3. Aufruf createfinalList... mit Übergabe des erzeugten Kalenders, hier wird auch die Vorschlagsfunktion aufgerufen, Intervalle berücksichtigt
	//    etc. --> fertige Liste mit Einträgen
	
	public int updateDuration(Entry<?> userPrefs, int duration, int marginPre, int marginPost)
	{
		if (userPrefs.getEndTime().toSecondOfDay() - userPrefs.getStartTime().toSecondOfDay() 
			< (duration + marginPre + marginPost) * 60)
			return -1;
		else return duration + marginPre + marginPost;
	}

	public Calendar createCalendarFromUserInput(Entry<?> userPrefs, boolean[] weekdays, HashMap<DayOfWeek, List<Entry<?>>> openingHours, int duration)
	{
		ArrayList<Entry<?>> possibleEntries = null;
		var possibleRecurringDates = new Calendar();
		if (openingHours != null)		
			possibleEntries = adjustToOpeningHours(duration, userPrefs, openingHours);
		else
		{
			possibleEntries	= new ArrayList<Entry<?>>();	
			for (int i = 0; i < 7 ; i++)				
				possibleEntries.add(createEntry(userPrefs.getStartDate().plusDays(i), 
					userPrefs.getStartTime(), userPrefs.getEndTime()));
		}		
		possibleRecurringDates.addEntries(addRFC2445RecurrenceRule(weekdays, possibleEntries, userPrefs));

		return possibleRecurringDates;
	}	

	public ArrayList<Entry<?>> adjustToOpeningHours(int duration, Entry<?> rawData, HashMap<DayOfWeek, List<Entry<?>>> openingHours)
	{
		var adjustedEntrys = new ArrayList<Entry<?>>();
		
		for (int i = 0; i < openingHours.size(); i++) 
		{
			var day = openingHours.get(DayOfWeek.of(i+1));			
			for (int j = 0; j < day.size(); j++) 
			{
				var startTimeOpen = day.get(j).getStartTime();
				var endTimeOpen= day.get(j).getEndTime();
				var startTimeRaw = rawData.getStartTime();
				var endTimeRaw = rawData.getEndTime();

				if (startTimeRaw.isBefore(startTimeOpen))
					startTimeRaw = startTimeOpen;
				if (endTimeRaw.isAfter(endTimeOpen))
					endTimeRaw = endTimeOpen;
				if (endTimeRaw.toSecondOfDay() - startTimeRaw.toSecondOfDay() < duration * 60)
					continue;
				else						
					adjustedEntrys.add(createEntry(rawData.getStartDate().plusDays(i), startTimeRaw, endTimeRaw));						
			}
		}
		return adjustedEntrys;
	} 

	public ArrayList<Entry<?>> addRFC2445RecurrenceRule(boolean[] weekdays, ArrayList<Entry<?>> adjustedEntries, Entry<?> userPrefs)
	{
		String[] weekdaysRule = { "MO", "TU", "WE", "TH", "FR", "SA", "SU" };
		var weeksduration = (int)(userPrefs.getEndDate().toEpochDay() -
			userPrefs.getStartDate().toEpochDay())/7;		

		for (int i = 0; i < adjustedEntries.size(); i++) 
		{
			var checkForWeekday = adjustedEntries.get(i).getStartDate().getDayOfWeek().getValue()-1;
			if (weekdays[checkForWeekday])
				adjustedEntries.get(i).setRecurrenceRule("FREQ=WEEKLY;BYDAY=" + weekdaysRule[checkForWeekday] 
																			  + ";INTERVAL=1;COUNT=" 
																			  + weeksduration);
			else
				adjustedEntries.remove(i--);		
		}
		return adjustedEntries;
	}
	
	public List<Entry<?>> createFinalListForTableView(int intervalInDays, int maxSuggestions, Calendar finalDates, int duration)
	{
		var firstLookup = LocalDate.ofInstant(finalDates.getEarliestTimeUsed(), ZoneId.systemDefault());
		var lastLookup = LocalDate.ofInstant(finalDates.getLatestTimeUsed(), ZoneId.systemDefault());
		var possibleSlots = new ArrayList<Entry<?>>();		

		for (int i = 0; i < maxSuggestions; i++) 
		{
			var entries = finalDates.findEntries(firstLookup, lastLookup, ZoneId.systemDefault());
			for (var entriesDay : entries.values()) 
			{
				for (var entry : entriesDay) 
				{
					if (possibleSlots.size() > maxSuggestions)
						break;
					else
					{
						int sizeBefore = possibleSlots.size();
						possibleSlots.addAll(findAvailableTimeSlot(entry, duration));					
						if (possibleSlots.size() > sizeBefore)
						{
							firstLookup = firstLookup.plusDays(intervalInDays);
							break;
						}
					}
				}				
			}			
		}
		return possibleSlots;
	}
}

