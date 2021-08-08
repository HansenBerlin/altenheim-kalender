package com.altenheim.kalender.implementations.controller.viewController;

import com.altenheim.kalender.interfaces.factorys.ComboBoxFactory;
import com.altenheim.kalender.interfaces.factorys.EntryFactory;
import com.altenheim.kalender.interfaces.logicController.DateSuggestionController;
import com.altenheim.kalender.interfaces.logicController.GoogleAPIController;
import com.altenheim.kalender.interfaces.logicController.MailClientAccessController;
import com.altenheim.kalender.interfaces.logicController.SmartSearchController;
import com.altenheim.kalender.interfaces.viewController.CalendarEntriesController;
import com.altenheim.kalender.interfaces.viewController.AnimationController;
import com.altenheim.kalender.interfaces.viewController.PopupViewController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import com.altenheim.kalender.implementations.controller.models.*;
import com.altenheim.kalender.resourceClasses.ComboBoxCreate;
import com.altenheim.kalender.implementations.controller.logicController.*;
import com.altenheim.kalender.implementations.controller.factories.*;
import org.controlsfx.control.ToggleSwitch;

public class SearchViewController extends SearchViewRequestHandlerController
{
    private final AnimationController animationController;
    private final ComboBoxFactory comboBoxFactory;

    private int userStep = 1;     

    public SearchViewController(GoogleAPIController api, CalendarEntriesController allCalendars, AnimationController animationController,
                                ComboBoxFactory comboBoxFactory, PopupViewController popupViewController, MailClientAccessController mailCreationController,
                                EntryFactory entryFactory, SmartSearchController smartSearch, DateSuggestionController dateSuggestionController)
    {
        super(api, allCalendars, popupViewController, mailCreationController, entryFactory, smartSearch, dateSuggestionController);        
        this.animationController = animationController;
        this.comboBoxFactory = comboBoxFactory;
    }      

    @FXML
    public void initialize() 
    {    
        super.initialize();
        TableView<SuggestionsModel> tableSuggestions = createTable();
        stepThreeUserInput.getChildren().add(tableSuggestions);
        setupInitialContainerStates();
        setupToggleBindings();
        setupTextboxInputValidation();
        createComboBoxes();
        setDateAndTimeFields();
        registerButtonEvents();
    }
       
    public void calendarToggleClicked()
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

    private void registerButtonEvents()
    {
        btnConfirm.setOnAction(this::updateUserStepView);
        btnBack.setOnAction(this::updateUserStepView);
        btnReset.setOnAction(this::updateUserStepView);
        toggleCalendars.setOnMouseReleased(event -> calendarToggleClicked());
    }

    public void setupAdvancedOptionsToggles()
    {
        if (!SettingsModelImpl.useAdvancedFeatures)
        {
            toggleUseOpeningHours.setDisable(true);
            toggleUseTravelDuration.setDisable(true);
        }
    }
    

    protected void updateUserStepView(ActionEvent event) 
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
                startRequest();
                iterateThroughSuggestions();
                btnConfirm.setText("NÄCHSTE 20 VORSCHLÄGE"); 
                if (toggleAddAutomatically.isSelected())
                    btnConfirm.setVisible(false);                
            }   
            if (userStep == 3)
            {                
                iterateThroughSuggestions();  
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

    private TableView<SuggestionsModel> createTable() 
    {
        TableColumn<SuggestionsModel, String> startTimeColumn = new TableColumn<>("Startzeit");
        TableColumn<SuggestionsModel, String> endTimeColumn = new TableColumn<>("Endzeit");
        TableColumn<SuggestionsModel, String> dateColumnStart = new TableColumn<>("Startdatum");
        TableColumn<SuggestionsModel, String> dateColumnEnd = new TableColumn<>("Enddatum");
        TableColumn<SuggestionsModel, String> button = new TableColumn<>("eintragen");
        TableView<SuggestionsModel> table = new TableView<>(SuggestionsModel.data);

        startTimeColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        endTimeColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        dateColumnStart.setCellValueFactory(new PropertyValueFactory<>("dayStart"));
        dateColumnEnd.setCellValueFactory(new PropertyValueFactory<>("dayEnd"));
        button.setCellValueFactory(new PropertyValueFactory<>("button"));

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
                    if (j > 3)
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

            textFieldsFirstView[j].textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.matches("\\d*"))
                {
                    textFieldsFirstView[j].setText(newValue.replaceAll("[^\\d]", ""));
                }
                else
                {
                    double value = Double.parseDouble(textFieldsFirstView[j].getText());
                    sliderFirstView[j].setValue(value);
                }
            });
        i++;            
        }
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