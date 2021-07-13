package com.altenheim.kalender.interfaces;

import java.io.IOException;
import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.List;
import com.altenheim.kalender.models.SerializableEntry;

public interface IGoogleAPIController 
{
    HashMap<DayOfWeek, List<SerializableEntry>> getOpeningHours(String locationSearchUserInput) throws IOException, InterruptedException;
    int[] searchForDestinationDistance(String startAt, String destination) throws IOException, InterruptedException;
}
