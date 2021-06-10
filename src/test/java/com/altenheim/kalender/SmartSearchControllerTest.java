package com.altenheim.kalender;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;

import com.altenheim.kalender.controller.logicController.CalendarSer;
import com.altenheim.kalender.controller.logicController.SmartSearchController;
import com.altenheim.kalender.interfaces.ICalendarEntriesModel;
import com.altenheim.kalender.models.EntrySer;
import com.calendarfx.model.Calendar;
import com.calendarfx.model.Entry;
import org.junit.jupiter.api.Test;

public class SmartSearchControllerTest 
{  
    @Test
    void findAvailableTimeSlot_oneDayOnePossibleSuggestionOnSameDay_shouldReturnOneEntry()
    {
        var entryUser = createEntryDummy(10, 18, 1, 1);
        var entryOneCalendar = createEntryDummy(10, 14, 1, 1);
        var entryTwoCalendar = createEntryDummy(16, 18, 1, 1);
        var allEntriesMock = mock(ICalendarEntriesModel.class);
        var calendarMockEntries = new CalendarSer();
        calendarMockEntries.addEntries(entryOneCalendar, entryTwoCalendar);
        when(allEntriesMock.getSpecificCalendarByIndex(0)).thenReturn(calendarMockEntries);

        var controller = new SmartSearchController(allEntriesMock);
        var result = controller.findAvailableTimeSlot(entryUser, 60);

        assertEquals(1, result.size());
    }

    @Test
    void findAvailableTimeSlot_OneDayOnePossibleSuggestionOnSameDay_shouldReturnCorrectDuration()
    {
        var entryUser = createEntryDummy(10, 18, 1, 1);
        var entryOneCalendar = createEntryDummy(10, 14, 1, 1);
        var entryTwoCalendar = createEntryDummy(16, 18, 1, 1);
        var allEntriesMock = mock(ICalendarEntriesModel.class);
        var calendarMockEntries = new CalendarSer();

        calendarMockEntries.addEntries(entryOneCalendar, entryTwoCalendar);
        when(allEntriesMock.getSpecificCalendarByIndex(0)).thenReturn(calendarMockEntries);
        var controller = new SmartSearchController(allEntriesMock);
        var result = controller.findAvailableTimeSlot(entryUser, 60);

        assertEquals(120, result.get(0).getDuration().toMinutes());
    }

    @Test
    void findAvailableTimeSlot_multiplePossibleSuggestionOnSameDay_shouldReturnAllSuggestions()
    {
        var entryUser = createEntryDummy(8, 18, 1, 1);
        var entryOneCalendar = createEntryDummy(10, 11, 1, 1);
        var entryTwoCalendar = createEntryDummy(13, 15, 1, 1);
        var entryThreeCalendar = createEntryDummy(16, 17, 1, 1);
        var allEntriesMock = mock(ICalendarEntriesModel.class);
        var calendarMockEntries = new CalendarSer();

        calendarMockEntries.addEntries(entryOneCalendar, entryTwoCalendar, entryThreeCalendar);
        when(allEntriesMock.getSpecificCalendarByIndex(0)).thenReturn(calendarMockEntries);
        var controller = new SmartSearchController(allEntriesMock);
        var result = controller.findAvailableTimeSlot(entryUser, 60);

        assertEquals(4, result.size());
    }

    @Test
    void findAvailableTimeSlot_multiplePossibleSuggestionOnSameDay_shouldReturnCorrectTimeSum()
    {
        var entryUser = createEntryDummy(8, 18, 1, 1);
        var entryOneCalendar = createEntryDummy(10, 11, 1, 1);
        var entryTwoCalendar = createEntryDummy(13, 15, 1, 1);
        var entryThreeCalendar = createEntryDummy(16, 17, 1, 1);
        var allEntriesMock = mock(ICalendarEntriesModel.class);
        var calendarMockEntries = new CalendarSer();

        calendarMockEntries.addEntries(entryOneCalendar, entryTwoCalendar, entryThreeCalendar);
        when(allEntriesMock.getSpecificCalendarByIndex(0)).thenReturn(calendarMockEntries);
        var controller = new SmartSearchController(allEntriesMock);
        var result = controller.findAvailableTimeSlot(entryUser, 60);

        long totalMinutes = 0;
        for (var entry : result)        
            totalMinutes+=entry.getDuration().toMinutes();
        assertEquals(360, totalMinutes);
    }

    @Test
    void findAvailableTimeSlot_oneSuggestionWithOverlappingEntryOnStartOnSameDay_shouldReturnOneEntry()
    {
        var entryUser = createEntryDummy(8, 18, 1, 1);
        var entryOneCalendar = createEntryDummy(7, 11, 1, 1);  
        var allEntriesMock = mock(ICalendarEntriesModel.class);
        var calendarMockEntries = new CalendarSer();

        calendarMockEntries.addEntries(entryOneCalendar);
        when(allEntriesMock.getSpecificCalendarByIndex(0)).thenReturn(calendarMockEntries);
        var controller = new SmartSearchController(allEntriesMock);
        var result = controller.findAvailableTimeSlot(entryUser, 60);
        
        assertEquals(1, result.size());
    }

