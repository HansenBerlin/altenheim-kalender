package com.altenheim.kalender.controller.logicController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import com.altenheim.kalender.interfaces.IDateSuggestionController;
import com.calendarfx.model.Entry;

public class DateSuggestionController implements IDateSuggestionController 
{
    public Entry<String> getDateSuggestionFromEntryList(ArrayList<Entry<?>> input, LocalDateTime startSearchDateTime, int dateLenght) 
    {
        if (input == null || input.isEmpty() || input.get(input.size()-1).getEndAsLocalDateTime().isBefore(startSearchDateTime.plusMinutes((long) dateLenght + 1))) 
            return null;

        var runNumber = 0;
        while (true) 
        {
            if (runNumber > input.size()-1)
                return null;
            
            if (input.get(runNumber).getStartAsLocalDateTime().isAfter(startSearchDateTime))
                startSearchDateTime = input.get(runNumber).getStartAsLocalDateTime();
            
            if (input.get(runNumber).getStartAsLocalDateTime().isBefore(startSearchDateTime.plusSeconds(1))
                && input.get(runNumber).getEndAsLocalDateTime().isAfter(startSearchDateTime.plusMinutes(dateLenght).minusSeconds(1)))
                return createEntry(startSearchDateTime, dateLenght);
            
            runNumber++;
        }
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
