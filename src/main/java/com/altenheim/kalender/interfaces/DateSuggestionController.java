package com.altenheim.kalender.interfaces;

import java.time.LocalDateTime;
import java.util.ArrayList;

import com.calendarfx.model.Entry;

public interface DateSuggestionController
{
    Entry<String> getDateSuggestionFromEntryList(ArrayList<Entry<String>> input, LocalDateTime startSearchDateTime, int dateLenght);
}
