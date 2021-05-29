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
import com.altenheim.kalender.controller.MainWindowController;

public class StartJFX extends Application
{    
    @Override
    public void start(Stage primaryStage) throws Exception 
    {      
        //kann sein, dass im Build durch den default Zugriff auf ressources durch Maven die
        // untere Vorgehensweise Fehler schmeißt, also mal noch drinlassen bitte
        //FXMLLoader loader = new FXMLLoader(getClass().getResource("prototypeUI.fxml"));
        FXMLLoader loader = new FXMLLoader();
        FileInputStream fileInputStream = new FileInputStream(new File("src/main/java/resources/pocLoadSceneInScene.fxml"));        
        loader.setController(new MainWindowController());
        Parent root = loader.load(fileInputStream);     

        primaryStage.setScene(new Scene(root));            
        primaryStage.setTitle("Kalender Prototype"); 
        primaryStage.show();         
    }
    
    public static void main(String[] args) throws IOException, InterruptedException, URISyntaxException
    {
        launch(args);    
    }
}
