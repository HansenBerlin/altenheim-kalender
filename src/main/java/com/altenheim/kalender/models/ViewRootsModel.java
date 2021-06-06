package com.altenheim.kalender.models;

import com.altenheim.kalender.controller.*;
import com.altenheim.kalender.interfaces.ViewRootsInterface;
import javafx.scene.layout.GridPane;

public class ViewRootsModel implements ViewRootsInterface
{
    private GridPane[] allViews;
    private ResponsiveController[] allControllers;   

    public ViewRootsModel(PlannerViewController plannerViewController, SearchViewController searchViewController)
    {
        allViews = new GridPane[2];
        allControllers = new ResponsiveController[2];        
        allControllers[0] = plannerViewController;
        allControllers[1] = searchViewController;
    }

    public GridPane[] getAllViews() { return allViews; }
    public ResponsiveController[] getAllViewControllers() { return allControllers; }   
    public void addViewRootToList(int atIndex, GridPane view) { allViews[atIndex] = view; }
}
