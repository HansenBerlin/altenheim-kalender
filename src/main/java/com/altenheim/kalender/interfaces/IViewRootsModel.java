package com.altenheim.kalender.interfaces;

import com.altenheim.kalender.controller.viewController.ResponsiveController;
import javafx.scene.layout.GridPane;

public interface IViewRootsModel 
{
    public GridPane[] getAllViews();
    public ResponsiveController[] getAllViewControllers();
    public void addViewRootToList(int atIndex, GridPane view);
}
