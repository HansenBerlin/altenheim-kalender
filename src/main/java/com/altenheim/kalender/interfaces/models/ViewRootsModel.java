package com.altenheim.kalender.interfaces.models;

import com.altenheim.kalender.implementations.controller.viewController.*;
import javafx.scene.layout.GridPane;

public interface ViewRootsModel
{
    GridPane[] getAllViews();
    ResponsiveController[] getAllViewControllers();
    void addViewRootToList(int atIndex, GridPane view);
    MainWindowController getMainWindowController();
    void setMainWindowController(MainWindowController mainWindowController);
}
