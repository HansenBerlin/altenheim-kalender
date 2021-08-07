package com.altenheim.kalender.controller.viewController;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import com.altenheim.kalender.models.*;
import com.altenheim.kalender.resourceClasses.ComboBoxCreate;
import com.altenheim.kalender.controller.logicController.*;
import com.altenheim.kalender.controller.Factories.*;
import com.altenheim.kalender.interfaces.*;
import org.controlsfx.control.ToggleSwitch;

public class SearchViewController extends SearchViewValidationController
{
    @FXML private Text txtHeaderStep, txtFirstStep, txtSecondStep, txtThirdStep;
    @FXML private TextField tfDurationMinutes, tfDurationHours;    
    @FXML private Button btnBack, btnConfirm, btnReset;     
    @FXML private VBox stepOneUserInput, stepTwoUserInput, stepThreeUserInput;
    @FXML private HBox containerDateRange, containerTimeRange, containerWeekdays, containerMailTemplate;
    @FXML private HBox containerTravel, containerOpeningHours, containerMargin, containerReccurrence;
    @FXML private Circle imgFirstStep, imgSecondStep, imgThirdStep;
    @FXML private RowConstraints firstRow; 
    
    private IAnimationController animationController;
    private IComboBoxFactory comboBoxFactory;  
    private SearchViewRequestHandlerController requestHandler;  

    private int userStep = 1;     

    public SearchViewController(IGoogleAPIController api, ICalendarEntriesModel allCalendars, IAnimationController animationController, 
        IComboBoxFactory comboBoxFactory, SearchViewRequestHandlerController requestHandler) 
    {
        super(api, allCalendars);        
        this.animationController = animationController;
        this.comboBoxFactory = comboBoxFactory;
        this.requestHandler = requestHandler;
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
        setDateAndTimeFields();
    }

    @FXML
    private void calendarToggleClicked(MouseEvent event) 
    {
        if (toggleCalendars.isSelected())
        {
            containerCalendars.getChildren().remove(calendarSelection);
        }
        else
        {
            calendarSelection = SplitMenuButtonFactory.createButtonForAvailableCalendars(allCalendars.getAllCalendars());
            containerCalendars.getChildren().add(calendarSelection);
        }
    }

    @FXML
    private void updateUserStepView(ActionEvent event) 
    {
        String[] headings = { "Basisinformationen", "Optionale Informationen", "Vorschlagsauswahl" };
        Circle[] images = { imgFirstStep, imgSecondStep, imgThirdStep };
        VBox[] allSteps = { stepOneUserInput, stepTwoUserInput, stepThreeUserInput };
        int incrementor = 0;
        var button = (Button) event.getSource();

        if (button.equals(btnReset)) 
        {
            clearFields();
            return;
        }
        if (button.equals(btnConfirm)) 
        {
            if (userStep == 2) 
            {
                btnReset.setVisible(false);
                SuggestionsModel.data.clear();
                requestHandler.startRequest();
                requestHandler.iterateThroughSuggestions();
                btnConfirm.setText("NÄCHSTE 20 VORSCHLÄGE"); 
                if (toggleAddAutomatically.isSelected())
                    btnConfirm.setVisible(false);                
            }   
            if (userStep == 3)
            {                
                requestHandler.iterateThroughSuggestions();  
                return;
            }
            incrementor = 1;
        } 
        else if (button.equals(btnBack)) 
        {
            if (userStep == 1)
                return;
            if (userStep == 3)
            {
                btnReset.setVisible(true);
                btnConfirm.setVisible(true);
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

    private void changeViewState(VBox deactivate, VBox activate, Circle currentC, Circle nextC) 
    {
        deactivate.setDisable(true);
        deactivate.setVisible(false);
        activate.setDisable(false);
        activate.setVisible(true);
        currentC.setId("customCircleInactive");
        nextC.setId("customCircleActive");
    }

    private void createComboBoxes() 
    {
        dropdownVehicle = comboBoxFactory.create(ComboBoxCreate.VEHICLES);
        dropdownStartAtDest = comboBoxFactory.create(ComboBoxCreate.START);
        dropdownEndAtDest = comboBoxFactory.create(ComboBoxCreate.DESTINATION);
        dropdownDestinationOpening = comboBoxFactory.create(ComboBoxCreate.DESTINATION);
        dropdownInterval = comboBoxFactory.create(ComboBoxCreate.RECCURENCEOPTIONS);
        dropdownInterval.setValue(dropdownInterval.getItems().get(0)); 
        dropdownMailTemplates = comboBoxFactory.create(ComboBoxCreate.MAILTEMPLATESELECTORTEMPLATE);
        dropDownContact = comboBoxFactory.create(ComboBoxCreate.MAILADRESSES);

        containerTravel.getChildren().addAll(dropdownVehicle, dropdownStartAtDest, dropdownEndAtDest);  
        containerOpeningHours.getChildren().add(dropdownDestinationOpening);
        containerReccurrence.getChildren().add(dropdownInterval);
        containerMailTemplate.getChildren().addAll(dropdownMailTemplates, dropDownContact);
        dropdownEndAtDest.getEditor().textProperty().bindBidirectional(dropdownDestinationOpening.getEditor().textProperty());
        startDate.setEditable(false);
        endDate.setEditable(false);
    }

    private void setupInitialContainerStates() 
    {
        HBox[] containers = { containerDateRange, containerTimeRange, containerWeekdays, containerTravel,
                containerOpeningHours, containerMargin, containerReccurrence, containerCalendars, containerMailTemplate };

        for (var hBox : containers) 
        {
            hBox.setScaleX(0);
            hBox.setScaleY(0);
        }
    } 
/*
    private Button createSendMailButton()
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

    private Button createAddEntryButton(Entry<String> currSug)
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

    private void registerButtonSendMailEvent(Button button)
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
    }*/

/*
    private void iterateThroughSuggestions() 
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
    
    private void automaticEntryCreation()
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

    private void createEntryIncludingTravelTimes(Entry<String> currentSuggestion) 
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

    private void startRequest() 
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
    }*/

    private TableView<SuggestionsModel> createTable() 
    {
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

        startTimeColumn.setPrefWidth(100);
        endTimeColumn.setPrefWidth(100);
        dateColumnStart.setPrefWidth(100);
        dateColumnEnd.setPrefWidth(100);
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
        ToggleSwitch[] toggles = { toggleDateRange, toggleTimeRange, toggleWeekdays, toggleCalendars, toggleUseTravelDuration, 
            toggleUseOpeningHours, toggleUseMargin, toggleRecurringDate, toggleUseMailTemplate };
        HBox[] containers = { containerDateRange, containerTimeRange, containerWeekdays, containerCalendars, containerTravel, 
            containerOpeningHours, containerMargin, containerReccurrence, containerMailTemplate };

        int i = 0;

        while (i < toggles.length) 
        {
            final int j = i;
            toggles[j].selectedProperty().addListener(((observable, oldValue, newValue) -> 
            {
                Boolean valueToSet;
                {
                    if (j > 3 && j < 9)
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

    protected void changeContentPosition(double width, double height) 
    {
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
}