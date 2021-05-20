import controller.*;
import interfaces.*;
import views.*;
import models.CalendarEntriesModel;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application
{    
    @Override
    public void start(Stage primaryStage) throws Exception 
    {        
        Parent root = FXMLLoader.load(getClass().getResource("prototypeUI.fxml"));
        primaryStage.setScene(new Scene(root));    
        primaryStage.setTitle("Kalender Prototype"); 
        primaryStage.show(); 
    }
    
    public static void main(String[] args) throws IOException, InterruptedException, URISyntaxException
    {
        launch(args);        
    }
}




