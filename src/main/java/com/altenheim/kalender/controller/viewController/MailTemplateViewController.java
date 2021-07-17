package com.altenheim.kalender.controller.viewController;

import java.util.List;
import com.altenheim.kalender.interfaces.IIOController;
import com.altenheim.kalender.interfaces.IMailCreationController;
import com.altenheim.kalender.models.MailTemplateModel;
import com.altenheim.kalender.models.SettingsModel;

public class MailTemplateViewController extends ResponsiveController
{ 
    private IIOController ioController;
    private SettingsModel settings;
    private IMailCreationController mailController;
    private List<MailTemplateModel> mailTemplates;

    public MailTemplateViewController(IIOController ioController, SettingsModel settings, 
        IMailCreationController mailController, List<MailTemplateModel> mailTemplates)
    {
        this.ioController = ioController;
        this.settings = settings;
        this.mailController = mailController;
        this.mailTemplates = mailTemplates;
    }
    
    public void changeContentPosition(double width, double height) 
    {
        
    }    
}


