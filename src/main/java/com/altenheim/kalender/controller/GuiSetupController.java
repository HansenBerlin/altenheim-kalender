package com.altenheim.kalender.controller;

import com.altenheim.kalender.resourceClasses.FxmlFiles;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

public class GuiSetupController 
{
    private JMetro jMetroStyle;
    private AnchorPane root;
    private GridPane currentView;
    private PlannerViewController plannerViewController;
    private SearchViewController searchViewController;
    private ParentViewController[] allControllers;
    private List<GridPane> allViews;
    //private final String[] buttonCaptions = {"Planner", "Smart Search", "Stats", "Contacts", "Mailtemplates", "Settings"};
    //private List<Button> allMenuButtons;   

    public GuiSetupController(JMetro jMetroStyle, AnchorPane root, 
        GridPane currentView, ParentViewController[] allControllers) 
    {
        this.jMetroStyle = jMetroStyle;
        this.root = root;
        this.currentView = currentView; 
        this.allControllers = allControllers;
        allViews = new ArrayList<GridPane>();        
    }

    public void init() throws IOException
    {
        plannerViewController = new PlannerViewController(this.root);
        searchViewController = new SearchViewController(this.root);
        allControllers[0] = plannerViewController;
        allControllers[1] = searchViewController;  
        setupViews();
        initializeViews();
        setupColorPreferences();
    }

    public void setupViews() throws IOException 
    {
        for (int i = 0; i < FxmlFiles.ALL_FILES.length; i++) 
        {       
            var loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(FxmlFiles.ALL_FILES[i]));
            loader.setController(allControllers[i]); 
            allViews.add(loader.load());            
        }        
    }  
    
    private void initializeViews()
    {
        for (var view : allViews) 
        {
            view.setDisable(true);
            view.setVisible(false);
            root.getChildren().add(view);            
        }
        allViews.get(0).setVisible(true);
        allViews.get(0).setDisable(false);
        currentView = allViews.get(0);
    }    

    private void setupColorPreferences()
    {
        boolean isDarkmodeActive = false; //apäter aus gespeicherten settings abrufen
        if (isDarkmodeActive)
            jMetroStyle.setStyle(Style.DARK);
        else    
            jMetroStyle.setStyle(Style.LIGHT);
    }
}
