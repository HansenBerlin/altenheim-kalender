package com.altenheim.kalender.interfaces;

import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.List;
import com.altenheim.kalender.models.SerializableEntry;

public interface IGoogleAPIController 
{
    HashMap<DayOfWeek, List<SerializableEntry>> getOpeningHours(String locationSearchUserInput);
    int[] searchForDestinationDistance(String startAt, String destination);
    int[] searchForDestinationDistance(String origin, String destination, String travelMode);
}
