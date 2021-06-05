package com.altenheim.kalender.controller;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

public class ParentViewController 
{
    @FXML
    protected GridPane childContainer;
    protected AnchorPane parentContainer;

    public ParentViewController(AnchorPane parentContainer)
    {
        this.parentContainer = parentContainer;
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
        if (childContainer == null)
            return;
        childContainer.setPrefSize(parentContainer.getWidth(), parentContainer.getHeight());
    }

    
    
}
