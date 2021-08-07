package com.altenheim.kalender.controller.logicController;

import com.altenheim.kalender.interfaces.IGoogleAPIController;
import com.altenheim.kalender.models.*;
import com.altenheim.kalender.resourceClasses.DateFormatConverter;
import com.altenheim.kalender.controller.viewController.ResponsiveController;
import com.altenheim.kalender.interfaces.*;
import org.controlsfx.control.ToggleSwitch;
import java.time.*;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.calendarfx.model.Entry;
import com.calendarfx.view.TimeField;

public class SearchViewValidationController extends ResponsiveController
{
    private IGoogleAPIController api;
    protected ICalendarEntriesModel allCalendars;
    private IPopupViewController popupViewController;
    private IMailCreationController mailCreationController;
    protected IEntryFactory entryFactory;

    public SearchViewValidationController(IGoogleAPIController api, ICalendarEntriesModel allCalendars, IPopupViewController popupViewController,
        IMailCreationController mailCreationController, IEntryFactory entryFactory)
    {
        this.api = api;
        this.allCalendars = allCalendars;
        this.popupViewController = popupViewController;
        this.mailCreationController = mailCreationController;
        this.entryFactory = entryFactory;
    }

    @FXML protected ToggleSwitch toggleDateRange, toggleTimeRange, toggleWeekdays, toggleCalendars;   
    @FXML protected ToggleSwitch toggleUseTravelDuration, toggleUseOpeningHours, toggleUseMargin, toggleRecurringDate, toggleAddAutomatically, toggleUseMailTemplate; 
    @FXML protected CheckBox tickMonday, tickTuesday, tickWednesday, tickThursday, tickFriday, tickSaturday, tickSunday;  
    @FXML protected Slider sliderDurationHours, sliderDurationMinutes, sliderMarginBeforeAppointment, sliderRecurrences, sliderMarginAfterAppointment;
    @FXML protected HBox containerCalendars;
    @FXML protected TimeField timeStart, timeEnd; 
    @FXML protected DatePicker startDate, endDate;
    @FXML protected TextField tfAppointmentName;
    @FXML protected Text txtHeaderStep, txtFirstStep, txtSecondStep, txtThirdStep;
    @FXML protected TextField tfDurationMinutes, tfDurationHours;    
    @FXML protected Button btnBack, btnConfirm, btnReset;     
    @FXML protected VBox stepOneUserInput, stepTwoUserInput, stepThreeUserInput;
    @FXML protected HBox containerDateRange, containerTimeRange, containerWeekdays, containerMailTemplate;
    @FXML protected HBox containerTravel, containerOpeningHours, containerMargin, containerReccurrence;
    @FXML protected Circle imgFirstStep, imgSecondStep, imgThirdStep;
    @FXML protected RowConstraints firstRow; 
    

    protected ComboBox<String> dropdownVehicle, dropdownStartAtDest, dropdownEndAtDest, dropdownInterval, dropdownDestinationOpening, dropdownMailTemplates, dropDownContact;
    protected SplitMenuButton calendarSelection = new SplitMenuButton();
    protected int recurrences = 1;   
    protected int timeAfterGlobal = 0;
    protected int timeBeforeGlobal = 0;
    protected int travelTimeTo = 0;
    protected LocalDateTime timeToStartSearch;
    protected Entry<String> currentSuggestion;
    protected ArrayList<Entry<String>> currentSuggestions; 

    @FXML
    public void initialize()
    {
    }
    
    
    protected void validateCalendarSelectionInput()
    {
        allCalendars.clearCalendarsSelectedByUser();
        if (toggleCalendars.isSelected())
        {
            var allAvailaibleCalendars = allCalendars.getAllCalendars();
            for (var calendar : allAvailaibleCalendars)
            {
                if (calendar.getName().equals(SettingsModel.defaultCalendarForSearchView))
                {
                    allCalendars.addToAllCalendarsSelectedByUser(calendar);
                    return;
                }
                allCalendars.addToAllCalendarsSelectedByUser(allAvailaibleCalendars.get(0));
            }            
        }
        else
        {
            var tickBoxesCalendar = calendarSelection.getItems();
            for (var menuItem : tickBoxesCalendar) 
            {
                var checkbox = (CheckBox)menuItem.getGraphic();
                if (checkbox.isSelected())
                {
                    allCalendars.addToAllCalendarsSelectedByUserByCalendarName(checkbox.getText());
                }                
            }
        }
    }

    protected final void clearFields()
    {
        currentSuggestion = null;
        currentSuggestions = null;
        timeToStartSearch = null;
        recurrences = 1;
        timeAfterGlobal = 0;
        timeBeforeGlobal = 0;
        travelTimeTo = 0;   
        SuggestionsModel.data.clear();
        resetToggleStates();
        resetSliderStates();
        setDateAndTimeFields();
        containerCalendars.getChildren().clear(); 
        dropdownStartAtDest.getEditor().setText("");
        dropdownEndAtDest.getEditor().setText("");
        dropdownDestinationOpening.getEditor().setText("");
         
    }

    private void resetToggleStates()
    {
        toggleUseTravelDuration.setSelected(false);
        toggleUseOpeningHours.setSelected(false);
        toggleUseMargin.setSelected(false);
        toggleRecurringDate.setSelected(false);
        toggleAddAutomatically.setSelected(true); 
        toggleDateRange.setSelected(true);
        toggleTimeRange.setSelected(true);
        toggleWeekdays.setSelected(true);
        toggleUseMailTemplate.setSelected(false);
        toggleCalendars.setSelected(true);        
    }

