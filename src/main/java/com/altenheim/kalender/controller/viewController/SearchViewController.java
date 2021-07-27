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
import com.altenheim.kalender.controller.Factories.EntryFactory;
import com.altenheim.kalender.interfaces.*;
import com.calendarfx.view.TimeField;
import org.controlsfx.control.ToggleSwitch;

public class SearchViewController extends ResponsiveController
{
    @FXML private RowConstraints firstRow;
    @FXML private Text txtHeaderStep, txtFirstStep, txtSecondStep, txtThirdStep;
    @FXML private TextField tfAppointmentName, tfDurationMinutes, tfDurationHours;
    @FXML private Button btnBack, btnConfirm, btnReset;    
    @FXML private VBox stepOneUserInput, stepTwoUserInput, stepThreeUserInput;
    @FXML private DatePicker startDate, endDate;    
    @FXML private CheckBox tickMonday, tickTuesday, tickWednesday, tickThursday, tickFriday, tickSaturday, tickSunday;  
    @FXML private TimeField timeStart, timeEnd; 
    @FXML private ToggleSwitch toggleDateRange, toggleTimeRange, toggleWeekdays;   
    @FXML private HBox containerDateRange, containerTimeRange, containerWeekdays;
    @FXML private HBox containerTravel, containerOpeningHours, containerMargin, containerReccurrence;
    @FXML private ToggleSwitch toggleUseTravelDuration, toggleUseOpeningHours, toggleUseMargin, toggleRecurringDate, toggleAddAutomatically; 
    @FXML private Slider sliderDurationHours, sliderDurationMinutes, sliderMarginBeforeAppointment, sliderRecurrences, sliderMarginAfterAppointment;
    @FXML private Circle imgFirstStep, imgSecondStep, imgThirdStep;
    @FXML private Text infoName, infoDuration, infoBetweenDate, infoBetweenTime, infoWeekdays, infoTravelTime, infoTimeBefore, infoTimeAfter, infoReccurrences, infoInterval;   
    private ComboBox<String> dropdownVehicle, dropdownStartAtDest, dropdownEndAtDest, dropdownInterval, dropdownDestinationOpening;
    private int userStep = 1;
    private Button dummyButton = new Button();

    private ISmartSearchController smartSearch;
    private IEntryFactory entryFactory;
    private IGoogleAPIController api;
    private IIOController iOController;
    private IAnimationController animationController;
    private IComboBoxFactory comboBoxFactory;
    private IDateSuggestionController dateSuggestionController;
    private MailTemplateModel mailTemplates;
    private SettingsModel settings;
    private ArrayList<SerializableEntry> currentSuggestions;
    

    public SearchViewController(ISmartSearchController smartSearch, IEntryFactory entryFactory,
            MailTemplateModel mailTemplates, SettingsModel settings, IGoogleAPIController api,
            IIOController iOController, IAnimationController animationController, IComboBoxFactory comboBoxFactory,
            IDateSuggestionController dateSuggestionController) {
        this.smartSearch = smartSearch;
        this.entryFactory = entryFactory;
        this.mailTemplates = mailTemplates;
        this.settings = settings;
        this.api = api;
        this.iOController = iOController;
        this.animationController = animationController;
        this.comboBoxFactory = comboBoxFactory;
        this.dateSuggestionController = dateSuggestionController;
        dummyButton.setVisible(false);
    }

    @FXML
    private void initialize() {
        TableView<SuggestionsModel> tableSuggestions = createTable();
        stepThreeUserInput.getChildren().add(tableSuggestions);
        setupInitialContainerStates();
        setupToggleBindings();
        setupTextboxInputValidation();
        createComboBoxes();
        // setupSliderBindings();
        btnReset.setVisible(false);
    }

    private void createComboBoxes() {
        dropdownVehicle = comboBoxFactory.create(ComboBoxCreate.VEHICLES);
        dropdownStartAtDest = comboBoxFactory.create(ComboBoxCreate.START);
        dropdownEndAtDest = comboBoxFactory.create(ComboBoxCreate.DESTINATION);
        dropdownDestinationOpening = comboBoxFactory.create(ComboBoxCreate.DESTINATION);
        dropdownInterval = comboBoxFactory.create(ComboBoxCreate.RECCURENCEOPTIONS);
        dropdownInterval.setValue(dropdownInterval.getItems().get(0));        
        containerTravel.getChildren().addAll(dropdownVehicle, dropdownStartAtDest, dropdownEndAtDest);  
        containerOpeningHours.getChildren().add(dropdownDestinationOpening);
        containerReccurrence.getChildren().add(dropdownInterval);
        dropdownEndAtDest.getEditor().textProperty().bindBidirectional(dropdownDestinationOpening.getEditor().textProperty());
        startDate.setEditable(false);
        endDate.setEditable(false);
    }

