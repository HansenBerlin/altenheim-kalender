package com.altenheim.kalender.interfaces;

import java.util.ArrayList;
import com.calendarfx.model.Entry;

public interface ISmartSearchController 
{
	public ArrayList<Entry<?>> findPossibleTimeSlots(Entry<?> input, int duration, boolean[] weekdays, ArrayList<ArrayList<Entry<?>>> openingHours, int timeBefore, int timeAfter, int maxNumberOfReturnEntrys, int intervalDays);
}
