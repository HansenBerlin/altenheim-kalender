package com.altenheim.kalender;

import java.io.IOException;
import java.net.URISyntaxException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import com.altenheim.kalender.controller.MainViewController;

public class StartJFX extends Application
{    
    @Override
    public void start(Stage primaryStage) throws Exception 
    {      
        //FXMLLoader loader = new FXMLLoader(getClass().getResource("\\prototypeUI.fxml"));
        //var controller = new MainViewController();
        //loader.setController(controller);
        //Parent root = loader.load();

        StackPane root = new StackPane();
        var scene = new Scene(root);
        //primaryStage.setScene(new Scene(root));   
        primaryStage.setScene(scene); 
        primaryStage.setTitle("Kalender Prototype"); 
        primaryStage.show();         
    }
    
    public static void main(String[] args) throws IOException, InterruptedException, URISyntaxException
    {
        launch(args);    
    }
}
