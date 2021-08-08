package com.altenheim.kalender.interfaces.logicController;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.calendarfx.model.Entry;

public interface SmartSearchController
{
	ArrayList<Entry<String>> findPossibleTimeSlots(Entry<String> input, int duration, boolean[] weekdays,
			HashMap<DayOfWeek, List<Entry<String>>> openingHours, int timeBefore, int timeAfter, int intervalDays);
}