    private void setupSliderBindings() {
        tfDurationMinutes.textProperty().bind(sliderDurationMinutes.valueProperty().asString());
        tfDurationHours.textProperty().bind(sliderDurationHours.valueProperty().asString());
    }

    private void setupInitialContainerStates() {
        HBox[] containers = { containerDateRange, containerTimeRange, containerWeekdays, containerTravel,
                containerOpeningHours, containerMargin, containerReccurrence };

        for (var hBox : containers) {
            hBox.setScaleX(0);
            hBox.setScaleY(0);
        }
    }   

    @FXML
    private void updateUserStepView(ActionEvent event) {
        String[] headings = { "Basisinformationen", "Optionale Informationen", "Vorschlagsauswahl" };
        Circle[] images = { imgFirstStep, imgSecondStep, imgThirdStep };
        VBox[] allSteps = { stepOneUserInput, stepTwoUserInput, stepThreeUserInput };
        int incrementor = 0;
        var button = (Button) event.getSource();

        if (button.equals(btnReset)) {
            clearFields();
            return;
        }
        if (button.equals(btnConfirm)) {
            btnReset.setVisible(true);
            if (userStep == 2) {
                SuggestionsModel.data.clear();
                startRequest();
                iterateThroughSuggestions(); 
                btnConfirm.setText("NÄCHSTE 20 VORSCHLÄGE"); 
            }   
            if (userStep == 3)
            {                
                iterateThroughSuggestions();  
                return;
            }
            incrementor = 1;
        } else if (button.equals(btnBack)) {
            if (userStep == 1)
                return;
            if (userStep == 3)
            {
                btnReset.setVisible(false);
                btnConfirm.setText("WEITER");
            }
            incrementor = -1;
        }  
        
        int currentIndex = userStep - 1;
        int requestedIndex = userStep - 1 + incrementor;
        changeViewState(allSteps[currentIndex], allSteps[requestedIndex], images[currentIndex], images[requestedIndex]);
        userStep += incrementor;
        txtHeaderStep.setText(headings[currentIndex]);
    }
    
    
    private int recurrences = 1;   
    //private int timeBeforeGlobal = 0;
    private int timeAfterGlobal = 0;
    private int travelTimeTo = 0;
    private LocalDateTime timeToStartSearch;
    private SerializableEntry currentSuggestion;

    private void iterateThroughSuggestions() {
        if (toggleAddAutomatically.isSelected()) {
            automaticEntryCreation();
            return;
        } else {
            SuggestionsModel.toggleTravelTime = toggleUseTravelDuration.isSelected();
            SuggestionsModel.travelTime = travelTimeTo;
        }

        int duration = validateDuration() + timeAfterGlobal;
        SuggestionsModel.data.clear();

        for (int i = 0; i < 20; i++)         
        {
            currentSuggestion = dateSuggestionController.getDateSuggestionFromEntryList(currentSuggestions, timeToStartSearch, duration);
            
            if (currentSuggestion == null)
                return;        
            
            timeToStartSearch = currentSuggestion.getEndAsLocalDateTime(); 
            SuggestionsModel.addToList(currentSuggestion.getStartTime(), currentSuggestion.getEndTime().minusMinutes(timeAfterGlobal), 
                currentSuggestion.getStartDate(), currentSuggestion.getEndDate(), new Button("EINTRAGEN"), tfAppointmentName.getText());
        } 
    } 
    
