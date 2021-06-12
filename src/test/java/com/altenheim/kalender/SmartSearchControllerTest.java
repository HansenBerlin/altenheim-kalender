package com.altenheim.kalender;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;

import com.altenheim.kalender.controller.logicController.SmartSearchController;
import com.altenheim.kalender.interfaces.ICalendarEntriesModel;
import com.calendarfx.model.Calendar;
import com.calendarfx.model.Entry;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SmartSearchControllerTest 
{  
    @Test
    void testInput() // Testet alle Methoden zusammen, also kein Unittest, nur erstmal für mich :-)
    {
        // Terminsuche an allen Wochentagen
        boolean[] weekdays = { true, true, true, true, true, false, false };
        // Terminsuche zwischen dem 1.1 und 31.12.2021, täglich zwischen 10 und 12
        var userPrefs = createEntryDummy(12, 14, 1, 15, 1, 1);
        // Öffnungszeit täglich von 8-20 Uhr
        var openingHours = createIrregularOpeningHours();
        var controller = new SmartSearchController(null);
        // Terminläne 60 Minuten, keine An- oder Abfahrt
        var result = controller.createCalendarFromUserInput(userPrefs, 60, 0, 0, weekdays, openingHours);
        var hashmaptest = createEntryListForEachCalendar(result);
        assertEquals(1, result.findEntries("test").size());
    }


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
        var calendarMockEntries = new Calendar();

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
        var calendarMockEntries = new Calendar();

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
        var calendarMockEntries = new Calendar();

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
        var calendarMockEntries = new Calendar();

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
        var calendarMockEntries = new Calendar();

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
        var calendarMockEntries = new Calendar();

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
        var calendarMockEntries = new Calendar();

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
        var calendarMockEntries = new Calendar();

        calendarMockEntries.addEntries(entryOneCalendar);
        when(allEntriesMock.getSpecificCalendarByIndex(0)).thenReturn(calendarMockEntries);
        var controller = new SmartSearchController(allEntriesMock);
        var result = controller.findAvailableTimeSlot(entryUser, 600);
        
        assertEquals(1, result.size());
    }

    public HashMap<DayOfWeek, List<Entry<String>>> createIrregularOpeningHours()
    {
        var openingHours = new HashMap<DayOfWeek, List<Entry<String>>>();
        var startTime = LocalTime.of(8, 0);
        var endTimeAlt = LocalTime.of(12, 0);
        var startTimeAlt = LocalTime.of(14, 0);
        var endTime = LocalTime.of(20, 0);

        for (var day : DayOfWeek.values()) 
        {
            var entrys = new ArrayList<Entry<String>>();
            // Sonntages keine Einträge
            if (day.getValue() == 7)
                continue;
            // 2 Einträge (also Mittagspause von 12-14h) an Dienstagen, Donnerstag und Samstag
            if (day.getValue() %2 == 0)
            {
                var entryOne = new Entry<String>();
                var entryTwo = new Entry<String>();
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
                var entryOne = new Entry<String>();
                entryOne.changeStartTime(startTime);
                entryOne.changeEndTime(endTime);               
                entrys.add(entryOne);
            }
            openingHours.put(day, entrys);            
        }
        return openingHours;        
    }     

    private Entry<String> createEntryDummy(int startTime, int EndTime, int startDay, int endDay)
    {
        var entryUser = new Entry<String>("User Preference");
        var startDate = LocalDate.of(2021, 1, startDay);  
        var endDate = LocalDate.of(2021, 1, endDay);  
        entryUser.changeStartDate(startDate);
        entryUser.changeEndDate(endDate);
        entryUser.changeStartTime(LocalTime.of(startTime, 00, 00));
        entryUser.changeEndTime(LocalTime.of(EndTime, 00, 00));
        return entryUser;
    }

    private Entry<String> createEntryDummy(int startTime, int EndTime, int startDay, int endDay, int startMonth, int endMonth)
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

    public HashMap<String, List<Entry<?>>> createEntryListForEachCalendar(Calendar calendar) 
	{			
		var output = new HashMap<String, List<Entry<?>>>();
        var zoneId = ZoneId.systemDefault();			
        var tempList = new ArrayList<Entry<?>>();
        var firstEntry = LocalDate.ofInstant(calendar.getEarliestTimeUsed(), zoneId);
        var lastEntry = LocalDate.ofInstant(calendar.getLatestTimeUsed(), zoneId);
        var entries = calendar.findEntries(firstEntry, lastEntry, zoneId);
 	    for (var entry : entries.values())            	
            for (var singleEntry : entry)
                tempList.add(singleEntry); 	        	
        output.put(calendar.getName(), tempList);	
				
		return output;
	}


