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
}
