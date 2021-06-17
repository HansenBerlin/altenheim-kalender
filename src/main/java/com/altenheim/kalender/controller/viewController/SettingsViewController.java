package com.altenheim.kalender.controller.viewController;

import com.altenheim.kalender.models.SettingsModel;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class SettingsViewController extends ResponsiveController
{ 
    private SettingsModel settings;
    @FXML
    private TextField Studiengang;

    @FXML
    private TextField Kurs;

    @FXML
    void save(ActionEvent event) {
        
    }
    public SettingsViewController(SettingsModel settings)
    {
        this.settings = settings;
    }
    
    public void changeContentPosition() 
    {
        
    }    
    

       
        
        
    
    
}


