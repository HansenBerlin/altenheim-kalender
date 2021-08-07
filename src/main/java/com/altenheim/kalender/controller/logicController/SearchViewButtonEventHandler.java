package com.altenheim.kalender.controller.logicController;

import com.altenheim.kalender.resourceClasses.DateFormatConverter;
import com.altenheim.kalender.interfaces.*;
import com.altenheim.kalender.models.SettingsModel;
import com.calendarfx.model.Entry;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

public class SearchViewButtonEventHandler extends SearchViewValidationController
{
    private IMailCreationController mailCreationController;
    private IPopupViewController popupViewController;
    private IEntryFactory entryFactory;
    
    public SearchViewButtonEventHandler(IGoogleAPIController api, ICalendarEntriesModel allCalendars, 
        IMailCreationController mailCreationController, IPopupViewController popupViewController, IEntryFactory entryFactory) 
    {
        super(api, allCalendars);
        this.mailCreationController = mailCreationController;
        this.popupViewController = popupViewController;
        this.entryFactory = entryFactory;
    }

    public Button createSendMailButton()
    {
        var button = new Button("Mailanfrage");
        if (toggleUseMailTemplate.isSelected())  
        {
            button.setVisible(true); 
            registerButtonSendMailEvent(button);
        }      
        else
            button.setVisible(false);

        return button;
    }

    public Button createAddEntryButton(Entry<String> currSug)
    {
        var button = new Button("EINTRAGEN");
        String startDate = DateFormatConverter.formatDate(currSug.getStartDate());
        String endDate = DateFormatConverter.formatDate(currSug.getEndDate());
        String startTime = DateFormatConverter.formatTime(currSug.getStartTime());
        String endTime = DateFormatConverter.formatTime(currSug.getEndTime());
        String title = currSug.getTitle();
        var sendMailButton = createSendMailButton();

        button.setOnAction(new EventHandler<ActionEvent>() 
        {
            public void handle(ActionEvent e) 
            {
                if (toggleUseTravelDuration.isSelected() == false)
                    travelTimeTo = 0; 
                createEntryIncludingTravelTimes(currSug);                           
                                  
                popupViewController.showEntryAddedDialogWithMailOption(startDate, endDate, startTime, endTime, title, sendMailButton);
            }
        }); 
        return button;      
    }

    public void registerButtonSendMailEvent(Button button)
    {
        button.setOnAction(new EventHandler<ActionEvent>() 
        {
            public void handle(ActionEvent e) 
            {                
                String templateName = dropdownMailTemplates.getValue();
                String recipient = validateRecipient();
                String date = DateFormatConverter.formatDate(currentSuggestion.getStartDate());
                String time = DateFormatConverter.formatTime(currentSuggestion.getStartTime());
                mailCreationController.processMailWrapper(templateName, date, time, recipient);                               
            }
        });
    }

    public void createEntryIncludingTravelTimes(Entry<String> currentSuggestion) 
    {
        int traveltime = 0;
        if (toggleUseTravelDuration.isSelected()) 
        {
            traveltime = travelTimeTo;
        }
        String defaultCalendarName = SettingsModel.defaultCalendarForSearchView;
        entryFactory.createNewUserEntryIncludingTravelTimes(currentSuggestion.getStartDate(),
                currentSuggestion.getEndDate(), currentSuggestion.getStartTime().plusMinutes(timeBeforeGlobal),
                currentSuggestion.getEndTime().minusMinutes(timeAfterGlobal), tfAppointmentName.getText(),
                 traveltime, defaultCalendarName);
    } 
    
}