    private void automaticEntryCreation()
    {
        int duration = validateDuration() + timeAfterGlobal;
        int interval = calculateInterval();
        if (interval == 0)
            interval = 1;
        LocalDateTime tempDate = timeToStartSearch.minusMinutes(1);
        LocalTime startAt = timeToStartSearch.toLocalTime();
        while (recurrences > 0) 
        {
            currentSuggestion = dateSuggestionController.getDateSuggestionFromEntryList(currentSuggestions, timeToStartSearch, duration);
            if (currentSuggestion == null)
                return;
            
            timeToStartSearch = currentSuggestion.getEndAsLocalDateTime(); 

            if (currentSuggestion.getStartAsLocalDateTime().isAfter(tempDate))
            {
                createEntryIncludingTravelTimes(currentSuggestion);                
                SuggestionsModel.addToList(currentSuggestion.getStartTime(), currentSuggestion.getEndTime().minusMinutes(timeAfterGlobal), 
                    currentSuggestion.getStartDate(), currentSuggestion.getEndDate(), dummyButton, tfAppointmentName.getText());
                timeToStartSearch = LocalDateTime.of(timeToStartSearch.toLocalDate(), startAt);
                tempDate = timeToStartSearch.plusDays(interval);
                recurrences--;
            }
        } 
    }

    private void createEntryIncludingTravelTimes(SerializableEntry currentSuggestion) {
        int traveltime = 0;
        if (toggleUseTravelDuration.isSelected()) {
            traveltime = travelTimeTo;
        }
        entryFactory.createNewUserEntryIncludingTravelTimes(currentSuggestion.getStartDate(),
                currentSuggestion.getEndDate(), currentSuggestion.getStartTime(),
                currentSuggestion.getEndTime().minusMinutes(timeAfterGlobal), tfAppointmentName.getText(), traveltime);
    }
    
    private void clearFields()
    {
        currentSuggestion = null;
        currentSuggestions = null;
        timeToStartSearch = null;
        recurrences = 1;
        //timeBeforeGlobal = 0;
        timeAfterGlobal = 0;
        travelTimeTo = 0;   
        SuggestionsModel.data.clear();
        resetToggleStates();
    }

