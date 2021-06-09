package com.altenheim.kalender;

import com.altenheim.kalender.controller.logicController.*;
import com.altenheim.kalender.controller.viewController.*;
import com.altenheim.kalender.interfaces.*;
import com.altenheim.kalender.models.*;
import jfxtras.styles.jmetro.JMetro;

public class ObjectFactory
{    
    GuiSetupController guiSetup;
    ViewRootsInterface allViews;
    JMetro jMetroStyle;

    protected GuiSetupController getGuiController() { return guiSetup; }
    protected ViewRootsInterface getAllViews() { return allViews; }
    protected JMetro getJMetroSetup() { return jMetroStyle; }

    public void createServices() throws Exception 
    {     
        jMetroStyle = new JMetro();   
        ICalendarEntriesModel calendarEntriesModel = new CalendarEntriesModel();
        IAppointmentEntryFactory appointmentEntryCreator = new AppointmentEntryFactory(calendarEntriesModel);
        appointmentEntryCreator.createTestCalendar();
        ISmartSearchController smartSearch = new SmartSearchController(calendarEntriesModel);

        var websiteCt = new WebsiteScraperController();
        var exportCt = new ExportController();
        var importCt = new ImportController(calendarEntriesModel, websiteCt);
        var ioCt = new IOController();
        var settingsCt = new SettingsController(ioCt, exportCt);
        var apiCt = new GoogleAPIController();
        var mailCreationCt = new MailCreationController();

        var mailTemplates = new MailTemplateModel();
        var contacts = new ContactsModel();
        var settings = new SettingsModel();
        
        var searchVCt = new SearchViewController(smartSearch, appointmentEntryCreator, contacts, mailTemplates, settings, apiCt);
        var plannerVCt = new PlannerViewController(calendarEntriesModel, appointmentEntryCreator, importCt, exportCt);
        var mailVCt = new MailTemplateViewController(ioCt, settingsCt, mailCreationCt);
        var statsVCt = new StatsViewController(contacts, calendarEntriesModel);
        var contactsVCt = new ContactsViewController(contacts, apiCt, ioCt);
        var settingsVCt = new SettingsViewController(settingsCt, settings);
        
        allViews = new ViewRootsModel(plannerVCt, searchVCt, statsVCt, contactsVCt, mailVCt, settingsVCt);        
        guiSetup = new GuiSetupController(jMetroStyle, allViews);        
        guiSetup.init(); 
    }

    
    
    
      
}
