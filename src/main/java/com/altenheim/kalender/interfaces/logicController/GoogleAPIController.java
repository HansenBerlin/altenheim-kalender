package com.altenheim.kalender.interfaces.logicController;

import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.List;
import com.calendarfx.model.Entry;

public interface GoogleAPIController
{
    HashMap<DayOfWeek, List<Entry<String>>> getOpeningHours(String locationSearchUserInput);
    int[] searchForDestinationDistance(String origin, String destination, String travelMode);
}
