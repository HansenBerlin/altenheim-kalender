package com.altenheim.kalender.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;

public class UpdateViewController
{   
    private GridPane planner;
    private GridPane search;
    private SearchViewController searchViewController;
    private PlannerViewController plannerViewController;

    public UpdateViewController(SearchViewController searchViewController, PlannerViewController plannerViewController, GridPane planner, GridPane search)
    {
        this.searchViewController = searchViewController;
        this.plannerViewController = plannerViewController;
        this.planner = planner;
        this.search = search;
    }    

    public GridPane getSearchView()
    {
        return search;
    }

    public GridPane getPlannerView()
    {
        return planner;
    }

    public void setupNodes() throws IOException 
    {
        var loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/plannerView.fxml"));
        loader.setController(plannerViewController); 
        planner = loader.load();

        var loader2 = new FXMLLoader();
        loader2.setLocation(getClass().getResource("/searchView.fxml"));
        loader2.setController(searchViewController); 
        search = loader2.load();
    }
}