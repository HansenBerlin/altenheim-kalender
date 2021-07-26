package com.altenheim.kalender;

import com.altenheim.kalender.controller.Factories.InjectorFactory;
import com.altenheim.kalender.controller.viewController.MainWindowController;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class JavaFXLauncher extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        var objectFactory = new InjectorFactory();
        objectFactory.createServices();
        var guiSetup = objectFactory.getGuiController();
        guiSetup.init();
        var mainController = new MainWindowController(primaryStage, objectFactory.getAllViews(), guiSetup,
                objectFactory.getCustomCalendarView(), objectFactory.getSettingsModel());

        var loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/mainView.fxml"));
        loader.setController(mainController);

        Parent root = loader.load();
        var scene = new Scene(root);
        var jMetroStyle = objectFactory.getJMetroSetup();
        jMetroStyle.setScene(scene);
        guiSetup.setupColorMode();

        primaryStage.setScene(scene);
        primaryStage.setTitle("Smart Planner HWR");
        primaryStage.setMaximized(true);
        primaryStage.setMinHeight(720);
        primaryStage.setMinWidth(1080);

        var initialSettingsLoader = objectFactory.getInitialSettingsLoader();
        initialSettingsLoader.initializeSettings();
        initialSettingsLoader.initialValidationCheck();

        mainController.switchCssMode();

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                objectFactory.getIOController().writeCalendarFiles();
                System.exit(0);
            }
        });

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}