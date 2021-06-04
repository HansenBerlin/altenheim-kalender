package com.altenheim.kalender.controller;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class ParentViewController 
{
    @FXML
    protected GridPane childContainerView;
    protected Stage stage;
    protected AnchorPane parent;

    public ParentViewController(Stage stage, AnchorPane parent)
    {
        this.stage = stage;
        this.parent = parent;

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
    
}