//Nico Tests
//Start findSelectedWeekdays
    @Test
    void findSelectedWeekdays_oneDaySelectedInOneWeek_shouldReturnOneEntry(){
        var entryUser = new Entry<String>("User Preference");
        entryUser.changeStartDate(LocalDate.of(2021, 6, 7));
        entryUser.changeEndDate(LocalDate.of(2021, 6, 13));
        boolean[] weekdays = {true, false, false, false, false,     false, false};

        var allEntriesMock = mock(ICalendarEntriesModel.class);
        
        
        var controller = new SmartSearchController(allEntriesMock);
        var result = controller.findSelectedWeekdays(entryUser, weekdays);
        
        assertEquals(1, result.size());
    }

    @Test
    void findSelectedWeekdays_nullDaysSelectedInOneWeek_shouldReturnNullEntry(){
        var entryUser = new Entry<String>("User Preference");
        entryUser.changeStartDate(LocalDate.of(2021, 6, 7));
        entryUser.changeEndDate(LocalDate.of(2021, 6, 13));
        boolean[] weekdays = {false, false, false, false, false,     false, false};

        var allEntriesMock = mock(ICalendarEntriesModel.class);
        
        
        var controller = new SmartSearchController(allEntriesMock);
        var result = controller.findSelectedWeekdays(entryUser, weekdays);
        
        assertEquals(0, result.size());
    }

    @Test
    void findSelectedWeekdays_threeDaysSelectedInOneWeek_shouldReturnThreeEntry(){
        var entryUser = new Entry<String>("User Preference");
        entryUser.changeStartDate(LocalDate.of(2021, 6, 7));
        entryUser.changeEndDate(LocalDate.of(2021, 6, 13));
        boolean[] weekdays = {true, false, true, false, true,     false, false};

        var allEntriesMock = mock(ICalendarEntriesModel.class);
        
        
        var controller = new SmartSearchController(allEntriesMock);
        var result = controller.findSelectedWeekdays(entryUser, weekdays);
        
        assertEquals(3, result.size());
    }

    @Test
    void findSelectedWeekdays_threeDaysOneDoubleSelectedInOneWeek_shouldReturnTwoEntry(){
        var entryUser = new Entry<String>("User Preference");
        entryUser.changeStartDate(LocalDate.of(2021, 6, 7));
        entryUser.changeEndDate(LocalDate.of(2021, 6, 13));
        boolean[] weekdays = {true, true, false, true, false,     false, false};

        var allEntriesMock = mock(ICalendarEntriesModel.class);
        
        
        var controller = new SmartSearchController(allEntriesMock);
        var result = controller.findSelectedWeekdays(entryUser, weekdays);
        
        assertEquals(2, result.size());
    }

    @Test
    void findSelectedWeekdays_sevenDaysOneSevenDaysLongSelectedInOneWeek_shouldReturnOneEntry(){
        var entryUser = new Entry<String>("User Preference");
        entryUser.changeStartDate(LocalDate.of(2021, 6, 7));
        entryUser.changeEndDate(LocalDate.of(2021, 6, 13));
        boolean[] weekdays = {true, true, true, true, true,     true, true};

        var allEntriesMock = mock(ICalendarEntriesModel.class);
        
        
        var controller = new SmartSearchController(allEntriesMock);
        var result = controller.findSelectedWeekdays(entryUser, weekdays);
        
        assertEquals(1, result.size());
    }

    @Test
    void findSelectedWeekdays_threeDaysOneDoubleSelectedTwoWeek_shouldReturnFourEntry(){
        var entryUser = new Entry<String>("User Preference");
        entryUser.changeStartDate(LocalDate.of(2021, 6, 7));
        entryUser.changeEndDate(LocalDate.of(2021, 6, 20));
        boolean[] weekdays = {true, true, false, true, false,     false, false};

        var allEntriesMock = mock(ICalendarEntriesModel.class);
        
        
        var controller = new SmartSearchController(allEntriesMock);
        var result = controller.findSelectedWeekdays(entryUser, weekdays);
        
        assertEquals(4, result.size());
    }

