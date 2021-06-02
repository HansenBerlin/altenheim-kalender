package com.altenheim.kalender.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Slider;

import java.io.IOException;

import com.calendarfx.view.TimeField;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXCheckbox;
import io.github.palexdev.materialfx.controls.MFXDatePicker;

public class SearchViewController 
{
    @FXML
    private GridPane childContainerView;

    @FXML
    private Slider sliderAppointmentDuration;

    @FXML
    private MFXDatePicker datePickerStart, datePickerEnd;   

    @FXML
    private MFXCheckbox cbMonday, cbTuesday, cbWednesday, cbThursday, cbFriday, cbSaturday, cbSunday;

    @FXML
    private TimeField timeStarttimeEnd;    

    @FXML
    private MFXButton btnBack, btnConfirm;    

    private Stage stage;

    public SearchViewController(Stage stage)
    {
        this.stage = stage;
    }

    @FXML
    void btnListener(ActionEvent event) 
    {

    }

    @FXML 
    private void initialize() throws IOException 
    {
        
    }

    private void initializeTickBoxes()
    {
        //cbMonday.set

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


