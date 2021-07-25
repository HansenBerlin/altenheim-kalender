package com.altenheim.kalender.controller.viewController;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

public abstract class ResponsiveController {
    protected GridPane childContainer;

    abstract void changeContentPosition(double width, double height);

    protected void setChildContainer(GridPane childContainer) {
        this.childContainer = childContainer;
    }

    final void changeSize(AnchorPane parent) {
        if (childContainer == null)
            return;
        childContainer.setMinWidth(parent.getWidth());
        childContainer.setMinHeight(parent.getHeight());
    }
    
}
