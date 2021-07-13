package com.altenheim.kalender.controller.viewController;

import java.util.List;
import com.altenheim.kalender.interfaces.IIOController;
import com.altenheim.kalender.interfaces.IMailCreationController;
import com.altenheim.kalender.models.ContactModel;
import com.altenheim.kalender.models.MailTemplateModel;
import com.altenheim.kalender.models.SettingsModel;

public class MailTemplateViewController extends ResponsiveController
{ 
    private IIOController ioController;
    private SettingsModel settings;
    private IMailCreationController mailController;
    private List<ContactModel> contacts;
    private List<MailTemplateModel> mailTemplates;

    public MailTemplateViewController(IIOController ioController, SettingsModel settings, 
        IMailCreationController mailController, List<ContactModel> contacts, List<MailTemplateModel> mailTemplates)
    {
        this.ioController = ioController;
        this.settings = settings;
        this.mailController = mailController;
        this.contacts = contacts;
        this.mailTemplates = mailTemplates;
    }
    
    public void changeContentPosition() 
    {
        
    }    
}


