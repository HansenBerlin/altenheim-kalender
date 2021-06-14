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
	public ArrayList<Entry<?>> findPossibleTimeSlots(Entry<?> input, int duration, boolean[] weekdays, ArrayList<ArrayList<Entry<?>>> openingHours, int timeBefore, int timeAfter, int maxNumberOfReturnEntrys);
}
