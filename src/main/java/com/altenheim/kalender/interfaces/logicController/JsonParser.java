package com.altenheim.kalender.interfaces.logicController;

import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.List;
import com.calendarfx.model.Entry;

public interface JsonParser
{
    String parseJsonForLocationId(String jsonBody);
    HashMap<DayOfWeek, List<Entry<String>>> parseJsonForOpeningHours(String jsonBody);
}
