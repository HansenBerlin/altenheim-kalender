package com.altenheim.kalender.interfaces;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.Entry;

public interface ISmartSearchController 
{
	public ArrayList<Entry<String>> findAvailableTimeSlot(Entry<String> input, int duration);
	public Calendar createCalendarFromUserInput(Entry<String> userPrefs, int duration, int marginPre, 
		int marginPost, boolean[] weekdays, HashMap<DayOfWeek, List<Entry<String>>> openingHours);

  
}
