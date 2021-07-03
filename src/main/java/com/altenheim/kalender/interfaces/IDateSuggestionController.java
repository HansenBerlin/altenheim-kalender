package com.altenheim.kalender.interfaces;

import java.time.LocalDateTime;
import java.util.ArrayList;
import com.calendarfx.model.Entry;

public interface IDateSuggestionController {
    Entry<String> getDateSuggestionFromEntryList(ArrayList<Entry<?>> input, LocalDateTime startSearchDateTime, int dateLenght);
}