//End findSelectedWeekdays

// Start encloseEntryDayTimes
@Test
void encloseEntryDayTimes_OneEntryTwoWeekLength_shouldReturnFourteenEntryWithCorrectTimes(){
    var entryUser = new Entry<String>("User Preference");
    entryUser.changeStartDate(LocalDate.of(2021, 6, 7));
    entryUser.changeEndDate(LocalDate.of(2021, 6, 20));

    var selectedHours = new Entry<String>("Selected Hours");
    selectedHours.changeStartTime(LocalTime.of(10, 0));
    selectedHours.changeEndTime(LocalTime.of(18, 0));

    var allEntriesMock = mock(ICalendarEntriesModel.class);
    
    
    var controller = new SmartSearchController(allEntriesMock);
    var result = controller.encloseEntryDayTimes(entryUser, selectedHours);
    
    for (Entry<String> entry : result) {
        assertEquals(selectedHours.getStartTime(), entry.getStartTime());
        assertEquals(selectedHours.getEndTime(), entry.getEndTime());
    }
    
    assertEquals(14, result.size());
}

@Test
void encloseEntryDayTimes_fourEntryTwoWeekLength_shouldReturnNineEntryWithCorrectTimes(){
    var entryOne = new Entry<String>("Test");
    var entryTwo = new Entry<String>("Test");
    var entryThree = new Entry<String>("Test");
    var entryFour = new Entry<String>("Test");
    ArrayList<Entry<String>> entrylist = new ArrayList<Entry<String>>();

    entryOne.changeStartDate(LocalDate.of(2021, 6, 7));//1
    entryOne.changeEndDate(LocalDate.of(2021, 6, 7));
    entrylist.add(entryOne);
    
    entryTwo.changeStartDate(LocalDate.of(2021, 6, 8));//3
    entryTwo.changeEndDate(LocalDate.of(2021, 6, 10));
    entrylist.add(entryTwo);
    
    entryThree.changeStartDate(LocalDate.of(2021, 6,15));//3
    entryThree.changeEndDate(LocalDate.of(2021, 6, 17));
    entrylist.add(entryThree);
    
    entryFour.changeStartDate(LocalDate.of(2021, 6, 19));//2
    entryFour.changeEndDate(LocalDate.of(2021, 6, 20));
    entrylist.add(entryFour);
   
    
    var selectedHours = new Entry<String>("Selected Hours");
    selectedHours.changeStartTime(LocalTime.of(10, 0));
    selectedHours.changeEndTime(LocalTime.of(18, 0));

    var allEntriesMock = mock(ICalendarEntriesModel.class);
    
    
    var controller = new SmartSearchController(allEntriesMock);
    var result = controller.encloseEntryDayTimes(entrylist, selectedHours);
    
    for (Entry<String> entry : result) {
        assertEquals(selectedHours.getStartTime(), entry.getStartTime());
        assertEquals(selectedHours.getEndTime(), entry.getEndTime());
    }
    assertEquals(9, result.size());
}

@Test
void encloseEntryDayTimes_OneEntryOneDayLength_shouldReturnOneEntryWithCorrectOpenTimes(){
    var entryOne = new Entry<String>("Test");
    
    entryOne.changeStartDate(LocalDate.of(2021, 6, 7));//0
    entryOne.changeEndDate(LocalDate.of(2021, 6, 7));
    
    var selectedHours = new ArrayList<ArrayList<Entry<String>>>();


    var selectedHoursDay0 = new ArrayList<Entry<String>>();

    var hours = new Entry<String>("Selected Hours");
    hours.changeStartTime(LocalTime.of(10, 0));
    hours.changeEndTime(LocalTime.of(18, 0));
    selectedHoursDay0.add(hours);

    selectedHours.add(selectedHoursDay0);


    var allEntriesMock = mock(ICalendarEntriesModel.class);
  
    var controller = new SmartSearchController(allEntriesMock);
    var result = controller.encloseEntryDayTimes(entryOne, selectedHours);

    assertEquals(LocalTime.of(10, 00), result.get(0).getStartTime());
    assertEquals(LocalTime.of(18, 00), result.get(0).getEndTime());
    
    assertEquals(1, result.size());
}

