package com.altenheim.kalender.interfaces;

import java.time.LocalDateTime;
import java.util.ArrayList;
import com.altenheim.kalender.models.SerializableEntry;

public interface IDateSuggestionController {
    SerializableEntry getDateSuggestionFromEntryList(ArrayList<SerializableEntry> input, LocalDateTime startSearchDateTime, int dateLenght);
}
