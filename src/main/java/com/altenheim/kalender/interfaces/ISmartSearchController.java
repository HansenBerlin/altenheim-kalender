package com.altenheim.kalender.interfaces;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.altenheim.kalender.models.SerializableEntry;
import com.calendarfx.model.Entry;

public interface ISmartSearchController {
	ArrayList<Entry<?>> findPossibleTimeSlots(Entry<?> input, int duration, boolean[] weekdays,
			HashMap<DayOfWeek, List<SerializableEntry>> openingHours, int timeBefore, int timeAfter,
			int maxNumberOfReturnEntrys, int intervalDays);
}
