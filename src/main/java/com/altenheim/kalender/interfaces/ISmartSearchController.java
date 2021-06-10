package com.altenheim.kalender.interfaces;

import java.util.ArrayList;
import com.altenheim.kalender.models.EntrySer;
public interface ISmartSearchController 
{
	public ArrayList<EntrySer> findAvailableTimeSlot(EntrySer input, int duration);
  
}
