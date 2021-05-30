package com.altenheim.kalender.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;

public class UpdateViewController
{   
    private String fileName;
    private GridPane node;
    private SearchViewController searchViewController;
    private PlannerViewController plannerViewController;

    public UpdateViewController(SearchViewController searchViewController, PlannerViewController plannerViewController)
    {
        node = new GridPane();
        this.searchViewController = searchViewController;
        this.plannerViewController = plannerViewController;

    }

    public void setFilename(String fileName)
    {
        this.fileName = fileName;        
    }

    public GridPane getNode()
    {
        return node;
    }

    protected GridPane update(String controller) throws IOException 
    {
        var loader = new FXMLLoader();
        var fileInputStream = new FileInputStream(new File("src/main/java/resources/" + fileName)); 
        if (controller.equals("planner"))   
            loader.setController(plannerViewController); 
        else if (controller.equals("search"))   
            loader.setController(searchViewController); 
        node = loader.load(fileInputStream);
        return node;
    }
}