@Test
void encloseEntryDayTimes_OneEntryOneDayLength_shouldReturnTwoEntryWithCorrectOpenTimes(){
    var entryOne = new Entry<String>("Test");
    
    entryOne.changeStartDate(LocalDate.of(2021, 6, 7));//0
    entryOne.changeEndDate(LocalDate.of(2021, 6, 7));
    
    var selectedHours = new ArrayList<ArrayList<Entry<String>>>();


    var selectedHoursDay0 = new ArrayList<Entry<String>>();

    var hours = new Entry<String>("Selected Hours");
    hours.changeStartTime(LocalTime.of(10, 0));
    hours.changeEndTime(LocalTime.of(14, 0));
    selectedHoursDay0.add(hours);

    var hours1 = new Entry<String>("Selected Hours");
    hours1.changeStartTime(LocalTime.of(15, 0));
    hours1.changeEndTime(LocalTime.of(20, 0));
    selectedHoursDay0.add(hours1);

    selectedHours.add(selectedHoursDay0);


    var allEntriesMock = mock(ICalendarEntriesModel.class);
  
    var controller = new SmartSearchController(allEntriesMock);
    var result = controller.encloseEntryDayTimes(entryOne, selectedHours);

    assertEquals(LocalTime.of(10, 00), result.get(0).getStartTime());
    assertEquals(LocalTime.of(14, 00), result.get(0).getEndTime());
    
    assertEquals(LocalTime.of(15, 00), result.get(1).getStartTime());
    assertEquals(LocalTime.of(20, 00), result.get(1).getEndTime());
    
    assertEquals(2, result.size());
}

@Test
void encloseEntryDayTimes_OneEntryTwoDayLength_shouldReturnThreeEntryWithCorrectOpenTimes(){
    var entryOne = new Entry<String>("Test");
    
    entryOne.changeStartDate(LocalDate.of(2021, 6, 7));//0
    entryOne.changeEndDate(LocalDate.of(2021, 6, 8));//1
    
    var selectedHours = new ArrayList<ArrayList<Entry<String>>>();


    var selectedHoursDay0 = new ArrayList<Entry<String>>();

    var hours = new Entry<String>("Selected Hours");
    hours.changeStartTime(LocalTime.of(10, 0));
    hours.changeEndTime(LocalTime.of(14, 0));
    selectedHoursDay0.add(hours);

    var hours1 = new Entry<String>("Selected Hours");
    hours1.changeStartTime(LocalTime.of(15, 0));
    hours1.changeEndTime(LocalTime.of(20, 0));
    selectedHoursDay0.add(hours1);

    selectedHours.add(selectedHoursDay0);


    var selectedHoursDay1 = new ArrayList<Entry<String>>();

    var hours2 = new Entry<String>("Selected Hours");
    hours2.changeStartTime(LocalTime.of(10, 0));
    hours2.changeEndTime(LocalTime.of(14, 0));
    selectedHoursDay1.add(hours2);

    selectedHours.add(selectedHoursDay1);


    var allEntriesMock = mock(ICalendarEntriesModel.class);
  
    var controller = new SmartSearchController(allEntriesMock);
    var result = controller.encloseEntryDayTimes(entryOne, selectedHours);

    assertEquals(LocalTime.of(10, 00), result.get(0).getStartTime());
    assertEquals(LocalTime.of(14, 00), result.get(0).getEndTime());
    
    assertEquals(LocalTime.of(15, 00), result.get(1).getStartTime());
    assertEquals(LocalTime.of(20, 00), result.get(1).getEndTime());

    assertEquals(LocalTime.of(10, 00), result.get(2).getStartTime());
    assertEquals(LocalTime.of(14, 00), result.get(2).getEndTime());
    
    assertEquals(3, result.size());
}


