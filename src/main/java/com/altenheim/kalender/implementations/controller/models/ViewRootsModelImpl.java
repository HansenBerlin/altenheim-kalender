package com.altenheim.kalender.implementations.controller.models;

import com.altenheim.kalender.implementations.controller.viewController.*;
import com.altenheim.kalender.interfaces.models.ViewRootsModel;
import javafx.scene.layout.GridPane;

public class ViewRootsModelImpl implements ViewRootsModel
{
    private final GridPane[] allViews;
    private final ResponsiveController[] allControllers;
    private MainWindowController mainWindowController;

    public ViewRootsModelImpl(PlannerViewController plannerCt, SearchViewController searchViewCt, ContactsViewController contactsCt,
                              MailTemplateViewController mailCt, SettingsViewController settingsCt)
    {
        ResponsiveController[] allCt = { plannerCt, searchViewCt, contactsCt, mailCt, settingsCt };
        allViews = new GridPane[allCt.length];
        allControllers = allCt;
    }

    public GridPane[] getAllViews() { return allViews; }
    public void addViewRootToList(int atIndex, GridPane view) { allViews[atIndex] = view; } 
    public ResponsiveController[] getAllViewControllers() { return allControllers; }
    public MainWindowController getMainWindowController() { return mainWindowController; }
    public void setMainWindowController(MainWindowController mainWindowController) { this.mainWindowController = mainWindowController; } 

}
