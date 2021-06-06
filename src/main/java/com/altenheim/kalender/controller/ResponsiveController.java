package com.altenheim.kalender.controller;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

public abstract class ResponsiveController 
{
    protected GridPane childContainer;
    abstract void changeContentPosition();    

    protected void setChildContainer(GridPane childContainer)
    {
        this.childContainer = childContainer;
    }

    protected void changeSize(AnchorPane parent)
    {
        if (childContainer == null)
            return;
        childContainer.setMinWidth(parent.getWidth());
        childContainer.setMinHeight(parent.getHeight());        
    }    
}
