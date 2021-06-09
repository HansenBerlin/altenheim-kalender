package com.altenheim.kalender;

import com.altenheim.kalender.controller.viewController.MainWindowController;
import com.altenheim.kalender.resourceClasses.FxmlFiles;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class JavaFXLauncher extends Application
{
    @Override
    public void start(Stage primaryStage) throws Exception 
    { 
        var objectFactory = new ObjectFactory();
        objectFactory.createServices();
        var jMetroStyle = objectFactory.getJMetroSetup();
        var guiSetup = objectFactory.getGuiController();
        var mainController = new MainWindowController(primaryStage, objectFactory.getAllViews(), guiSetup);

        var loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(FxmlFiles.MAIN_VIEW));     
        loader.setController(mainController);        
        
        Parent root = loader.load();        
        var scene = new Scene(root);
        jMetroStyle.setScene(scene);
        guiSetup.setupColorMode();   
        
        primaryStage.setScene(scene);            
        primaryStage.setTitle("Smart Planner HWR"); 
        primaryStage.setMaximized(true);
        primaryStage.show();  
    }

    public static void main(String[] args)
    {
        launch(args);    
    }

    
    
}
