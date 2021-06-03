package com.altenheim.kalender;

import java.io.File;
import java.io.FileInputStream;
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
        var fileInputStream = new FileInputStream(new File("src/main/java/resources/pocLoadSceneInScene.fxml"));        
        var jMetro = new JMetro(Style.LIGHT);
        loader.setController(new MainWindowController(primaryStage, jMetro));
        Parent root = loader.load(fileInputStream);  
        var scene = new Scene(root);

        //scene.getStylesheets().add(StartJFX.class.getResource("testDarkTheme.css").toExternalForm());
        //scene.getStylesheets().add(StartJFX.class.getResource("bootstrap3.css").toExternalForm());
        jMetro.setScene(scene);
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
