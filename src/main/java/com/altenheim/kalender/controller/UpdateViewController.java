package com.altenheim.kalender.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

public class UpdateViewController extends Task<AnchorPane> 
{   
    String fileName;
    AnchorPane node;

    public UpdateViewController()
    {
        node = new AnchorPane();
    }

    public void setFilename(String fileName)
    {
        this.fileName = fileName;
        
    }

    @Override protected AnchorPane call() throws IOException 
    {
        var loader = new FXMLLoader();
        var fileInputStream = new FileInputStream(new File("src/main/java/resources/" + fileName)); 

        Platform.runLater(new Runnable() 
        {
            @Override public void run() 
            {                
                try 
                {
                    node = loader.load(fileInputStream);
                } 
                catch (IOException e) 
                {
                    e.printStackTrace();
                }                
            }
        });

        return node;
    }
}