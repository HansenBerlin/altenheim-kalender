package com.altenheim.kalender.interfaces;

import com.altenheim.kalender.controller.viewController.*;
import javafx.scene.layout.GridPane;

public interface IViewRootsModel 
{
    GridPane[] getAllViews();
    ResponsiveController[] getAllViewControllers();
    void addViewRootToList(int atIndex, GridPane view);
    MainWindowController getMainWindowController();
    void setMainWindowController(MainWindowController mainWindowController);
}