    @Test
    void findAvailableTimeSlot_oneSuggestionWithOverlappingEntryOnStartAndEndOnSameDay_shouldReturnOneEntry()
    {
        var entryUser = createEntryDummy(8, 18, 1, 1);
        var entryOneCalendar = createEntryDummy(7, 11, 1, 1);  
        var entryTwoCalendar = createEntryDummy(17, 19, 1, 1); 
        var allEntriesMock = mock(ICalendarEntriesModel.class);
        var calendarMockEntries = new CalendarSer();

        calendarMockEntries.addEntries(entryOneCalendar, entryTwoCalendar);
        when(allEntriesMock.getSpecificCalendarByIndex(0)).thenReturn(calendarMockEntries);
        var controller = new SmartSearchController(allEntriesMock);
        var result = controller.findAvailableTimeSlot(entryUser, 60);
        
        assertEquals(1, result.size());
    }

    @Test
    void findAvailableTimeSlot_multipleSuggestionsWithOverlappingEntryOnStartAndEndOnSameDay_shouldReturnTwoEntrys()
    {
        var entryUser = createEntryDummy(8, 18, 1, 1);
        var entryOneCalendar = createEntryDummy(7, 11, 1, 1);
        var entryTwoCalendar = createEntryDummy(13, 15, 1, 1);
        var entryThreeCalendar = createEntryDummy(16, 19, 1, 1);
        var allEntriesMock = mock(ICalendarEntriesModel.class);
        var calendarMockEntries = new CalendarSer();

        calendarMockEntries.addEntries(entryOneCalendar, entryTwoCalendar, entryThreeCalendar);
        when(allEntriesMock.getSpecificCalendarByIndex(0)).thenReturn(calendarMockEntries);
        var controller = new SmartSearchController(allEntriesMock);
        var result = controller.findAvailableTimeSlot(entryUser, 60);
        
        assertEquals(2, result.size());
    }

    @Test
    void findAvailableTimeSlot_multipleSuggestionsMultipleDays_shouldReturnThreeEntrys()
    {
        //Terminsuche zwischen 10 und 11 Uhr zwischen dem 10.1. und 13.1.
        var entryUser = createEntryDummy(10, 11, 10, 13); 
        var entryOneCalendar = createEntryDummy(10, 11, 9, 9);
        var entryTwoCalendar = createEntryDummy(10, 11, 10, 10);
        var entryThreeCalendar = createEntryDummy(13, 17, 11, 11);
        var entryFourCalendar = createEntryDummy(10, 11, 15, 15);
        // --> freie Termine am 11., 12., und 13.
        var allEntriesMock = mock(ICalendarEntriesModel.class);
        var calendarMockEntries = new CalendarSer();

        calendarMockEntries.addEntries(entryOneCalendar, entryTwoCalendar, entryThreeCalendar, entryFourCalendar);
        when(allEntriesMock.getSpecificCalendarByIndex(0)).thenReturn(calendarMockEntries);
        var controller = new SmartSearchController(allEntriesMock);
        var result = controller.findAvailableTimeSlot(entryUser, 60);
        
        assertEquals(3, result.size());
    }

    @Test
    void findAvailableTimeSlot_multipleSuggestionsMultipleDaysWithSpanOverTwoDays_shouldReturnTwoEntrys()
    {
        var entryUser = createEntryDummy(1, 23, 10, 13); 
        var entryOneCalendar = createEntryDummy(2, 3, 10, 12);        
        var allEntriesMock = mock(ICalendarEntriesModel.class);
        var calendarMockEntries = new CalendarSer();

        calendarMockEntries.addEntries(entryOneCalendar);
        when(allEntriesMock.getSpecificCalendarByIndex(0)).thenReturn(calendarMockEntries);
        var controller = new SmartSearchController(allEntriesMock);
        var result = controller.findAvailableTimeSlot(entryUser, 600);
        
        assertEquals(1, result.size());
    }

    private EntrySer createEntryDummy(int startTime, int EndTime, int startDay, int endDay)
    {
        var entryUser = new EntrySer("User Preference");
        var startDate = LocalDate.of(2021, 1, startDay);  
        var endDate = LocalDate.of(2021, 1, endDay);  
        entryUser.changeStartDate(startDate);
        entryUser.changeEndDate(endDate);
        entryUser.changeStartTime(LocalTime.of(startTime, 00, 00));
        entryUser.changeEndTime(LocalTime.of(EndTime, 00, 00));
        return entryUser;
    }
}
