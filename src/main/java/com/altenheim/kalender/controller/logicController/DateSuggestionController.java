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
        {
            return null;
        }

        var runNumber = 0;
        while (true) 
        {
            if (runNumber > input.size()-1) 
            {
                return null;
            }
            if (input.get(runNumber).getStartAsLocalDateTime().isAfter(startSearchDateTime)) {
                startSearchDateTime = input.get(runNumber).getStartAsLocalDateTime();
            }
            if (input.get(runNumber).getStartAsLocalDateTime().isBefore(startSearchDateTime.plusSeconds(1))
                && input.get(runNumber).getEndAsLocalDateTime().isAfter(startSearchDateTime.plusMinutes(dateLenght).minusSeconds(1))) 
            {
                var output = new Entry<String>();
                output.changeEndDate(startSearchDateTime.plusMinutes(dateLenght).toLocalDate());
                output.changeStartDate(startSearchDateTime.toLocalDate());
                output.changeEndTime(startSearchDateTime.plusMinutes(dateLenght).toLocalTime());
                output.changeStartTime(startSearchDateTime.toLocalTime());
                return output;
            }
            runNumber++;
        }
    }
}