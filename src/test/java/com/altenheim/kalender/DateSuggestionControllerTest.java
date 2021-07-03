package com.altenheim.kalender;

import static org.junit.jupiter.api.Assertions.assertTrue;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import com.altenheim.kalender.controller.logicController.DateSuggestionController;
import com.calendarfx.model.Entry;
import org.junit.jupiter.api.Test;

public class DateSuggestionControllerTest {
    @Test
    void getDateSuggestionFromEntryList_ThreePossibleDays_ShouldReturnDateOnFirstDay()
    {
        var entryOne = createEntryDummy(10, 18, 1, 1, 1, 1);
        var entryTwo = createEntryDummy(10, 18, 2, 2, 1, 1);
        var entryThree = createEntryDummy(10, 18, 3, 3, 1, 1);
        var input = new ArrayList<Entry<?>>();
        input.add(entryOne);
        input.add(entryTwo);
        input.add(entryThree);

        var startSearchDateTime = LocalDateTime.of(2021, 1, 1, 10, 0);

        var controller = new DateSuggestionController();
        var result = controller.getDateSuggestionFromEntryList(input, startSearchDateTime, 60);

        var finalResult1 = result.getStartAsLocalDateTime().equals(LocalDateTime.of(2021, 1, 1, 10, 0));
        var finalResult2 = result.getEndAsLocalDateTime().equals(LocalDateTime.of(2021, 1, 1, 11, 0));
        assertTrue(finalResult1);
        assertTrue(finalResult2);
    }

    @Test
    void getDateSuggestionFromEntryList_ThreePossibleDays_ShouldReturnDateOnSecondDay()
    {
        var entryOne = createEntryDummy(10, 18, 1, 1, 1, 1);
        var entryTwo = createEntryDummy(10, 18, 2, 2, 1, 1);
        var entryThree = createEntryDummy(10, 18, 3, 3, 1, 1);
        var input = new ArrayList<Entry<?>>();
        input.add(entryOne);
        input.add(entryTwo);
        input.add(entryThree);

        var startSearchDateTime = LocalDateTime.of(2021, 1, 2, 10, 0);

        var controller = new DateSuggestionController();
        var result = controller.getDateSuggestionFromEntryList(input, startSearchDateTime, 60);

        var finalResult1 = result.getStartAsLocalDateTime().equals(LocalDateTime.of(2021, 1, 2, 10, 0));
        var finalResult2 = result.getEndAsLocalDateTime().equals(LocalDateTime.of(2021, 1, 2, 11, 0));
        assertTrue(finalResult1);
        assertTrue(finalResult2);
    }

    @Test
    void getDateSuggestionFromEntryList_TwoPossibleDays_ShouldReturnDateOnSecondHalfOfTheFirstDay()
    {
        var entryOne = createEntryDummy(10, 15, 1, 1, 1, 1);
        var entryTwo = createEntryDummy(18, 20, 1, 1, 1, 1);
        var entryThree = createEntryDummy(10, 18, 3, 3, 1, 1);
        var input = new ArrayList<Entry<?>>();
        input.add(entryOne);
        input.add(entryTwo);
        input.add(entryThree);

        var startSearchDateTime = LocalDateTime.of(2021, 1, 1, 18, 0);

        var controller = new DateSuggestionController();
        var result = controller.getDateSuggestionFromEntryList(input, startSearchDateTime, 60);

        var finalResult = result.getStartAsLocalDateTime().equals(LocalDateTime.of(2021, 1, 1, 18, 0));
        assertTrue(finalResult);
    }

    private Entry<?> createEntryDummy(int startTime, int EndTime, int startDay, int endDay, int startMonth, int endMonth)
    {
        var entryUser = new Entry<String>("User Preference");
        var startDate = LocalDate.of(2021, startMonth, startDay);  
        var endDate = LocalDate.of(2021, endMonth, endDay);  
        entryUser.changeStartDate(startDate);
        entryUser.changeEndDate(endDate);
        entryUser.changeStartTime(LocalTime.of(startTime, 00, 00));
        entryUser.changeEndTime(LocalTime.of(EndTime, 00, 00));
        return entryUser;
    }
}