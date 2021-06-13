package com.altenheim.kalender.interfaces;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.Entry;

public interface ISmartSearchController 
{
	public ArrayList<Entry<?>> findAvailableTimeSlot(Entry<?> input, int duration);
	public Calendar createCalendarFromUserInput(Entry<?> userPrefs, boolean[] weekdays, 
		HashMap<DayOfWeek, List<Entry<?>>> openingHours, int updatedDuration);
  
}
