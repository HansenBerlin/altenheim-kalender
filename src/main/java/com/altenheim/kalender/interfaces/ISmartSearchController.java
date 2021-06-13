package com.altenheim.kalender.interfaces;

import java.util.ArrayList;
import com.calendarfx.model.Entry;

public interface ISmartSearchController 
{
	public ArrayList<Entry<String>> findAvailableTimeSlot(Entry<String> input, int duration);
	public ArrayList<Entry<String>> findAvailableTimeSlot(Entry<String> input, int duration, boolean[] weekdays, Entry<String>	selectedHours, int maxNumberOfReturnEntrys);
	public ArrayList<Entry<String>> findAvailableTimeSlot(Entry<String> input, int duration, boolean[] weekdays, Entry<String>	selectedHours, int timeBefore, int timeAfter, int maxNumberOfReturnEntrys);
	public ArrayList<Entry<String>> findAvailableTimeSlot(Entry<String> input, int duration, boolean[] weekdays, Entry<String>	selectedHours, ArrayList<ArrayList<Entry<String>>> openingHours, int maxNumberOfReturnEntrys);
	public ArrayList<Entry<String>> findAvailableTimeSlot(Entry<String> input, int duration, boolean[] weekdays, Entry<String>	selectedHours, ArrayList<ArrayList<Entry<String>>> openingHours, int timeBefore, int timeAfter, int maxNumberOfReturnEntrys);
	
}
