package com.altenheim.kalender.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Slider;
import java.io.IOException;
import com.calendarfx.view.TimeField;

public class SearchViewController 
{
    @FXML
    private GridPane childContainerView;

    @FXML
    private Slider sliderAppointmentDuration;
   
    @FXML
    private TimeField timeStarttimeEnd;     

    private Stage stage;
    private AnchorPane parent;

    public SearchViewController(Stage stage, AnchorPane parent)
    {
        this.stage = stage;
        this.parent = parent;
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