@Test
void encloseEntryDayTimes_OneEntryTwoDayLength_shouldReturnTwoEntryWithCorrectOpenTimes(){
    var entryOne = new Entry<String>("Test");
    
    entryOne.changeStartDate(LocalDate.of(2021, 6, 7));//0
    entryOne.changeEndDate(LocalDate.of(2021, 6, 8));//1
    
    var selectedHours = new ArrayList<ArrayList<Entry<String>>>();


    var selectedHoursDay0 = new ArrayList<Entry<String>>();

    var hours = new Entry<String>("Selected Hours");
    hours.changeStartTime(LocalTime.of(10, 0));
    hours.changeEndTime(LocalTime.of(14, 0));
    selectedHoursDay0.add(hours);

    var hours1 = new Entry<String>("Selected Hours");
    hours1.changeStartTime(LocalTime.of(15, 0));
    hours1.changeEndTime(LocalTime.of(20, 0));
    selectedHoursDay0.add(hours1);

    selectedHours.add(selectedHoursDay0);


    var selectedHoursDay1 = new ArrayList<Entry<String>>();
    //Day 1 is Empty

    selectedHours.add(selectedHoursDay1);


    var allEntriesMock = mock(ICalendarEntriesModel.class);
  
    var controller = new SmartSearchController(allEntriesMock);
    var result = controller.encloseEntryDayTimes(entryOne, selectedHours);

    assertEquals(LocalTime.of(10, 00), result.get(0).getStartTime());
    assertEquals(LocalTime.of(14, 00), result.get(0).getEndTime());
    
    assertEquals(LocalTime.of(15, 00), result.get(1).getStartTime());
    assertEquals(LocalTime.of(20, 00), result.get(1).getEndTime());
    
    assertEquals(2, result.size());
}

@Test
void encloseEntryDayTimes_OneEntryTwoDayLength_OneDayHoursEmpty_shouldReturnTwoEntryWithCorrectOpenTimes(){
    var entryOne = new Entry<String>("Test");
    
    entryOne.changeStartDate(LocalDate.of(2021, 6, 7));//0
    entryOne.changeEndDate(LocalDate.of(2021, 6, 8));//1
    
    var selectedHours = new ArrayList<ArrayList<Entry<String>>>();


    var selectedHoursDay0 = new ArrayList<Entry<String>>();

    var hours = new Entry<String>("Selected Hours");
    hours.changeStartTime(LocalTime.of(10, 0));
    hours.changeEndTime(LocalTime.of(14, 0));
    selectedHoursDay0.add(hours);

    var hours1 = new Entry<String>("Selected Hours");
    hours1.changeStartTime(LocalTime.of(15, 0));
    hours1.changeEndTime(LocalTime.of(20, 0));
    selectedHoursDay0.add(hours1);

    selectedHours.add(selectedHoursDay0);
    //Day 1 is null
    selectedHours.add(null);




    var allEntriesMock = mock(ICalendarEntriesModel.class);
  
    var controller = new SmartSearchController(allEntriesMock);
    var result = controller.encloseEntryDayTimes(entryOne, selectedHours);

    assertEquals(LocalTime.of(10, 00), result.get(0).getStartTime());
    assertEquals(LocalTime.of(14, 00), result.get(0).getEndTime());
    
    assertEquals(LocalTime.of(15, 00), result.get(1).getStartTime());
    assertEquals(LocalTime.of(20, 00), result.get(1).getEndTime());
    
    assertEquals(2, result.size());
}
//End encloseEntryDayTimes

//Start modifySelectedHours
@Test
void modifySelectedHours_OneEntryOneDayLength_shouldReturnOneEntryWithCorrectTimes(){
    var  entry = new Entry<String>("User Preference");
    entry.changeStartDate(LocalDate.of(2021, 6, 7));
    entry.changeStartTime(LocalTime.of(16, 00));
    entry.changeEndDate(LocalDate.of(2021, 6, 7));
    entry.changeEndTime(LocalTime.of(20, 00));

    

    var allEntriesMock = mock(ICalendarEntriesModel.class);
    
    
    var controller = new SmartSearchController(allEntriesMock);
    var result = controller.modifySelectedHours(entry, 60, 60);
    
    
    assertEquals(LocalTime.of(15, 00), result.getStartTime());
    assertEquals(LocalTime.of(21, 00), result.getEndTime());
}