    private void resetSliderStates()
    {
        Slider[] allSliders = { sliderDurationHours, sliderDurationMinutes, sliderMarginBeforeAppointment, sliderRecurrences, sliderMarginAfterAppointment };
        CheckBox[] allTicks = { tickMonday, tickTuesday, tickWednesday, tickThursday, tickFriday, tickSaturday, tickSunday };

        for (var checkBox : allTicks) 
        {
            checkBox.setSelected(false);            
        }

        for (var slider : allSliders)
        {
            slider.setValue(0);
        }
    }

    protected void setDateAndTimeFields()
    {
        timeStart.setValue(LocalTime.of(0, 0));
        timeEnd.setValue(LocalTime.of(23, 59)); 
        startDate.setValue(LocalDate.now());
        endDate.setValue(LocalDate.now().plusDays(365));
    }

    protected int validateDuration() 
    {
        int duration = (int)sliderDurationMinutes.getValue() + (int)sliderDurationHours.getValue() * 60;
        if (duration < 15)
            duration = 15;
        return duration;
    }

    protected LocalDate[] validateDateInput() 
    {
        var startDateInput = startDate.getValue();
        var endDateDateInput = endDate.getValue();

        if (startDateInput == null || toggleDateRange.isSelected())
            startDateInput = LocalDate.now();
        if (endDateDateInput == null || toggleDateRange.isSelected())
            endDateDateInput = LocalDate.now().plusDays(1500);
        return new LocalDate[] { startDateInput, endDateDateInput };         
    }

    protected LocalTime[] validateTimeInput() 
    {
        var startTimeInput = timeStart.getValue();
        var endTimeInput = timeEnd.getValue();

        if (toggleTimeRange.isSelected()) {
            startTimeInput = LocalTime.of(0, 0, 0);
            endTimeInput = LocalTime.of(23, 59, 59);
        }
        return new LocalTime[] { startTimeInput, endTimeInput };
    }

    protected boolean[] validateWeekdays() 
    {
        if (toggleWeekdays.isSelected() == false) 
        {
            return new boolean[] { tickMonday.isSelected(), tickTuesday.isSelected(), tickWednesday.isSelected(),
                    tickThursday.isSelected(), tickFriday.isSelected(), tickSaturday.isSelected(),
                    tickSunday.isSelected() };
        } else
            return new boolean[] { true, true, true, true, true, true, true };
    } 
    
    protected int validateTravelTime()
    {
        var origin = dropdownStartAtDest.getSelectionModel().getSelectedItem();
        var destination = dropdownEndAtDest.getSelectionModel().getSelectedItem();
        int travelTime = 0;

        if (toggleUseTravelDuration.isSelected() && origin.isEmpty() == false && destination.isEmpty() == false) {
            var response = api.searchForDestinationDistance(origin, destination, getApiStringFromInput());
            travelTime = updateTravelTimeToMinutes(response[0]);
        }
        return travelTime;
    }    

    protected  HashMap<DayOfWeek, List<Entry<String>>> validateOpeningHours() 
    {
        var openingHours = new HashMap<DayOfWeek, List<Entry<String>>>();
        var destination = dropdownEndAtDest.getSelectionModel().getSelectedItem();

        if (toggleUseOpeningHours.isSelected() && destination.isEmpty() == false)
            openingHours = api.getOpeningHours(destination);

        return openingHours;
    }

    protected int[] compareTimes(int timeBefore, int timeAfter, int travelTime) 
    {
        int[] updatedTimes = new int[2];
        if (timeBefore > travelTime)
            updatedTimes[0] = timeBefore;
        else
            updatedTimes[0] = travelTime;
        if (timeAfter > travelTime)
            updatedTimes[1] = timeAfter;
        else
            updatedTimes[1] = travelTime;
        return updatedTimes;
    }

    protected int calculateInterval() 
    {
        var userInput = dropdownInterval.getSelectionModel().getSelectedItem();
        if (userInput == null)
            return 0;
        int returnValue = switch (userInput) {
            case "täglich" -> 1;
            case "wöchentlich" -> 7;
            case "monatlich" -> 30;
            case "halbjährlich" -> 182;
            case "jährlich" -> 365;
            default -> 0;
        };
        return returnValue;
    }    

    protected int validateReccurrences() 
    {
        if (toggleRecurringDate.isSelected())
            return sliderRecurrences.valueProperty().intValue();
        else
            return 1;
    }

    protected String validateRecipient()
    {
        String selectedContact = dropDownContact.getValue();
        for (var contact : ContactModel.data) 
        {
            if (contact.getFullName().equals(selectedContact));
                return contact.getMail();            
        }
        return "";
    }

    protected void changeContentPosition(double width, double height) 
    {

    }

    private String getApiStringFromInput() 
    {
        String input = dropdownVehicle.getSelectionModel().getSelectedItem();
        if (input == null)
            return "";

        String returnValue = switch (dropdownVehicle.getSelectionModel().getSelectedItem()) {
            case "Fußgänger" -> "walking";
            case "Fahrrad" -> "bicycling";
            case "Öffis" -> "transit";
            case "Auto" -> "driving";
            default -> "";
        };
        return returnValue;
    }

    private int updateTravelTimeToMinutes(int travelTime) 
    {
        if (travelTime != 0)
            travelTime = travelTime / 60;
        return travelTime;
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
