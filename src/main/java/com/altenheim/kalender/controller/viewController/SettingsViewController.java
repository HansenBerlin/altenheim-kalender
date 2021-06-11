package com.altenheim.kalender.controller.viewController;

import com.altenheim.kalender.interfaces.ISettingsController;
import com.altenheim.kalender.models.SettingsModel;

public class SettingsViewController extends ResponsiveController implements ISettingsController
{ 
    private ISettingsController settingsController;
    private SettingsModel settings;

    public SettingsViewController(ISettingsController settingsController, SettingsModel settings)
    {
        this.settingsController = settingsController;
        this.settings = settings;
    }
    
    public void changeContentPosition() 
    {
        
    }    
}