@Test
void modifySelectedHours_TwoEntryOneTwoLength_shouldReturnTwoEntryWithCorrectTimes(){
    
    var input = new ArrayList<Entry<String>>();
    var  entry = new Entry<String>("User Preference");
    entry.changeStartDate(LocalDate.of(2021, 6, 7));
    entry.changeStartTime(LocalTime.of(16, 00));
    entry.changeEndDate(LocalDate.of(2021, 6, 7));
    entry.changeEndTime(LocalTime.of(20, 00));
    input.add(entry);

    var  entry2 = new Entry<String>("User Preference");
    entry2.changeStartDate(LocalDate.of(2021, 6, 8));
    entry2.changeStartTime(LocalTime.of(16, 00));
    entry2.changeEndDate(LocalDate.of(2021, 6, 8));
    entry2.changeEndTime(LocalTime.of(20, 00));
    input.add(entry2);

    var allEntriesMock = mock(ICalendarEntriesModel.class);
    
    
    var controller = new SmartSearchController(allEntriesMock);
    var result = controller.modifySelectedHours(input, 60, 60);
    
    for (Entry<String> resultEntry : result) {
        assertEquals(LocalTime.of(15, 00), resultEntry.getStartTime());
        assertEquals(LocalTime.of(21, 00), resultEntry.getEndTime());
    }
    
}

@Test
void modifySelectedHours_TwoEntryOneTwoLength_shouldReturnFourEntryWithCorrectTimes(){
    
    var input = new ArrayList<Entry<String>>();
    var  entry = new Entry<String>("User Preference");
    entry.changeStartDate(LocalDate.of(2021, 6, 7));
    entry.changeStartTime(LocalTime.of(16, 00));
    entry.changeEndDate(LocalDate.of(2021, 6, 7));
    entry.changeEndTime(LocalTime.of(20, 00));
    input.add(entry);

    var  entry2 = new Entry<String>("User Preference");
    entry2.changeStartDate(LocalDate.of(2021, 6, 8));
    entry2.changeStartTime(LocalTime.of(16, 00));
    entry2.changeEndDate(LocalDate.of(2021, 6, 8));
    entry2.changeEndTime(LocalTime.of(20, 00));
    input.add(entry2);


    var input1 = new ArrayList<Entry<String>>();
    var  entry3 = new Entry<String>("User Preference");
    entry3.changeStartDate(LocalDate.of(2021, 6, 7));
    entry3.changeStartTime(LocalTime.of(16, 00));
    entry3.changeEndDate(LocalDate.of(2021, 6, 7));
    entry3.changeEndTime(LocalTime.of(20, 00));
    input1.add(entry3);

    var  entry4 = new Entry<String>("User Preference");
    entry4.changeStartDate(LocalDate.of(2021, 6, 8));
    entry4.changeStartTime(LocalTime.of(16, 00));
    entry4.changeEndDate(LocalDate.of(2021, 6, 8));
    entry4.changeEndTime(LocalTime.of(20, 00));
    input.add(entry4);

    

    ArrayList<ArrayList<Entry<String>>> input2 = new ArrayList<ArrayList<Entry<String>>>();
    input2.add(input);
    input2.add(input1);
    var allEntriesMock = mock(ICalendarEntriesModel.class);
    
    
    var controller = new SmartSearchController(allEntriesMock);
    var result = controller.modifySelectedHoursList(input2, 60, 60);
    int numberResults = 0;
    for (ArrayList<Entry<String>> resultEntry : result) {
        for (Entry<String> ent : resultEntry) {
            assertEquals(LocalTime.of(15, 00), ent.getStartTime());
            assertEquals(LocalTime.of(21, 00), ent.getEndTime());
            numberResults++;
        }  
    }
    assertEquals(4, numberResults);
    
}
//End modifySelectedHours


@Test
void reduceListLength_TwoEntry_shouldReturnOneEntry(){
    
    var input = new ArrayList<Entry<String>>();
    for (int i = 0; i < 10; i++) {
        input.add(null);
    }
    var allEntriesMock = mock(ICalendarEntriesModel.class);

    var controller = new SmartSearchController(allEntriesMock);
    var result = controller.reduceListLength(input, 5);
    
    assertEquals(5, result.size());
    
}   
}
