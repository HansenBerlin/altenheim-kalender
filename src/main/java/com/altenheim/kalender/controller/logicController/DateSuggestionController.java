package com.altenheim.kalender.controller.logicController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import com.altenheim.kalender.interfaces.IDateSuggestionController;
import com.calendarfx.model.Entry;

public class DateSuggestionController implements IDateSuggestionController 
{
    public Entry<String> getDateSuggestionFromEntryList(ArrayList<Entry<?>> input, LocalDateTime startSearchDateTimeInput, int dateLenght) 
    {
        var startSearchDateTime  = LocalDateTime.of(startSearchDateTimeInput.toLocalDate(), startSearchDateTimeInput.toLocalTime());
        
        if (input.get(input.size()-1).getEndAsLocalDateTime().isBefore(startSearchDateTime.plusMinutes((long) dateLenght + 1))) 
            return null;

        for (int runNumber = 0; runNumber < input.size(); runNumber++) 
        {
            var entryStart = input.get(runNumber).getStartAsLocalDateTime();
            
            if (entryStart.isAfter(startSearchDateTime))
                startSearchDateTime = input.get(runNumber).getStartAsLocalDateTime();
            
            if (entryStart.isBefore(startSearchDateTime.plusSeconds(1))
                && entryStart.isAfter(startSearchDateTime.plusMinutes(dateLenght).minusSeconds(1)))
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
