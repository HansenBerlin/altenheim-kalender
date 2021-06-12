package com.altenheim.kalender.controller.viewController;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import com.altenheim.kalender.models.*;
import java.io.IOException;
import java.util.List;

import com.altenheim.kalender.interfaces.IAppointmentEntryFactory;
import com.altenheim.kalender.interfaces.IGoogleAPIController;
import com.altenheim.kalender.interfaces.IIOController;
import com.altenheim.kalender.interfaces.ISmartSearchController;
import com.calendarfx.view.TimeField;
import org.controlsfx.control.ToggleSwitch;


public class SearchViewController extends ResponsiveController
{
    @FXML private Text txtHeaderStep, txtFirstStep, txtSecondStep, txtThirdStep;
    @FXML private TextField txtAppointmentName, txtDuration;
    @FXML private Button btnBack, btnConfirm;    
    @FXML private VBox stepOneUserInput, stepTwoUserInput, stepThreeUserInput;
    @FXML private DatePicker startDate, endDate;    
    @FXML private CheckBox tickMonday, tickTuesday, tickWednesday, tickThursday, tickFriday, tickSaturday, tickSunday;  
    @FXML private TimeField timeStart, timeEnd;    
    @FXML private ToggleSwitch toggleUseTravelDuration, toggleUseMargin, toggleUseOpeningHours, toggleRecurringDate, 
        toggleAutoSuggest, toogleUseNextAppointment;  
    @FXML private Slider sliderMarginBeforeAppointment, sliderRecurrences, sliderMarginAfterAppointment, sliderAppointmentDuration;
    @FXML private SplitMenuButton dropdownToDestinationOpeningOptions, dropdownInterval,dropdownStartAt, 
        dropdownToDestinationTravelTimeOption, dropdownVehicle;        
    @FXML private Spinner<Integer> sliderSuggestionCount;  
    @FXML private Circle imgFirstStep, imgSecondStep, imgThirdStep;
        
    private TableView<SuggestionsModel> tableSuggestions;
    private int userStep = 1;    
    private ISmartSearchController smartSearch;
    private IAppointmentEntryFactory entryFactory;
    private List<ContactModel> contacts;
    private MailTemplateModel mailTemplates;
    private SettingsModel settings;
    private IGoogleAPIController api;
    private IIOController iOController;

    public SearchViewController(ISmartSearchController smartSearch, IAppointmentEntryFactory entryFactory, List<ContactModel> contacts, 
        MailTemplateModel mailTemplates, SettingsModel settings, IGoogleAPIController api, IIOController iOController)
    {
        this.smartSearch = smartSearch;
        this.entryFactory = entryFactory;
        this.contacts = contacts;
        this.mailTemplates = mailTemplates;
        this.settings = settings;
        this.api = api;
        this.iOController = iOController;
    }

    @FXML
    private void initialize()
    {
        tableSuggestions = createTable();
        stepThreeUserInput.getChildren().add(tableSuggestions);
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
                return;
            incrementor = 1;
        }
        else if (button.equals(btnBack) && userStep == 1)        
            return;           
        
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

    @FXML
    private void testUpdate(ActionEvent event) throws IOException, ClassNotFoundException
    {   /*
        var testEntry = entryFactory.createUserSettingsEntry(timeStart.getValue(), timeEnd.getValue());
        var result = smartSearch.findAvailableTimeSlot(testEntry, (int)sliderAppointmentDuration.getValue());
        for (var entry : result) 
        {
            SuggestionsModel.addToList(entry.getStartTime(), entry.getEndTime());
            System.out.println(entry.getStartTime() + " " + entry.getEndTime());
        }
        iOController.writeCalendarFiles(null);

        for (int i = 0; i < 5; i++)        
            entryFactory.createRandomCalendarList("Test " + i);
        iOController.writeCalendarFiles();*/
        entryFactory.createContactsList(100);
        iOController.saveContactsToFile();     
    }

    @FXML
    private void resetTest(ActionEvent event) throws IOException, ClassNotFoundException
    {
        //SuggestionsModel.data.clear();
        //settings.setScrapingInterval(settings.getScrapingInterval()+1000);        
    }


    public void changeContentPosition() 
    {        
    }

    private TableView<SuggestionsModel> createTable()
    {
        TableColumn<SuggestionsModel, String> startColumn = new TableColumn<>("Start");
        TableColumn<SuggestionsModel, String> endColumn = new TableColumn<>("Ende");
        TableView<SuggestionsModel> table = new TableView<SuggestionsModel>(SuggestionsModel.data);

        startColumn.setCellValueFactory(new PropertyValueFactory<SuggestionsModel, String>("startTime"));
        endColumn.setCellValueFactory(new PropertyValueFactory<SuggestionsModel, String>("endTime"));
        startColumn.setPrefWidth(150);
        endColumn.setPrefWidth(150);         
        table.getColumns().add(startColumn);
        table.getColumns().add(endColumn);
        //table.setPrefHeight(200);

        return table;
    }
}