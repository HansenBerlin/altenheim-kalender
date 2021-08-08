package com.altenheim.kalender.interfaces.logicController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import com.calendarfx.model.Entry;

public interface DateSuggestionController
{
    Entry<String> getDateSuggestionFromEntryList(ArrayList<Entry<String>> input, LocalDateTime startSearchDateTime, int dateLenght);
}
