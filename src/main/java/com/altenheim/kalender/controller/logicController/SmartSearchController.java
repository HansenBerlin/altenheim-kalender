package com.altenheim.kalender.controller.logicController;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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

import impl.org.controlsfx.collections.MappingChange.Map;
import net.fortuna.ical4j.model.WeekDay.Day;

import com.altenheim.kalender.interfaces.ICalendarEntriesModel;
import com.altenheim.kalender.interfaces.ISmartSearchController;

public class SmartSearchController implements ISmartSearchController 
{

	private ICalendarEntriesModel administrateEntries;

	public SmartSearchController(ICalendarEntriesModel administrateEntries) 
	{
		this.administrateEntries = administrateEntries;
	}


	public ArrayList<Entry<String>> reduceListLength(ArrayList<Entry<String>> input, int length) {
		while (input.size()>length) {
			input.remove(input.size()-1);
		}
		return input;
	}

	//return a list of search entrys only for the selected WeekDays in the selected time periode
	public ArrayList<Entry<String>> findSelectedWeekdays(Entry<String> input, boolean[] weekdays) {
		var output = new ArrayList<Entry<String>>();
		var start = input.getStartDate();
		var end = input.getEndDate();
		LocalDate now = input.getStartDate();
		
		while (now.isBefore(end) || now.equals(end)) {
			if (weekdays[now.getDayOfWeek().getValue()-1]) {
				// Tag ist ausgewählt
				
				if (now.equals(end)) {
					
					output.add(createEntryFormStartAndEndDate(start, end));
					return output;
				} else {
					now = now.plusDays(1);
				}
			} else {
				//Tag ist nicht ausgewählt
				if (start.equals(now)) {
					if (now.equals(end)) {
						return output;
					} else {
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
	//vielleicht unnötig
	public ArrayList<Entry<String>> encloseEntryDayTimes(ArrayList<Entry<String>> input, Entry<String>	selectedHours) {
		var output = new ArrayList<Entry<String>>();
		for (Entry<String> entry : input) {
			output.addAll(encloseEntryDayTimes(entry, selectedHours));
		}
		return output;
	}

	public ArrayList<Entry<String>> encloseEntryDayTimes(Entry<String> input, Entry<String>	selectedHours) {
		var output = new ArrayList<Entry<String>>();
		int periode = Days.between(input.getStartDate(), input.getEndDate()).getAmount();
		LocalDate day;
		for (int i = 0; i <= periode; i++) {
			day = input.getStartDate().plusDays(i);
			output.add(createEntryFormStartAndEndDateAndTime(day, day, selectedHours.getStartTime(), selectedHours.getEndTime()));
		}
		return output;
	}	
	
	public ArrayList<Entry<String>> encloseEntryDayTimes(Entry<String> input, ArrayList<ArrayList<Entry<String>>> selectedHours) {
		var output = new ArrayList<Entry<String>>();
		int periode = Days.between(input.getStartDate(), input.getEndDate()).getAmount();
		LocalDate day;

		for (int i = 0; i <= periode; i++) {
			day = input.getStartDate().plusDays(i);
			var daynumber = day.getDayOfWeek().getValue()-1; //Weekdays don´t start bye 0

			var selectedHoursDay = selectedHours.get(daynumber);
			if (selectedHoursDay != null) {
				if (!selectedHoursDay.isEmpty()) {
					for (Entry<String> entry : selectedHoursDay) {
						output.add(createEntryFormStartAndEndDateAndTime(day, day, entry.getStartTime(), entry.getEndTime()));
					}
				}
			}
			
			
		}
		return output;
	}

	public ArrayList<ArrayList<Entry<String>>> modifySelectedHoursList(ArrayList<ArrayList<Entry<String>>> selectedHours, int timeBefore, int timeAfter) {
		for (ArrayList<Entry<String>> entry : selectedHours) {
			entry = modifySelectedHours(entry, timeBefore, timeAfter);
		}
		return selectedHours;
	}
	
	public ArrayList<Entry<String>> modifySelectedHours(ArrayList<Entry<String>> selectedHours, int timeBefore, int timeAfter) {
		for (Entry<String> entry : selectedHours) {
			entry = modifySelectedHours(entry, timeBefore, timeAfter);
		}
		return selectedHours;
	}
	
	public Entry<String> modifySelectedHours(Entry<String> selectedHours, int timeBefore, int timeAfter) {
		selectedHours.changeStartTime(selectedHours.getStartTime().minusMinutes(timeBefore));
		selectedHours.changeEndTime(selectedHours.getEndTime().plusMinutes(timeAfter));
		return selectedHours;
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

	// Anfahrt, Abfahrt, Margin pre, Margin post, Wochentage, 
	// Start Ende Zeit, Start Ende Datum, Anzahl, Öffnungszeiten
	
	// Übergabe Dauer und Anfang und Ende
	// Dauer erhöhen um Anfahrt vorne und evtl hinten, selbes mit Margin
	// Liste aufbauen mit Öffnungszeiten berücksichtigt
	// Liste aufbauen mit Wochentagen berücksichtigt
	// Traveltime erst hinterher checken
	// Abfangen Traveltime ist zu lang für start und endtime?
	// int interval, int recurrences in dritter methde erst

	private boolean[] weekdays = { true, true, true, true, true, true, true };

	public Calendar createCalendarFromUserInput(Entry<String> userPrefs, int duration, int marginPre, 
		int marginPost, boolean[] weekdays, HashMap<DayOfWeek, List<Entry<String>>> openingHours)
	{
		int startSeconds = userPrefs.getStartTime().toSecondOfDay();
		int endSeconds =  userPrefs.getEndTime().toSecondOfDay();
		int durationInSecsWithMargins = (duration + marginPre + marginPost) * 60;
		
		if (startSeconds < marginPre || endSeconds > 86400 - marginPost ||
			endSeconds - startSeconds < durationInSecsWithMargins)
			return null;

		List<Entry<String>> possibleEntries = null;
		if (openingHours != null)		
			possibleEntries = adjustToOpeningHours(duration, userPrefs, createOpeningHours());
		if (possibleEntries == null)
		{
			var sameEntries = new ArrayList<Entry<String>>();
			{
				var startDate = userPrefs.getStartDate()
				for (int i = 0; i < ; i++) {
					
				}
			}
		}
			possibleEntries = addRFC2445RecurrenceRule(weekdays, adjustedEntries)

		
		

		var calendar = new Calendar();


		return calendar;
	}	

	public List<Entry<String>> addRFC2445RecurrenceRule(boolean[] weekdays, List<Entry<String>> adjustedEntries)
	{
		//var listWithRecurrencingDates = new ArrayList<Entry<String>>();
		String[] weekdaysRule = { "MO", "TU", "WE", "TH", "FR", "SA", "SU" };		

		for (int i = 0; i < adjustedEntries.size(); i++) 
		{
			if (weekdays[adjustedEntries.get(i).getStartDate().getDayOfWeek().getValue()])
				adjustedEntries.get(i).setRecurrenceRule("FREQ=WEEKLY;BYDAY=" + weekdaysRule[i] + ";INTERVAL=1;UNTIL=" + endDate);
			else
				adjustedEntries.remove(i--);		
		}			
		//userPrefs.setRecurrenceRule("FREQ=WEEKLY;BYDAY=" + reccurenceDays + ";INTERVAL=1");
		return adjustedEntries;
	}	

	public List<Entry<String>> adjustToOpeningHours(int duration, Entry<String> rawData, HashMap<DayOfWeek, List<Entry<String>>> openingHours)
	{
		long durationInMillis = duration * 60000;
		var adjustedEntrys = new ArrayList<Entry<String>>();
		
		for (int i = 0; i < openingHours.size(); i++) 
		{
			var day = openingHours.get(DayOfWeek.of(i));
			var startTimeRaw = rawData.getStartMillis();
			var endTimeRaw = rawData.getEndMillis();			
			
			for (int j = 0; j < day.size(); j++) 
			{
				var startTimeOpen = day.get(i).getStartMillis();
				var endTimeOpen= day.get(i).getEndMillis();
				if (startTimeRaw < startTimeOpen)
					startTimeRaw = startTimeOpen;
				if (endTimeRaw > endTimeOpen)
					endTimeRaw = endTimeOpen;
				if (endTimeRaw - startTimeRaw < durationInMillis)
					continue;
				else						
					adjustedEntrys.add(createEntryFromMillis(startTimeRaw, endTimeRaw));						
			}
		}
		return adjustedEntrys;
	}

	// ist nur zum Testen hier um Öffnungszeiten zu generieren
	private HashMap<DayOfWeek, List<Entry<String>>> createOpeningHours()
    {
        var openingHours = new HashMap<DayOfWeek, List<Entry<String>>>();
        var startTime = LocalTime.of(8, 0);
        var endTimeAlt = LocalTime.of(12, 0);
        var startTimeAlt = LocalTime.of(14, 0);
        var endTime = LocalTime.of(20, 0);

        for (var day : DayOfWeek.values()) 
        {
            var entrys = new ArrayList<Entry<String>>();
            if (day.getValue() %2 == 0)
            {
                var entryOne = new Entry<String>();
                var entryTwo = new Entry<String>();
                entryOne.changeStartTime(startTime);
                entryOne.changeEndTime(endTimeAlt);
                entryTwo.changeStartTime(startTimeAlt);
                entryTwo.changeEndTime(endTime);
                entrys.add(entryOne);
                entrys.add(entryTwo);
            }
            else
            {
                var entryOne = new Entry<String>();
                entryOne.changeStartTime(startTime);
                entryOne.changeEndTime(endTime);               
                entrys.add(entryOne);
            }
            openingHours.put(day, entrys);            
        }
        return openingHours;        
    }    
}
