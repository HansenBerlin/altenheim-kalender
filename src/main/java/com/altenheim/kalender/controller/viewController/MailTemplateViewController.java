package com.altenheim.kalender.controller.viewController;

import com.altenheim.kalender.interfaces.IIOController;
import com.altenheim.kalender.interfaces.IMailCreationController;
import com.altenheim.kalender.interfaces.ISettingsController;

public class MailTemplateViewController extends ResponsiveController
{ 
    private IIOController ioController;
    private ISettingsController settings;
    private IMailCreationController mailController;

    public MailTemplateViewController(IIOController ioController, ISettingsController settings, IMailCreationController mailController)
    {
        this.ioController = ioController;
        this.settings = settings;
        this.mailController = mailController;
    }
    
    public void changeContentPosition() 
    {
        
    }    
}


