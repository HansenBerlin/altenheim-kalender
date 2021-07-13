package com.altenheim.kalender.interfaces;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.altenheim.kalender.models.SerializableEntry;

public interface ISmartSearchController {
	ArrayList<SerializableEntry> findPossibleTimeSlots(SerializableEntry input, int duration, boolean[] weekdays,
			HashMap<DayOfWeek, List<SerializableEntry>> openingHours, int timeBefore, int timeAfter,
			int maxNumberOfReturnEntrys, int intervalDays);
}
