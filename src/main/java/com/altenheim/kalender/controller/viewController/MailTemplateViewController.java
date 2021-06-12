package com.altenheim.kalender.controller.viewController;

import java.util.List;

import com.altenheim.kalender.interfaces.IIOController;
import com.altenheim.kalender.interfaces.IMailCreationController;
import com.altenheim.kalender.interfaces.ISettingsController;
import com.altenheim.kalender.models.ContactModel;

public class MailTemplateViewController extends ResponsiveController
{ 
    private IIOController ioController;
    private ISettingsController settings;
    private IMailCreationController mailController;
    private List<ContactModel> contacts;

    public MailTemplateViewController(IIOController ioController, ISettingsController settings, 
        IMailCreationController mailController, List<ContactModel> contacts)
    {
        this.ioController = ioController;
        this.settings = settings;
        this.mailController = mailController;
        this.contacts = contacts;
    }
    
    public void changeContentPosition() 
    {
        
    }    
}


