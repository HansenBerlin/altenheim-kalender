package com.altenheim.kalender.controller.logicController;

import java.time.LocalTime;
import java.time.LocalDateTime;
import com.altenheim.kalender.interfaces.*;
import com.altenheim.kalender.interfaces.models.CalendarEntriesModel;
import com.altenheim.kalender.models.SuggestionsModel;

public class SearchViewRequestHandlerController extends SearchViewValidationController
{
    private ISmartSearchController smartSearch;
    private IDateSuggestionController dateSuggestionController;
    
    public SearchViewRequestHandlerController(IGoogleAPIController api, CalendarEntriesModel allCalendars,
                                              IPopupViewController popupViewController, IMailCreationController mailCreationController,
                                              EntryFactory entryFactory, ISmartSearchController smartSearch, IDateSuggestionController dateSuggestionController)
    {
        super(api, allCalendars, popupViewController, mailCreationController, entryFactory);
        this.smartSearch = smartSearch;
        this.dateSuggestionController = dateSuggestionController;
    }

    public void iterateThroughSuggestions() 
    {
        if (toggleAddAutomatically.isSelected()) 
        {
            automaticEntryCreation();
            return;
        }        

        int duration = validateDuration() + timeBeforeGlobal + timeAfterGlobal;
        SuggestionsModel.data.clear();

        for (int i = 0; i < 20; i++)         
        {
            currentSuggestion = dateSuggestionController.getDateSuggestionFromEntryList(currentSuggestions, timeToStartSearch, duration);
            
            if (currentSuggestion == null)
                return; 
            
            var buttonAdd = createAddEntryButton(currentSuggestion);
            timeToStartSearch = currentSuggestion.getEndAsLocalDateTime().minusMinutes(timeAfterGlobal); 
            SuggestionsModel.addToList(currentSuggestion.getStartTime().plusMinutes(timeBeforeGlobal), 
                currentSuggestion.getEndTime().minusMinutes(timeAfterGlobal), 
                currentSuggestion.getStartDate(), currentSuggestion.getEndDate(), 
                buttonAdd, tfAppointmentName.getText());
        } 
    } 
    
    public void automaticEntryCreation()
    {
        int duration = validateDuration() + timeBeforeGlobal + timeAfterGlobal;
        int interval = calculateInterval();
        if (interval == 0)
            interval = 1;        
        LocalTime startAt = timeToStartSearch.toLocalTime();                

        while (recurrences > 0) 
        {
            currentSuggestion = dateSuggestionController.getDateSuggestionFromEntryList(currentSuggestions, timeToStartSearch, duration);
            if (currentSuggestion == null)
                return;
            
            if (interval > 0)            
                timeToStartSearch = LocalDateTime.of(timeToStartSearch.toLocalDate().plusDays(interval), startAt);            
            else            
                timeToStartSearch = currentSuggestion.getEndAsLocalDateTime();
            
            var sendMailButton = createSendMailButton();
            createEntryIncludingTravelTimes(currentSuggestion);                
            SuggestionsModel.addToList(currentSuggestion.getStartTime().plusMinutes(timeBeforeGlobal), 
                currentSuggestion.getEndTime().minusMinutes(timeAfterGlobal), currentSuggestion.getStartDate(), 
                currentSuggestion.getEndDate(), sendMailButton, tfAppointmentName.getText());                
            recurrences--;            
        } 
    }     

    public void startRequest() 
    {
        var validatedDates = validateDateInput();
        var startDateInput = validatedDates[0];
        var endDateDateInput = validatedDates[1];
        var validatedTimes = validateTimeInput();
        var startTimeInput = validatedTimes[0];
        var endTimeInput = validatedTimes[1];
        var userPrefs = entryFactory.createUserEntry(startDateInput, endDateDateInput, startTimeInput, endTimeInput);
        int duration = validateDuration();
        int travelTime = validateTravelTime();
        var openingHours = validateOpeningHours();
        int timeBefore = (int) sliderMarginBeforeAppointment.getValue();
        int timeAfter = (int) sliderMarginAfterAppointment.getValue();
        var updatedTimes = compareTimes(timeBefore, timeAfter, travelTime);
        var weekdays = validateWeekdays();        
        int intervalDays = calculateInterval();   
        validateCalendarSelectionInput();    
        
        currentSuggestions = smartSearch.findPossibleTimeSlots(userPrefs, duration, weekdays, 
            openingHours, updatedTimes[0], updatedTimes[1], intervalDays);
        
        recurrences = validateReccurrences();  
        timeBeforeGlobal = updatedTimes[0];      
        timeAfterGlobal = updatedTimes[1];
        timeToStartSearch = LocalDateTime.of(startDateInput, startTimeInput); 
        travelTimeTo = travelTime;
    }    
}
