package com.altenheim.kalender.models;

import com.altenheim.kalender.controller.viewController.*;
import com.altenheim.kalender.interfaces.ViewRootsInterface;
import javafx.scene.layout.GridPane;

public class ViewRootsModel implements ViewRootsInterface
{
    private GridPane[] allViews;
    private ResponsiveController[] allControllers;   

    public ViewRootsModel(PlannerViewController plannerCt, SearchViewController searchViewCt, StatsViewController statsCt,
        ContactsViewController contactsCt, MailTemplateViewController mailCt, SettingsViewController settingsCt)
    {
        ResponsiveController[] allCt = { plannerCt, searchViewCt, statsCt, contactsCt, mailCt, settingsCt };
        allViews = new GridPane[allCt.length];
        allControllers = allCt;
    }

    public GridPane[] getAllViews() { return allViews; }
    public ResponsiveController[] getAllViewControllers() { return allControllers; }   
    public void addViewRootToList(int atIndex, GridPane view) { allViews[atIndex] = view; }
}
