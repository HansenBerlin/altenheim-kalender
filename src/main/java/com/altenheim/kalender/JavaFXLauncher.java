package com.altenheim.kalender;

import com.altenheim.kalender.controller.factories.InjectorFactoryImpl;
import com.altenheim.kalender.interfaces.factorys.InjectorFactory;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class JavaFXLauncher extends Application 
{
    @Override
    public void start(Stage primaryStage) throws Exception 
    {
        InjectorFactory objectFactory = new InjectorFactoryImpl();
        objectFactory.createServices();
        var guiSetup = objectFactory.getGuiController();
        guiSetup.init();
        var mainWindowController = objectFactory.getMainWindowController();
        var jmetro = guiSetup.getJMetroStyle();
        mainWindowController.initJFXObjects(primaryStage, jmetro);        

        var loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/mainView.fxml"));
        loader.setController(mainWindowController);

        Parent root = loader.load();
        var scene = new Scene(root);
        jmetro.setScene(scene);

        guiSetup.setupColorMode();

        primaryStage.setScene(scene);
        primaryStage.setTitle("Smart Planner HWR");
        primaryStage.setMaximized(true);        

        var initialSettingsLoader = objectFactory.getInitialSettingsLoader();
        initialSettingsLoader.initializeSettings();
        initialSettingsLoader.initialValidationCheck();
        
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() 
        {
            public void handle(WindowEvent we) 
            {              
                System.exit(0);
            }
        });

        var image = new Image(getClass().getResource("/Penaut.png").toString());
        primaryStage.getIcons().add(image);

        //objectFactory.searchVCt.initialize2();
        
        primaryStage.show();
        guiSetup.registerCalendars();

        
    }

    public static void main(String[] args) 
    {
        launch(args);
    }    
}