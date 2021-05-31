package com.altenheim.kalender.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javafx.application.Platform;
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
        var fileInputStream = new FileInputStream(new File("src/main/java/resources/plannerView.fxml")); 
        loader.setController(plannerViewController); 
        planner = loader.load(fileInputStream);
        var fileInputStream2 = new FileInputStream(new File("src/main/java/resources/searchView.fxml")); 
        var loader2 = new FXMLLoader();
        loader2.setController(searchViewController); 
        search = loader2.load(fileInputStream2);
    }
}