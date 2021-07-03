package com.altenheim.kalender.controller.logicController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import com.altenheim.kalender.interfaces.IDateSuggestionController;
import com.calendarfx.model.Entry;

public class DateSuggestionController implements IDateSuggestionController 
{
    public Entry<String> getDateSuggestionFromEntryList(ArrayList<Entry<?>> input, LocalDateTime startSearchDateTime, int dateLenght) 
    {
        if (input == null || input.isEmpty() || input.get(input.size()).getEndAsLocalDateTime().isBefore(startSearchDateTime.plusMinutes((long) dateLenght + 1))) 
        {
            return null;
        }

        var runNumber = 0;
        while (true) 
        {
            if (runNumber > input.size()) 
            {
                return null;
            }

            if (input.get(runNumber).getStartAsLocalDateTime().isBefore(startSearchDateTime.plusSeconds(1))
                && input.get(runNumber).getEndAsLocalDateTime().isAfter(startSearchDateTime.plusMinutes(dateLenght).minusSeconds(1))) 
            {
                var output = new Entry<String>();
                output.changeStartDate(startSearchDateTime.toLocalDate());
                output.changeStartTime(startSearchDateTime.toLocalTime());
                startSearchDateTime = startSearchDateTime.plusMinutes(dateLenght);
                output.changeEndDate(startSearchDateTime.toLocalDate());
                output.changeEndTime(startSearchDateTime.toLocalTime());
                return output;
            }
            runNumber++;
        }
    }
}