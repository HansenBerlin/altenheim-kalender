package com.altenheim.kalender.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TableColumn;
import javafx.scene.paint.Color;
import com.calendarfx.view.TimeField;
import org.controlsfx.control.ToggleSwitch;


public class SearchViewController 
{
    @FXML private GridPane childContainerView;
    @FXML private Text txtHeaderStep, txtFirstStep, txtSecondStep, txtThirdStep;
    @FXML private Button btnBack, btnConfirm;    
    @FXML private VBox stepOneUserInput, stepTwoUserInput, stepThreeUserInput;
    @FXML private DatePicker startDate, endDate;    
    @FXML private CheckBox tickMonday, tickTuesday, tickWednesday, tickThursday, tickFriday, tickSaturday, tickSunday;  
    @FXML private TimeField timeStart, timeEnd;    
    @FXML private ToggleSwitch toggleUseTravelDuration, toggleUseMargin, toggleUseOpeningHours, toggleRecurringDate, toggleAutoSuggest;  
    @FXML private Slider sliderMarginBeforeAppointment, sliderRecurrences, sliderMarginAfterAppointment, sliderAppointmentDuration;
    @FXML private SplitMenuButton dropdownToDestinationOpeningOptions, dropdownInterval,dropdownStartAt, 
        dropdownToDestinationTravelTimeOption, dropdownVehicle;  
    @FXML private TableColumn<String, String> columnStartDate;
    @FXML private TableColumn<String, String> columnEndDate;
    @FXML private TableColumn<Button, String> columnTick;
    @FXML private Spinner<Integer> sliderSuggestionCount;  
    @FXML private Circle imgFirstStep, imgSecondStep, imgThirdStep;

    private Stage stage;
    private AnchorPane parent;
    private int userStep = 1;

    public SearchViewController(Stage stage, AnchorPane parent)
    {
        this.stage = stage;
        this.parent = parent;
    }


    @FXML
    private void updateUserStepView(ActionEvent event) 
    {        
        String[] headings = {"Basisinformationen" , "Optionale Informationen", "Vorschlagsauswahl" };
        Circle[] images = { imgFirstStep, imgSecondStep, imgThirdStep };
        Text[] stepsText = { txtFirstStep, txtSecondStep, txtThirdStep };
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
        changeViewState(allSteps[currentIndex], allSteps[requestedIndex]); 
        changeStepsButtonState(images[currentIndex], images[requestedIndex], stepsText[currentIndex], stepsText[requestedIndex]);        
        userStep += incrementor; 
        txtHeaderStep.setText(headings[currentIndex]);
    }


    private void changeViewState(VBox deactivate, VBox activate)
    {
        deactivate.setDisable(true);
        deactivate.setVisible(false);
        activate.setDisable(false);
        activate.setVisible(true);
    }


    private void changeStepsButtonState(Circle currentC, Circle nextC, Text currentT, Text nextT)
    {
        currentC.setFill(Color.web("transparent"));
        nextC.setFill(Color.web("#6d25bf"));
        currentT.setFill(nextT.getFill());
        nextT.setFill(Color.WHITE);
    }  


    public void changeSize()
    {
        if (childContainerView == null)
            return;
        childContainerView.setPrefSize(parent.getWidth(), parent.getHeight());
    }

    
   /* public void changeContentPosition()
    {
        if (childContainerView == null)
            return;
        childContainerView.getChildren().remove(accordeonSettings);
        if (stage.getWidth() < 1200)
        {
            childContainerView.add(accordeonSettings, 0, 2);
        }
        else
        {
            childContainerView.add(accordeonSettings, 1, 1);
        }
     }*/
}


