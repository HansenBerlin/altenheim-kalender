package com.altenheim.kalender.controller;

import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class PlannerViewController 
{
    @FXML
    private GridPane childContainerView;

    @FXML
    private GridPane gridRightColumn;

    private Stage stage;

    public PlannerViewController(Stage stage)
    {
        this.stage = stage;
    }
    
    public void changeContentPosition()
    {
        if (childContainerView == null)
            return;
        childContainerView.getChildren().remove(gridRightColumn);
        if (stage.getWidth() < 1200)
        {
            childContainerView.add(gridRightColumn, 0, 1);
        }
        else
        {
            childContainerView.add(gridRightColumn, 1, 0);
        }
    }
}


