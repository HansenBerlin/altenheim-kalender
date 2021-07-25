package com.altenheim.kalender;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import com.altenheim.kalender.controller.logicController.SmartSearchController;
import com.altenheim.kalender.interfaces.ICalendarEntriesModel;
import com.altenheim.kalender.models.SerializableEntry;
import com.calendarfx.model.Calendar;
import com.calendarfx.model.Entry;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SmartSearchControllerTest 
{
    @Test
    void findAvailableTimeSlot_oneDayOnePossibleSuggestionOnSameDay_shouldReturnOneEntry()
    {
        var entryUser = createEntryDummy(10, 18, 1, 1);
        var entryOneCalendar = createEntryDummy(10, 14, 1, 1);
        var entryTwoCalendar = createEntryDummy(16, 18, 1, 1);
        var allEntriesMock = mock(ICalendarEntriesModel.class);
        var calendarMockEntries = new Calendar();
        calendarMockEntries.addEntries(entryOneCalendar, entryTwoCalendar);
        when(allEntriesMock.getSpecificCalendarByIndex(0)).thenReturn(calendarMockEntries);

        var controller = new SmartSearchController(allEntriesMock);
        var result = controller.findAvailableTimeSlot(entryUser, 60, 0, 0);

        assertEquals(1, result.size());
    }

    @Test
    void findAvailableTimeSlot_OneDayOnePossibleSuggestionOnSameDay_shouldReturnCorrectDuration()
    {
        var entryUser = createEntryDummy(10, 18, 1, 1);
        var entryOneCalendar = createEntryDummy(10, 14, 1, 1);
        var entryTwoCalendar = createEntryDummy(16, 18, 1, 1);
        var allEntriesMock = mock(ICalendarEntriesModel.class);
        var calendarMockEntries = new Calendar();

        calendarMockEntries.addEntries(entryOneCalendar, entryTwoCalendar);
        var list = new ArrayList<Calendar>();
        list.add(calendarMockEntries);
        when(allEntriesMock.getAllCalendars()).thenReturn(list);
        var controller = new SmartSearchController(allEntriesMock);
        var result = controller.findAvailableTimeSlot(entryUser, 60, 0, 0);

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
        var calendarMockEntries = new Calendar();

        calendarMockEntries.addEntries(entryOneCalendar, entryTwoCalendar, entryThreeCalendar);
        var list = new ArrayList<Calendar>();
        list.add(calendarMockEntries);
        when(allEntriesMock.getAllCalendars()).thenReturn(list);
        var controller = new SmartSearchController(allEntriesMock);
        var result = controller.findAvailableTimeSlot(entryUser, 60, 0, 0);

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
        var calendarMockEntries = new Calendar();
        calendarMockEntries.addEntries(entryOneCalendar, entryTwoCalendar, entryThreeCalendar);
        var list = new ArrayList<Calendar>();
        list.add(calendarMockEntries);
        when(allEntriesMock.getAllCalendars()).thenReturn(list);
        var controller = new SmartSearchController(allEntriesMock);
        var result = controller.findAvailableTimeSlot(entryUser, 60, 0, 0);

        long totalMinutes = 0;
        for (var entry : result) { 
            totalMinutes+=entry.getDuration().toMinutes();
         }
        assertEquals(360, totalMinutes);
    }

    @Test
    void findAvailableTimeSlot_oneSuggestionWithOverlappingEntryOnStartOnSameDay_shouldReturnOneEntry()
    {
        var entryUser = createEntryDummy(8, 18, 1, 1);
        var entryOneCalendar = createEntryDummy(7, 11, 1, 1);  
        var allEntriesMock = mock(ICalendarEntriesModel.class);
        var calendarMockEntries = new Calendar();

        calendarMockEntries.addEntries(entryOneCalendar);
        when(allEntriesMock.getSpecificCalendarByIndex(0)).thenReturn(calendarMockEntries);
        var controller = new SmartSearchController(allEntriesMock);
        var result = controller.findAvailableTimeSlot(entryUser, 60, 0, 0);
        
        assertEquals(1, result.size());
    }

    @Test
    void findAvailableTimeSlot_oneSuggestionWithOverlappingEntryOnStartAndEndOnSameDay_shouldReturnOneEntry()
    {
        var entryUser = createEntryDummy(8, 18, 1, 1);
        var entryOneCalendar = createEntryDummy(7, 11, 1, 1);  
        var entryTwoCalendar = createEntryDummy(17, 19, 1, 1); 
        var allEntriesMock = mock(ICalendarEntriesModel.class);
        var calendarMockEntries = new Calendar();

        calendarMockEntries.addEntries(entryOneCalendar, entryTwoCalendar);
        when(allEntriesMock.getSpecificCalendarByIndex(0)).thenReturn(calendarMockEntries);
        var controller = new SmartSearchController(allEntriesMock);
        var result = controller.findAvailableTimeSlot(entryUser, 60, 0, 0);
        
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
        var calendarMockEntries = new Calendar();

        calendarMockEntries.addEntries(entryOneCalendar, entryTwoCalendar, entryThreeCalendar);
        var list = new ArrayList<Calendar>();
        list.add(calendarMockEntries);
        when(allEntriesMock.getAllCalendars()).thenReturn(list);
        var controller = new SmartSearchController(allEntriesMock);
        var result = controller.findAvailableTimeSlot(entryUser, 60, 0, 0);
        
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
        var calendarMockEntries = new Calendar();

        calendarMockEntries.addEntries(entryOneCalendar, entryTwoCalendar, entryThreeCalendar, entryFourCalendar);
        var list = new ArrayList<Calendar>();
        list.add(calendarMockEntries);
        when(allEntriesMock.getAllCalendars()).thenReturn(list);
        var controller = new SmartSearchController(allEntriesMock);
        var result = controller.findAvailableTimeSlot(entryUser, 60, 0, 0);
        
        assertEquals(3, result.size());
    }

    @Test
    void findAvailableTimeSlot_multipleSuggestionsMultipleDaysWithSpanOverTwoDays_shouldReturnTwoEntrys()
    {
        var entryUser = createEntryDummy(1, 23, 10, 13); 
        var entryOneCalendar = createEntryDummy(2, 3, 10, 12);        
        var allEntriesMock = mock(ICalendarEntriesModel.class);
        var calendarMockEntries = new Calendar();

        calendarMockEntries.addEntries(entryOneCalendar);
        when(allEntriesMock.getSpecificCalendarByIndex(0)).thenReturn(calendarMockEntries);
        var controller = new SmartSearchController(allEntriesMock);
        var result = controller.findAvailableTimeSlot(entryUser, 600, 0, 0);
        
        assertEquals(1, result.size());
    }

    public HashMap<DayOfWeek, List<SerializableEntry>> createIrregularOpeningHours()
    {
        var openingHours = new HashMap<DayOfWeek, List<SerializableEntry>>();
        var startTime = LocalTime.of(8, 0);
        var endTimeAlt = LocalTime.of(12, 0);
        var startTimeAlt = LocalTime.of(14, 0);
        var endTime = LocalTime.of(20, 0);

        for (var day : DayOfWeek.values()) 
        {
            var entrys = new ArrayList<SerializableEntry>();
            // Sonntags keine Einträge
            if (day.getValue() == 7)
                continue;
            // 2 Einträge (also Mittagspause von 12-14h) an Dienstagen, Donnerstag und Samstag
            if (day.getValue() %2 == 0)
            {
                var entryOne = new SerializableEntry();
                var entryTwo = new SerializableEntry();
                entryOne.changeStartTime(startTime);
                entryOne.changeEndTime(endTimeAlt);
                entryTwo.changeStartTime(startTimeAlt);
                entryTwo.changeEndTime(endTime);
                entrys.add(entryOne);
                entrys.add(entryTwo);
            }
            // Montag, Mittwoch und Freitag Öffnung von 8-20h
            else
            {
                var entryOne = new SerializableEntry();
                entryOne.changeStartTime(startTime);
                entryOne.changeEndTime(endTime);               
                entrys.add(entryOne);
            }
            openingHours.put(day, entrys);            
        }
        return openingHours;        
    }     

    private SerializableEntry createEntryDummy(int startTime, int EndTime, int startDay, int endDay)
    {
        var entryUser = new SerializableEntry();
        entryUser.setTitle("User Preference");
        var startDate = LocalDate.of(2021, 1, startDay);  
        var endDate = LocalDate.of(2021, 1, endDay);  
        entryUser.changeStartDate(startDate);
        entryUser.changeEndDate(endDate);
        entryUser.changeStartTime(LocalTime.of(startTime, 00, 00));
        entryUser.changeEndTime(LocalTime.of(EndTime, 00, 00));
        return entryUser;
    }
    


 

    @Test
    void findAvailableTimeSlot_Return(){

        var input = new SerializableEntry();
        input = createEntryDummy(10, 19, 7, 6, 20, 6);
        boolean[] weekdays = {true, true, false, false, true, true, true};
        var openingHours2 = createOpeningHoursWithLunchBreak();
        var entryOneCalendar = createEntryDummy(10, 19, 7, 6, 8, 6);
        var allEntriesMock = mock(ICalendarEntriesModel.class);
        var calendarMockEntries = new Calendar();
        calendarMockEntries.addEntries(entryOneCalendar);
        var list = new ArrayList<Calendar>();
        list.add(calendarMockEntries);
        when(allEntriesMock.getAllCalendars()).thenReturn(list);

        var controller2 = new SmartSearchController(allEntriesMock);
        var result2 = controller2.findPossibleTimeSlots(input, 60, weekdays, openingHours2, 30, 30,0);

        assertEquals(9, result2.size()); 
        // 10-13, 16-22 geöffnet an Montagen, Mittwoch, Freitag, Sonntag zu, sonst 10-22
        // möglich:
        // Fr 2*, Sa 1*
        // Mo 2*, Di 1*, Fr. 2*, Sa 1*
        
    }

   

    @Test
    void findAvailableTimeSlot_Return_etwasKomplexer(){

        var input = new SerializableEntry();
        input = createEntryDummy(10, 20, 1, 11, 30, 11);
        boolean[] weekdays = {true, false, false, false, false, false, false};    
        var openingHours2 = createOpeningHoursWithLunchBreak(); 
        var entryOneCalendar = createEntryDummy(10, 22, 1, 11, 1, 11);
        var entryTwoCalendar = createEntryDummy(10, 14, 8, 11, 8, 11);
        var allEntriesMock = mock(ICalendarEntriesModel.class);
        var calendarMockEntries = new Calendar();
        calendarMockEntries.addEntries(entryOneCalendar, entryTwoCalendar);
        var list = new ArrayList<Calendar>();
        list.add(calendarMockEntries);
        when(allEntriesMock.getAllCalendars()).thenReturn(list);

        var controller2 = new SmartSearchController(allEntriesMock);
        var result2 = controller2.findPossibleTimeSlots(input, 60, weekdays, openingHours2, 20, 20 ,0);
 
        assertEquals(7, result2.size());
        // möglich:
        // 1.11: keiner
        // 8.11: einer
        // danach (15., 22, 29, je 2) 
        // --7
    }


    @Test
    void findAvailableTimeSlot_Return_userEntryGenauInÖZUndLängeAuchDurchBlockerUnAnreiseSollteNichtsZurückkommen()
    {        
        var input = new SerializableEntry();
        input = createEntryDummy(10, 20, 1, 11, 1, 11);
        boolean[] weekdays = {true, false, false, false, false, false, false}; 
        var openingHours2 = createOpeningHours(); 
        var entryOneCalendar = createEntryDummy(8, 10, 1, 11, 1, 11);        
        var allEntriesMock = mock(ICalendarEntriesModel.class);
        var calendarMockEntries = new Calendar();
        calendarMockEntries.addEntries(entryOneCalendar);
        var list = new ArrayList<Calendar>();
        list.add(calendarMockEntries);
        when(allEntriesMock.getAllCalendars()).thenReturn(list);

        var controller2 = new SmartSearchController(allEntriesMock);
        var result2 = controller2.findPossibleTimeSlots(input, 600, weekdays, openingHours2, 60, 60 ,0);
        
        assertEquals(0, result2.size()); 
    }

    @Test
    void findAvailableTimeSlot_Return_TBD()
    {        
        var input = new SerializableEntry();
        input = createEntryDummy(10, 20, 1, 11, 1, 11);
        boolean[] weekdays = {true, false, false, false, false, false, false};
        var openingHours2 = createOpeningHours(); 
        var entryOneCalendar = createEntryDummy(8, 11, 1, 11, 1, 11);        
        var allEntriesMock = mock(ICalendarEntriesModel.class);
        var calendarMockEntries = new Calendar();
        calendarMockEntries.addEntries(entryOneCalendar);
        when(allEntriesMock.getSpecificCalendarByIndex(0)).thenReturn(calendarMockEntries);

        var controller2 = new SmartSearchController(allEntriesMock);
        var result2 = controller2.findPossibleTimeSlots(input, 60, weekdays, openingHours2, 60, 60, 0);
        
        assertEquals(1, result2.size()); 
        // möglich:
        // Mo: 1* von 11-19
    
    }

    @Test
    void findAvailableTimeSlot_Return_hhh()
    {        
        var input = new SerializableEntry();
        input = createEntryDummy(16, 20, 1, 11, 8, 11);
        boolean[] weekdays = {false, true, false, true, false, true, false}; 
        var openingHours2 = createOpeningHours(); 
        var entryOneCalendar = createEntryDummy(15, 19, 2, 11, 2, 11);        
        var allEntriesMock = mock(ICalendarEntriesModel.class);
        var calendarMockEntries = new Calendar();
        calendarMockEntries.addEntries(entryOneCalendar);
        when(allEntriesMock.getSpecificCalendarByIndex(0)).thenReturn(calendarMockEntries);

        

        var controller2 = new SmartSearchController(allEntriesMock);
        var result2 = controller2.findPossibleTimeSlots(input, 30, weekdays, openingHours2, 30, 30, 0);
        
        assertEquals(2, result2.size()); 

        // möglich: 2 Termine
    }

    @Test
    void findAvailableTimeSlot_withIntervall_ReturnSixEntrys(){
        var input = new SerializableEntry();
        input = createEntryDummy(16, 20, 1, 11, 8, 11);
        boolean[] weekdays = {true, true, true, true, true, true, true}; 
        var openingHours2 = createOpeningHours(); 
               
        var allEntriesMock = mock(ICalendarEntriesModel.class);
        var calendarMockEntries = new Calendar();
       
        when(allEntriesMock.getSpecificCalendarByIndex(0)).thenReturn(calendarMockEntries);

        var controller2 = new SmartSearchController(allEntriesMock);
        var result2 = controller2.findPossibleTimeSlots(input, 30, weekdays, openingHours2, 30, 30, 10);
        var day = LocalDate.of(2021, 11, 1);
        for (Entry<?> entry : result2) {
            assertEquals(entry.getStartDate(), day);
            day = day.plusDays(10);
        }
        assertEquals(6, result2.size()); 

        // möglich: 6 Termine mit 10 Tagen Abstand
    }

    @Test
    void findAvailableTimeSlot_withIntervall_ReturnThreeEntrys(){
        var input = new SerializableEntry();
        input = createEntryDummy(16, 20, 1, 11, 8, 11);
        boolean[] weekdays = {true, false, true, false, true, false, true}; 
        var openingHours2 = createOpeningHours(); 
               
        var allEntriesMock = mock(ICalendarEntriesModel.class);
        var calendarMockEntries = new Calendar();
       
        when(allEntriesMock.getSpecificCalendarByIndex(0)).thenReturn(calendarMockEntries);

        var controller2 = new SmartSearchController(allEntriesMock);
        var result2 = controller2.findPossibleTimeSlots(input, 30, weekdays, openingHours2, 30, 30, 1);
        var day = LocalDate.of(2021, 11, 1);
        
        assertEquals(result2.get(0).getStartDate(), day);
        day = day.plusDays(2);
        assertEquals(result2.get(1).getStartDate(), day);
        day = day.plusDays(2);
        assertEquals(result2.get(2).getStartDate(), day);
        day = day.plusDays(2);
        
        assertEquals(3, result2.size()); 

        // möglich: 3 Termine 
    }

   ///////////////////////////////////////////////////////////////////////////

    private SerializableEntry createEntryDummy(int startTime, int EndTime, int startDay, int startMonth, int endDay, int endMonth)
    {
        var entryUser = new SerializableEntry();
        entryUser.setTitle("User Preference");
        var startDate = LocalDate.of(2021, startMonth, startDay);  
        var endDate = LocalDate.of(2021, endMonth, endDay);  
        entryUser.changeStartDate(startDate);
        entryUser.changeEndDate(endDate);
        entryUser.changeStartTime(LocalTime.of(startTime, 00, 00));
        entryUser.changeEndTime(LocalTime.of(EndTime, 00, 00));
        return entryUser;
    }

    private  HashMap<DayOfWeek, List<SerializableEntry>> createOpeningHours() {
        var openingHours = new HashMap<DayOfWeek, List<SerializableEntry>>();
        for (int i = 1; i <= 7; i++) {
            var day1 = new ArrayList<SerializableEntry>();
            day1.add(createEntryDummy(10, 20, 1, 1));
            openingHours.put(DayOfWeek.of(i), day1);
        }
        return openingHours;
    }

    private  HashMap<DayOfWeek, List<SerializableEntry>> createOpeningHoursWithLunchBreak() {
        var openingHours = new HashMap<DayOfWeek, List<SerializableEntry>>();
        for (int i = 0; i < 6; i++) {
            var day1 = new ArrayList<SerializableEntry>();
            if (i%2==0) {
                day1.add(createEntryDummy(10, 13, 1, 1));
                day1.add(createEntryDummy(16, 22, 1, 1));
            }else{
                day1.add(createEntryDummy(10, 22, 1, 1));
            }
            
            openingHours.put(DayOfWeek.of(i+1), day1);
        }
        openingHours.put(DayOfWeek.of(7), new ArrayList<SerializableEntry>());
        return openingHours;
    }
}
