package com.altenheim.kalender.models;

import com.altenheim.kalender.controller.viewController.*;
import com.altenheim.kalender.interfaces.ViewRootsModel;
import javafx.scene.layout.GridPane;

public class ViewRootsModelImpl implements ViewRootsModel
{
    private GridPane[] allViews;
    private ResponsiveController[] allControllers;
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