    private void resetToggleStates()
    {
        toggleUseTravelDuration.setSelected(false);
        toggleUseOpeningHours.setSelected(false);
        toggleUseMargin.setSelected(false);
        toggleRecurringDate.setSelected(false);
        toggleAddAutomatically.setSelected(false); 
        toggleDateRange.setSelected(true);
        toggleTimeRange.setSelected(true);
        toggleWeekdays.setSelected(true);

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

    private void startRequest() {
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
        //var suggestionsCount = validateSuggestionsCount();         
        
        currentSuggestions = smartSearch.findPossibleTimeSlots(userPrefs, duration, weekdays, 
            openingHours, updatedTimes[0], updatedTimes[1], intervalDays);
        
        recurrences = validateReccurrences();  
        //interval = intervalDays;
        //timeBeforeGlobal = updatedTimes[0];
        timeAfterGlobal = updatedTimes[1];
        //timeToStartSearch = currentSuggestions.get(0).getStartAsLocalDateTime(); 
        timeToStartSearch = LocalDateTime.of(startDateInput, startTimeInput); 
        travelTimeTo = travelTime;
    }

    private int validateDuration() {
        int duration = (int) sliderDurationMinutes.getValue() + (int) sliderDurationHours.getValue() * 60;
        if (duration < 5)
            duration = 5;
        return duration;
    }

    private LocalDate[] validateDateInput() {
        var startDateInput = startDate.getValue();
        var endDateDateInput = endDate.getValue();

        if (startDateInput == null || toggleDateRange.isSelected())
            startDateInput = LocalDate.now();
        if (endDateDateInput == null || toggleDateRange.isSelected())
            endDateDateInput = LocalDate.now().plusDays(1500);
        return new LocalDate[] { startDateInput, endDateDateInput };         
    }

    private LocalTime[] validateTimeInput() {
        var startTimeInput = timeStart.getValue();
        var endTimeInput = timeEnd.getValue();

        if (toggleTimeRange.isSelected()) {
            startTimeInput = LocalTime.of(0, 0, 0);
            endTimeInput = LocalTime.of(23, 59, 59);
        }
        return new LocalTime[] { startTimeInput, endTimeInput };
    }

    private boolean[] validateWeekdays() {
        if (toggleWeekdays.isSelected() == false) {
            return new boolean[] { tickMonday.isSelected(), tickTuesday.isSelected(), tickWednesday.isSelected(),
                    tickThursday.isSelected(), tickFriday.isSelected(), tickSaturday.isSelected(),
                    tickSunday.isSelected() };
        } else
            return new boolean[] { true, true, true, true, true, true, true };
    } 
    
    private int validateTravelTime()
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

    private String getApiStringFromInput() {
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

    private int updateTravelTimeToMinutes(int travelTime) {
        if (travelTime != 0)
            travelTime = travelTime / 60;
        return travelTime;
    }

    private HashMap<DayOfWeek, List<SerializableEntry>> validateOpeningHours() {
        var openingHours = new HashMap<DayOfWeek, List<SerializableEntry>>();
        var destination = dropdownEndAtDest.getSelectionModel().getSelectedItem();

        if (toggleUseOpeningHours.isSelected() && destination.isEmpty() == false)
            openingHours = api.getOpeningHours(destination);

        return openingHours;
    }

    private int[] compareTimes(int timeBefore, int timeAfter, int travelTime) {
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

    private int calculateInterval() {
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

    private int validateSuggestionsCount() {
        if (toggleRecurringDate.isSelected() == false && toggleAddAutomatically.isSelected())
            return 1;
        else
            return 1000;
    }

    private int validateReccurrences() {
        if (toggleRecurringDate.isSelected())
            return sliderRecurrences.valueProperty().intValue();
        else
            return 1;
    }

    private LocalTime validateStartSearchTime(LocalTime startTimeInput) {
        var currentTime = LocalDateTime.now().toLocalTime();
        if (startTimeInput.isBefore(currentTime))
            return currentTime;
        else
            return startTimeInput;
    }

    private void changeViewState(VBox deactivate, VBox activate, Circle currentC, Circle nextC) {
        deactivate.setDisable(true);
        deactivate.setVisible(false);
        activate.setDisable(false);
        activate.setVisible(true);
        currentC.setId("customCircleInactive");
        nextC.setId("customCircleActive");
    }

    @FXML
    private void testUpdate(ActionEvent event) throws IOException, ClassNotFoundException {
        // entryFactory.createRandomContactsList(100);
        // iOController.saveContactsToFile();
    }

    @FXML
    private void resetTest(ActionEvent event) throws IOException, ClassNotFoundException {
        SuggestionsModel.data.clear();
    }

    final void changeContentPosition(double width, double height) {
        Text[] headers = { txtFirstStep, txtSecondStep, txtThirdStep };
        Circle[] circles = { imgFirstStep, imgSecondStep, imgThirdStep };
        boolean isWindowSmall = false;
        int rowHeight = 80;
        if (height < 700) {
            isWindowSmall = true;
            rowHeight = 30;
        }
        firstRow.setPrefHeight(rowHeight);
        firstRow.setMaxHeight(rowHeight);
        animationController.growAndShrinkCircle(circles, headers, isWindowSmall);
    }

    private TableView<SuggestionsModel> createTable() {
        TableColumn<SuggestionsModel, String> startTimeColumn = new TableColumn<>("Startzeit");
        TableColumn<SuggestionsModel, String> endTimeColumn = new TableColumn<>("Endzeit");
        TableColumn<SuggestionsModel, String> dateColumnStart = new TableColumn<>("Startdatum");
        TableColumn<SuggestionsModel, String> dateColumnEnd = new TableColumn<>("Enddatum");
        TableColumn<SuggestionsModel, String> button = new TableColumn<>("eintragen");
        TableView<SuggestionsModel> table = new TableView<SuggestionsModel>(SuggestionsModel.data);

        startTimeColumn.setCellValueFactory(new PropertyValueFactory<SuggestionsModel, String>("startTime"));
        endTimeColumn.setCellValueFactory(new PropertyValueFactory<SuggestionsModel, String>("endTime"));
        dateColumnStart.setCellValueFactory(new PropertyValueFactory<SuggestionsModel, String>("dayStart"));
        dateColumnEnd.setCellValueFactory(new PropertyValueFactory<SuggestionsModel, String>("dayEnd"));
        button.setCellValueFactory(new PropertyValueFactory<SuggestionsModel, String>("button"));

        startTimeColumn.setPrefWidth(150);
        endTimeColumn.setPrefWidth(150);
        dateColumnStart.setPrefWidth(150);
        dateColumnEnd.setPrefWidth(150);
        button.setPrefWidth(150);
        table.getColumns().add(startTimeColumn);
        table.getColumns().add(endTimeColumn);
        table.getColumns().add(dateColumnStart);
        table.getColumns().add(dateColumnEnd);
        table.getColumns().add(button);

        return table;
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
}