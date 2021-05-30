package com.altenheim.kalender.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class SearchViewController 
{
    @FXML
    private GridPane childContainerView;

    @FXML
    private Accordion accordeonSettings;

    private Stage stage;

    public SearchViewController(Stage stage)
    {
        this.stage = stage;
    }
    
    public void changeContentPosition()
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
    }
}


