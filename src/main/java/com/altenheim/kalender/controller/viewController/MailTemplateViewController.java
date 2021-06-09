package com.altenheim.kalender.controller.viewController;

import com.altenheim.kalender.interfaces.IIOController;
import com.altenheim.kalender.interfaces.ISettingsController;

public class MailTemplateViewController extends ResponsiveController
{ 
    private IIOController ioController;
    private ISettingsController settings;   
    public MailTemplateViewController(IIOController ioController, ISettingsController settings)
    {
        this.ioController = ioController;
        this.settings = settings;
    }
    
    public void changeContentPosition() 
    {
        
    }    
}


