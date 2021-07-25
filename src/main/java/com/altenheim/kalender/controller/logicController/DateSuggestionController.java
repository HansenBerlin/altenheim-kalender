package com.altenheim.kalender.controller.logicController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import com.altenheim.kalender.interfaces.IDateSuggestionController;
import com.altenheim.kalender.models.SerializableEntry;

public class DateSuggestionController implements IDateSuggestionController {
    public SerializableEntry getDateSuggestionFromEntryList(ArrayList<SerializableEntry> input,
            LocalDateTime startSearchDateTimeInput, int dateLenght) {
        var startSearchDateTime = LocalDateTime.of(startSearchDateTimeInput.toLocalDate(),
                startSearchDateTimeInput.toLocalTime());

        if (input.get(input.size() - 1).getEndAsLocalDateTime()
                .isAfter(startSearchDateTime.plusMinutes((long) dateLenght + 1)))
            for (int runNumber = 0; runNumber < input.size(); runNumber++) {
                var entryStart = input.get(runNumber).getStartAsLocalDateTime();

                if (entryStart.isAfter(startSearchDateTime))
                    startSearchDateTime = input.get(runNumber).getStartAsLocalDateTime();

                if (entryStart.isBefore(startSearchDateTime.plusSeconds(1)) && input.get(runNumber)
                        .getEndAsLocalDateTime().isAfter(startSearchDateTime.plusMinutes(dateLenght).minusSeconds(1)))
                    return createEntry(startSearchDateTime, dateLenght);
            }
        return null;
    }

    private SerializableEntry createEntry(LocalDateTime startDateTime, int dateLenght) {
        var output = new SerializableEntry();
        output.changeEndDate(startDateTime.plusMinutes(dateLenght).toLocalDate());
        output.changeStartDate(startDateTime.toLocalDate());
        output.changeEndTime(startDateTime.plusMinutes(dateLenght).toLocalTime());
        output.changeStartTime(startDateTime.toLocalTime());
        return output;
    }
    
}
