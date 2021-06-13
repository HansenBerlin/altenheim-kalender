package com.altenheim.kalender.controller.logicController;


import java.time.DayOfWeek;
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

	public ArrayList<Entry<?>> findAvailableTimeSlot(Entry<?> input, int duration, boolean[] weekdays, Entry<?>	selectedHours, int maxNumberOfReturnEntrys){
		var workingEntrys = findSelectedWeekdays(input, weekdays);
		workingEntrys = encloseEntryDayTimes(workingEntrys, selectedHours);
		var output = new ArrayList<Entry<?>>();

		for (Entry<?> entry : workingEntrys) {
			output.addAll(findAvailableTimeSlot(entry, duration));
			if (output.size()>= maxNumberOfReturnEntrys ) {
				reduceListLength(output, maxNumberOfReturnEntrys);
				break;
			}
		}
		return output;
	}
	
	public ArrayList<Entry<?>> findAvailableTimeSlot(Entry<?> input, int duration, boolean[] weekdays, Entry<?>	selectedHours, int timeBefore, int timeAfter, int maxNumberOfReturnEntrys){
		modifySelectedHours(selectedHours, timeBefore, timeAfter);
		return findAvailableTimeSlot(input, duration, weekdays, selectedHours, maxNumberOfReturnEntrys);
	}
	
	public ArrayList<Entry<?>> findAvailableTimeSlot(Entry<?> input, int duration, boolean[] weekdays, Entry<?>	selectedHours, ArrayList<ArrayList<Entry<?>>> openingHours, int maxNumberOfReturnEntrys){
		openingHours = compareSelectedAndOpenHours(openingHours, selectedHours);
		var workingEntrys = new ArrayList<Entry<?>>();
		workingEntrys.addAll(findSelectedWeekdays(input, weekdays));
		var entryList = new ArrayList<Entry<?>>();
		var output = new ArrayList<Entry<?>>(); 
		for (Entry<?> entry : workingEntrys) 
			entryList.addAll(encloseEntryDayTimes(entry, openingHours));
		
		for (Entry<?> entry : entryList) {
			output.addAll(findAvailableTimeSlot(entry, duration));
			if (output.size()>= maxNumberOfReturnEntrys ) {
				reduceListLength(output, maxNumberOfReturnEntrys);
				break;
			}
		}

		return output;
	}
	
	public ArrayList<Entry<?>> findAvailableTimeSlot(Entry<?> input, int duration, boolean[] weekdays, Entry<?>	selectedHours, ArrayList<ArrayList<Entry<?>>> openingHours, int timeBefore, int timeAfter, int maxNumberOfReturnEntrys){
		openingHours = modifySelectedHoursList(openingHours, timeBefore, timeAfter);
		duration += timeAfter + timeBefore;
		return  findAvailableTimeSlot(input, duration, weekdays, selectedHours, openingHours, maxNumberOfReturnEntrys);
	}


	public ArrayList<ArrayList<Entry<?>>> compareSelectedAndOpenHours(ArrayList<ArrayList<Entry<?>>> inputOpeningHours, Entry<?> selectedHours) {
		var output = new ArrayList<ArrayList<Entry<?>>>();
		for (ArrayList<Entry<?>> day : inputOpeningHours) {
			if (day != null && !day.isEmpty()) {
				var outputDay = new ArrayList<Entry<?>>();
				for (Entry<?> entry : day) {
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
	
	public ArrayList<Entry<?>> reduceListLength(ArrayList<Entry<?>> input, int length) {
		while (input.size()>length) 
			input.remove(input.size()-1);
		return input;
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
	
	public ArrayList<Entry<?>> encloseEntryDayTimes(ArrayList<Entry<?>> input, Entry<?>	selectedHours) {
		var output = new ArrayList<Entry<?>>();
		for (Entry<?> entry : input) 
			output.addAll(encloseEntryDayTimes(entry, selectedHours));
		return output;
	}
	
	public ArrayList<Entry<?>> encloseEntryDayTimes(Entry<?> input, Entry<?>	selectedHours) {
		var output = new ArrayList<Entry<?>>();
		int periode = Days.between(input.getStartDate(), input.getEndDate()).getAmount();
		LocalDate day;
		for (int i = 0; i <= periode; i++) {
			day = input.getStartDate().plusDays(i);
			output.add(createEntryFormStartAndEndDateAndTime(day, day, selectedHours.getStartTime(), selectedHours.getEndTime()));
		}
		return output;
	}	
	
	public ArrayList<Entry<?>> encloseEntryDayTimes(Entry<?> input, ArrayList<ArrayList<Entry<?>>> selectedHours) {
		var output = new ArrayList<Entry<?>>();
		int periode = Days.between(input.getStartDate(), input.getEndDate()).getAmount();
		LocalDate day;
		for (int i = 0; i <= periode; i++) {
			day = input.getStartDate().plusDays(i);
			var daynumber = day.getDayOfWeek().getValue()-1; //Weekdays don´t start bye 0
			var selectedHoursDay = selectedHours.get(daynumber);
			if (selectedHoursDay != null && !selectedHoursDay.isEmpty()) 
				for (Entry<?> entry : selectedHoursDay)
					output.add(createEntryFormStartAndEndDateAndTime(day, day, entry.getStartTime(), entry.getEndTime()));
		}
		return output;
	}
	
	public ArrayList<ArrayList<Entry<?>>> modifySelectedHoursList(ArrayList<ArrayList<Entry<?>>> selectedHours, int timeBefore, int timeAfter) {
		for (ArrayList<Entry<?>> entry : selectedHours) 
			modifySelectedHours(entry, timeBefore, timeAfter);
		return selectedHours;
	}
	
	public ArrayList<Entry<?>> modifySelectedHours(ArrayList<Entry<?>> selectedHours, int timeBefore, int timeAfter) {
		for (Entry<?> entry : selectedHours) 
			modifySelectedHours(entry, timeBefore, timeAfter);
		return selectedHours;
	}
	
	public Entry<?> modifySelectedHours(Entry<?> selectedHours, int timeBefore, int timeAfter) {
		selectedHours.changeStartTime(selectedHours.getStartTime().minusMinutes(timeBefore));
		selectedHours.changeEndTime(selectedHours.getEndTime().plusMinutes(timeAfter));
		return selectedHours;
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
	// 1. Wenn Margins/Fahrtzeiten eingegeben werden Aufruf von Update Duration aus der View. --> neue Duration
	// 2. Aufruf createCalendarFromUserInput mit Übergabe der updated duration oder der default --> Kalender mit möglichen Einträgen
	//    nach Berücksichtigung der Öffnungszeiten (muss in Kalender eingetragen werden um die Recurrencedates zu nutzen, spart Lauf-
	//    zeit und Redundanz)
	// 3. Aufruf createfinalList... mit Übergabe des erzeugten Kalenders, hier wird auch die Vorschlagsfunktion aufgerufen, Intervalle berücksichtigt
	//    etc. --> fertige Liste mit Einträgen
	
	public int updateDuration(Entry<?> userPrefs, int duration, int marginPre, int marginPost)
	{
		if (userPrefs.getEndTime().toSecondOfDay() - userPrefs.getStartTime().toSecondOfDay() 
			< (duration + marginPre + marginPost) * 60)
			return -1;
		else return duration + marginPre + marginPost;
	}

	public Calendar createCalendarFromUserInput(Entry<?> userPrefs, boolean[] weekdays, HashMap<DayOfWeek, List<Entry<?>>> openingHours, int updatedDuration)
	{
		ArrayList<Entry<?>> possibleEntries = null;
		var possibleRecurringDates = new Calendar();
		if (openingHours != null)		
			possibleEntries = adjustToOpeningHours(updatedDuration, userPrefs, openingHours);
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

