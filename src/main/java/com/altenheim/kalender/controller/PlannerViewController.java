package com.altenheim.kalender.controller;

import com.altenheim.kalender.views.CalendarViewOverride;
import com.calendarfx.view.CalendarView;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class PlannerViewController 
{
    @FXML
    private GridPane childContainerView;

    @FXML
    private GridPane gridRightColumn;
    @FXML

    private CalendarViewOverride calendarViewTotal;

    private Stage stage;
    private AnchorPane parent;

    public PlannerViewController(Stage stage, AnchorPane parent)
    {
        this.stage = stage;
        this.parent = parent;
        calendarViewTotal = new CalendarViewOverride();

    }
    
    public void changeContentPosition()
    {
        /*if (childContainerView == null)
            return;
        childContainerView.getChildren().remove(gridRightColumn);
        if (stage.getWidth() < 1200)
        {
            childContainerView.add(gridRightColumn, 0, 1);
        }
        else
        {
            childContainerView.add(gridRightColumn, 1, 0);
        }*/
    }

    public void changeSize()
    {
        if (childContainerView == null)
            return;
        childContainerView.setPrefSize(parent.getWidth(), parent.getHeight());
    }

    public void addCustomCalendarView()
    {
        childContainerView.add(calendarViewTotal, 0, 0, 1, 1);
    }
}


