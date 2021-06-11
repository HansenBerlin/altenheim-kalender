package com.altenheim.kalender.interfaces;

import java.util.ArrayList;

import com.calendarfx.model.Entry;
public interface ISmartSearchController 
{
	public ArrayList<Entry<String>> findAvailableTimeSlot(Entry<String> input, int duration);
  
}
