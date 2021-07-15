package com.altenheim.kalender.controller.viewController;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import com.altenheim.kalender.models.*;
import com.altenheim.kalender.resourceClasses.ComboBoxCreate;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.altenheim.kalender.interfaces.*;
import com.calendarfx.model.Entry;
import com.calendarfx.view.TimeField;
import org.controlsfx.control.ToggleSwitch;


public class SearchViewController extends ResponsiveController
{
    @FXML private RowConstraints firstRow;
    @FXML private Text txtHeaderStep, txtFirstStep, txtSecondStep, txtThirdStep;
    @FXML private TextField tfAppointmentName, tfDurationMinutes, tfDurationHours;
    @FXML private Button btnBack, btnConfirm, btnNextSuggestion;    
    @FXML private VBox stepOneUserInput, stepTwoUserInput, stepThreeUserInput;
    @FXML private DatePicker startDate, endDate;    
    @FXML private CheckBox tickMonday, tickTuesday, tickWednesday, tickThursday, tickFriday, tickSaturday, tickSunday;  
    @FXML private TimeField timeStart, timeEnd; 

    @FXML private ToggleSwitch toggleDateRange, toggleTimeRange, toggleWeekdays;   
    @FXML private HBox containerDateRange, containerTimeRange, containerWeekdays;
    @FXML private HBox containerTravel, containerOpeningHours, containerMargin, containerReccurrence;
    @FXML private ToggleSwitch toggleUseTravelDuration, toggleUseOpeningHours, toggleUseMargin, toggleRecurringDate, toggleAutoSuggest, toggleAddAutomatically;  

    @FXML private Slider sliderDurationHours, sliderDurationMinutes, sliderMarginBeforeAppointment, sliderRecurrences, sliderMarginAfterAppointment;
    @FXML private Spinner<Integer> sliderSuggestionCount;  
    @FXML private Circle imgFirstStep, imgSecondStep, imgThirdStep;

    private ComboBox<String> dropdownVehicle, dropdownStartAtDest, dropdownEndAtDest, dropdownInterval, dropdownDestinationOpening;

    private ISmartSearchController smartSearch;
    private IEntryFactory entryFactory;
    private IContactFactory contactFactory;
    private IGoogleAPIController api;
    private IIOController iOController;
    private IAnimationController animationController;
    private IComboBoxFactory comboBoxFactory;
    private IDateSuggestionController dateSuggestionController;
    private List<ContactModel> contacts;
    private List<MailTemplateModel> mailTemplates;
    private SettingsModel settings;
    private ArrayList<SerializableEntry> currentSuggestions;
    private int userStep = 1;

  

    public SearchViewController(ISmartSearchController smartSearch, IEntryFactory entryFactory, List<ContactModel> contacts, 
        IContactFactory contactFactory, List<MailTemplateModel> mailTemplates, SettingsModel settings, IGoogleAPIController api, 
        IIOController iOController, IAnimationController animationController, IComboBoxFactory comboBoxFactory, IDateSuggestionController dateSuggestionController)
    {
        this.smartSearch = smartSearch;
        this.entryFactory = entryFactory;
        this.contacts = contacts;
        this.contactFactory = contactFactory;
        this.mailTemplates = mailTemplates;
        this.settings = settings;
        this.api = api;
        this.iOController = iOController;
        this.animationController = animationController;
        this.comboBoxFactory = comboBoxFactory;
        this.dateSuggestionController = dateSuggestionController;
    }

    @FXML
    private void initialize()
    {
        TableView<SuggestionsModel> tableSuggestions = createTable();
        stepThreeUserInput.getChildren().add(tableSuggestions);
        setupInitialContainerStates();
        setupToggleBindings();
        setupTextboxInputValidation();
        createComboBoxes();
        //setupSliderBindings();
        
    }

    private void createComboBoxes()
    {
        dropdownVehicle = comboBoxFactory.create(ComboBoxCreate.VEHICLES);
        dropdownStartAtDest = comboBoxFactory.create(ComboBoxCreate.START);
        dropdownEndAtDest = comboBoxFactory.create(ComboBoxCreate.DESTINATION);
        dropdownDestinationOpening = comboBoxFactory.create(ComboBoxCreate.DESTINATION);
        dropdownInterval = comboBoxFactory.create(ComboBoxCreate.RECCURENCEOPTIONS);

        containerTravel.getChildren().addAll(dropdownVehicle, dropdownStartAtDest, dropdownEndAtDest);  
        containerOpeningHours.getChildren().add(dropdownDestinationOpening);
        containerReccurrence.getChildren().add(dropdownInterval);

        dropdownEndAtDest.getEditor().textProperty().bindBidirectional(dropdownDestinationOpening.getEditor().textProperty());
    }

    

    private void setupSliderBindings()
    {                
        tfDurationMinutes.textProperty().bind(sliderDurationMinutes.valueProperty().asString());
        tfDurationHours.textProperty().bind(sliderDurationHours.valueProperty().asString());
    }

