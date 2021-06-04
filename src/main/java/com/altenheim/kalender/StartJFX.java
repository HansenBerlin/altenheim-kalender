package com.altenheim.kalender;

import java.io.IOException;
import java.net.URISyntaxException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
import com.altenheim.kalender.controller.MainWindowController;

public class StartJFX extends Application
{    
    @Override
    public void start(Stage primaryStage) throws Exception 
    {      
        var loader = new FXMLLoader();
        var jMetro = new JMetro(Style.LIGHT);
        
        loader.setLocation(getClass().getResource("/pocLoadSceneInScene.fxml"));     
        loader.setController(new MainWindowController(primaryStage, jMetro));
        
        Parent root = loader.load();  
        var scene = new Scene(root);

        jMetro.setScene(scene);
        //jMetro.getOverridingStylesheets().getClass().getResource("/rootcolors.css").toExternalForm();
        
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
