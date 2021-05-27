package com.altenheim.kalender;

import java.io.IOException;
import java.net.URISyntaxException;

import com.altenheim.kalender.controller.MainViewController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class StartJFX extends Application
{    
    @Override
    public void start(Stage primaryStage) throws Exception 
    {      
        FXMLLoader loader = new FXMLLoader(getClass().getResource("prototypeUI.fxml"));
        var controller = new MainViewController();
        loader.setController(controller);
        Parent root = loader.load();

        //Parent root = FXMLLoader.load(getClass().getResource("prototypeUI.fxml"));
        primaryStage.setScene(new Scene(root));    
        primaryStage.setTitle("Kalender Prototype"); 
        primaryStage.show();         
    }
    
    public static void main(String[] args) throws IOException, InterruptedException, URISyntaxException
    {
        launch(args);    
    }
}
