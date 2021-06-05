package com.altenheim.kalender;

import com.altenheim.kalender.controller.*;
import com.altenheim.kalender.resourceClasses.FxmlFiles;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;

public class StartJFX extends Application
{    
    @Override
    public void start(Stage primaryStage) throws Exception 
    {      
        var loader = new FXMLLoader();
        var jMetroStyle = new JMetro();        
        var childRoot = new AnchorPane();
        var allControllers = new ParentViewController[5];
        var allViews = new ArrayList<GridPane>();
        
        
        var guiSetup = new GuiSetupController(jMetroStyle, childRoot, allControllers, allViews);        
        guiSetup.init();            
        var mainController = new MainWindowController(primaryStage, jMetroStyle, allControllers, allViews);

        loader.setLocation(getClass().getResource(FxmlFiles.MAIN_VIEW));     
        loader.setController(mainController);        
        Parent root = loader.load();  
        var scene = new Scene(root);
        jMetroStyle.setScene(scene);
        
        primaryStage.setScene(scene);            
        primaryStage.setTitle("Smart Planner HWR"); 
        primaryStage.setMaximized(true);
        primaryStage.show();         
    }
    
    public static void main(String[] args) throws IOException, InterruptedException, URISyntaxException
    {
        launch(args);    
    }
}
