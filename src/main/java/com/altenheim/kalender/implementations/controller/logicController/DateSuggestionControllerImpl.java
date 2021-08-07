package com.altenheim.kalender.implementations.controller.logicController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import com.calendarfx.model.Entry;
import com.altenheim.kalender.interfaces.logicController.DateSuggestionController;

public class DateSuggestionControllerImpl implements DateSuggestionController
{
    public Entry<String> getDateSuggestionFromEntryList(ArrayList<Entry<String>> input, LocalDateTime startSearchDateTimeInput, int dateLenght) 
    {
        if (input.isEmpty())
            return null;
            
        var startSearchDateTime  = LocalDateTime.of(startSearchDateTimeInput.toLocalDate(), startSearchDateTimeInput.toLocalTime());
        
        if (input.get(input.size()-1).getEndAsLocalDateTime().isAfter(startSearchDateTime.plusMinutes((long) dateLenght + 1))) 
            for (int runNumber = 0; runNumber < input.size(); runNumber++) 
            {
                var entryStart = input.get(runNumber).getStartAsLocalDateTime();

                if (entryStart.isAfter(startSearchDateTime))
                    startSearchDateTime = input.get(runNumber).getStartAsLocalDateTime();

                if (entryStart.isBefore(startSearchDateTime.plusSeconds(1)) && input.get(runNumber)
                        .getEndAsLocalDateTime().isAfter(startSearchDateTime.plusMinutes(dateLenght).minusSeconds(1)))
                    return createEntry(startSearchDateTime, dateLenght);
            }
        return null;
    }

    private Entry<String> createEntry(LocalDateTime startDateTime, int dateLenght) {
        var output = new Entry<String>();
        output.changeEndDate(startDateTime.plusMinutes(dateLenght).toLocalDate());
        output.changeStartDate(startDateTime.toLocalDate());
        output.changeEndTime(startDateTime.plusMinutes(dateLenght).toLocalTime());
        output.changeStartTime(startDateTime.toLocalTime());
        return output;
    }    
}