package com.altenheim.kalender.interfaces;

import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.List;
import com.altenheim.kalender.models.SerializableEntry;

public interface IJsonParser {
    String parseJsonForLocationId(String jsonBody);

    public HashMap<DayOfWeek, List<SerializableEntry>> parseJsonForOpeningHours(String jsonBody);
}