    private void setupInitialContainerStates()
    {
        HBox[] containers = { containerDateRange, containerTimeRange, containerWeekdays, containerTravel, 
            containerOpeningHours, containerMargin, containerReccurrence };

        for (var hBox : containers) 
        {
            hBox.setScaleX(0);
            hBox.setScaleY(0);            
        }
    }

    private void setupToggleBindings()
    {
        ToggleSwitch[] toggles = { toggleDateRange, toggleTimeRange, toggleWeekdays, toggleUseTravelDuration, 
            toggleUseOpeningHours, toggleUseMargin, toggleRecurringDate };
        HBox[] containers = { containerDateRange, containerTimeRange, containerWeekdays, containerTravel, 
            containerOpeningHours, containerMargin, containerReccurrence };

        int i = 0;

        while (i < toggles.length) 
        {
            final int j = i;
            toggles[j].selectedProperty().addListener(((observable, oldValue, newValue) -> 
            {
                Boolean valueToSet;
                {
                    if (j > 2 && j < 7)
                        valueToSet = newValue;
                    else
                        valueToSet = oldValue;
                }
                animationController.growAndShrinkContainer(containers[j], valueToSet);
            }));
            i++;            
        }
    }

    private void setupTextboxInputValidation()
    {
        TextField[] textFieldsFirstView = { tfDurationMinutes, tfDurationHours };
        Slider[] sliderFirstView = { sliderDurationMinutes, sliderDurationHours };

        int i = 0;

        while (i < 2) 
        {
            final int j = i;            

            textFieldsFirstView[j].textProperty().addListener(new ChangeListener<String>() 
            {
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) 
                {
                    if (!newValue.matches("\\d*"))
                    {
                        textFieldsFirstView[j].setText(newValue.replaceAll("[^\\d]", ""));                    
                    }  
                     else
                    {
                        double value = Double.parseDouble(textFieldsFirstView[j].getText());
                        sliderFirstView[j].setValue(Double.valueOf(value));
                    }               
                }
            });
        i++;            
        };
    }

    @FXML
    private void updateUserStepView(ActionEvent event) 
    {
        String[] headings = {"Basisinformationen" , "Optionale Informationen", "Vorschlagsauswahl" };
        Circle[] images = { imgFirstStep, imgSecondStep, imgThirdStep };
        VBox[] allSteps = { stepOneUserInput, stepTwoUserInput, stepThreeUserInput }; 
        int incrementor = -1;
        var button = (Button)event.getSource();

        if (button.equals(btnConfirm))
        {
            if (userStep == 3)
            {
                startRequest();
                return;
            }
            incrementor = 1;
        }
        else if (button.equals(btnBack) && userStep == 1)        
            return;  
        else if (button.equals(btnNextSuggestion) && userStep == 3)
        {
            getNextSuggestion();
            return;  
        }       
        
        int currentIndex = userStep - 1;
        int requestedIndex = userStep - 1 + incrementor;
        changeViewState(allSteps[currentIndex], allSteps[requestedIndex], images[currentIndex], images[requestedIndex]); 
        userStep += incrementor; 
        txtHeaderStep.setText(headings[currentIndex]);
    }
    
    private int suggestions = 1;    
    private LocalDateTime timeToStartSearch;
    private SerializableEntry currentSuggestion;
    private void getNextSuggestion()
    {
        //var currentCheck = LocalDateTime.of(startDateUpdated, startTimeUpdated);
        int duration = (int)sliderDurationMinutes.getValue();
        //var newTime = currentCheck.plusMinutes(duration);
        currentSuggestion = dateSuggestionController.getDateSuggestionFromEntryList(currentSuggestions, timeToStartSearch, duration);
        var nextTimeToCheck = setNextSuggestion(currentSuggestion);
        SuggestionsModel.addToList(currentSuggestion.getStartTime(), currentSuggestion.getEndTime(), currentSuggestion.getStartDate());
        suggestions++;  
    }

    private LocalDateTime setNextSuggestion(SerializableEntry entry)
    {
        var startDateNew = startDate.getValue();
        var startTimeNew = timeStart.getValue();
        if (entry.getEndDate().isAfter(currentSuggestion.getStartDate()))
            startDateNew = entry.getEndDate();
        else
            startTimeNew = entry.getEndTime();
        var currentCheck = LocalDateTime.of(startDateNew, startTimeNew);
        //int duration = (int)sliderDurationMinutes.getValue();
        return currentCheck;
    }

    private void startRequest()
    {       
        var openingHours = new HashMap<DayOfWeek, List<SerializableEntry>>();
        int[] travelTime = { 0, 0 };
        var userPrefs = entryFactory.createUserEntry(startDate.getValue(),
                endDate.getValue(), timeStart.getValue(), timeEnd.getValue());
        int duration = (int)sliderDurationMinutes.getValue();
        //var openingHours = new HashMap<DayOfWeek, List<SerializableEntry>>();
        var origin = dropdownStartAtDest.getEditor().getText();
        var destination = dropdownEndAtDest.getEditor().getText();
        if (isInputStringEmpty(destination) == false)
            openingHours = api.getOpeningHours(destination);
        if (isInputStringEmpty(origin) == false)
            travelTime = api.searchForDestinationDistance(origin, destination, getApiStringFromInput());
        travelTime = updateTravelTimeToMinutes(travelTime);        
        int timeBefore = (int)sliderMarginBeforeAppointment.getValue();
        int timeAfter = (int)sliderMarginAfterAppointment.getValue();
        var updatedTimes = compareTimes(timeBefore, timeAfter, travelTime[0], travelTime[1]);
        boolean[] weekdays = { tickMonday.isSelected(), tickTuesday.isSelected(), tickWednesday.isSelected(),
                tickThursday.isSelected(), tickFriday.isSelected(), tickSaturday.isSelected(), tickSunday.isSelected() };
        int suggestionsCount = 100;             
        int intervalDays = 0;
        if (toggleRecurringDate.isDisabled() == false)
        {
            suggestionsCount = sliderRecurrences.valueProperty().intValue();
            intervalDays = sliderRecurrences.valueProperty().intValue();
        }
        else if (toggleRecurringDate.isDisabled() && toggleAddAutomatically.isDisabled() == false)
            suggestionsCount = 1; 

        currentSuggestions = smartSearch.findPossibleTimeSlots(userPrefs, duration, weekdays, openingHours, 
            updatedTimes[0], updatedTimes[1], suggestionsCount, intervalDays);
        
        timeToStartSearch = LocalDateTime.of(startDate.getValue(), timeStart.getValue());
    }

    private boolean isInputStringEmpty(String content)
    {
        if (content == null || content.isEmpty())
            return true;
        else
            return false;
    }

    private int[] updateTravelTimeToMinutes(int[] travelTime)
    {
        if (travelTime[0] != 0)
            travelTime[0] = travelTime[0]/60;
        if (travelTime[1] != 0)
            travelTime[1] = travelTime[1]/60;
        return travelTime;        
    }

    private int[] compareTimes(int timeBefore, int timeAfter, int travelTimeStart, int travelTimeEnd)
    {
        int[] updatedTimes = new int[2];
        if (timeBefore > travelTimeStart)
            updatedTimes[0] = timeBefore;
        else
            updatedTimes[0] = travelTimeStart;
        if (timeAfter > travelTimeEnd)
            updatedTimes[1] = timeAfter;
        else
            updatedTimes[1] = travelTimeEnd;
        return updatedTimes;
    }

    private String getApiStringFromInput()
    {
        String input = switch(dropdownVehicle.getEditor().getText()) 
        {
            case "Fußgänger" -> "walking";
            case "Fahrrad" -> "bicycling";
            case "Öffis" -> "transit";
            case "Auto" -> "driving";
            default -> ""; 
        };
        return input;
    }

    private void changeViewState(VBox deactivate, VBox activate, Circle currentC, Circle nextC)
    {
        deactivate.setDisable(true);
        deactivate.setVisible(false);
        activate.setDisable(false);
        activate.setVisible(true);
        currentC.setId("customCircleInactive");
        nextC.setId("customCircleActive");   
    }

    @FXML
    private void testUpdate(ActionEvent event) throws IOException, ClassNotFoundException
    {
        entryFactory.createRandomContactsList(100);
        iOController.saveContactsToFile();     
    }

    @FXML
    private void resetTest(ActionEvent event) throws IOException, ClassNotFoundException
    {
        SuggestionsModel.data.clear();
    }

    final void changeContentPosition(double width, double height) 
    {          
        Text[] headers = { txtFirstStep, txtSecondStep, txtThirdStep };
        Circle[] circles = { imgFirstStep, imgSecondStep, imgThirdStep };
        boolean isWindowSmall = false;
        int rowHeight = 80;
        if (height < 700)   
        {
            isWindowSmall = true; 
            rowHeight = 30;
        }           
        firstRow.setPrefHeight(rowHeight);
        firstRow.setMaxHeight(rowHeight);
        animationController.growAndShrinkCircle(circles, headers, isWindowSmall);       
    }

    private TableView<SuggestionsModel> createTable()
    {
        TableColumn<SuggestionsModel, String> startTimeColumn = new TableColumn<>("Startzeit");
        TableColumn<SuggestionsModel, String> endTimeColumn = new TableColumn<>("Endzeit");
        TableColumn<SuggestionsModel, String> dateColumn = new TableColumn<>("Datum");
        TableView<SuggestionsModel> table = new TableView<SuggestionsModel>(SuggestionsModel.data);

        startTimeColumn.setCellValueFactory(new PropertyValueFactory<SuggestionsModel, String>("startTime"));
        endTimeColumn.setCellValueFactory(new PropertyValueFactory<SuggestionsModel, String>("endTime"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<SuggestionsModel, String>("day"));

        startTimeColumn.setPrefWidth(200);
        endTimeColumn.setPrefWidth(200);
        dateColumn.setPrefWidth(200);
        table.getColumns().add(startTimeColumn);
        table.getColumns().add(endTimeColumn);
        table.getColumns().add(dateColumn);

        return table;
    }
}