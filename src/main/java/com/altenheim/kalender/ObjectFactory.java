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
    JMetro jMetroStyle = new JMetro();

    protected GuiSetupController getGuiController() { return guiSetup; }
    protected ViewRootsInterface getAllViews() { return allViews; }
    protected JMetro getJMetroSetup() { return jMetroStyle; }

    public void createServices() throws Exception 
    {        
        ICalendarEntriesModel calendarEntriesModel = new CalendarEntriesModel();
        IAppointmentEntryFactory appointmentEntryCreator = new AppointmentEntryFactory(calendarEntriesModel);
        appointmentEntryCreator.createTestCalendar();
        ISmartSearchController smartSearch = new SmartSearchController(calendarEntriesModel);

        var exportCt = new ExportController();
        var ioCt = new IOController();
        var apiCt = new GoogleAPIController();
        var mailCreationCt = new MailCreationController();
        var websiteCt = new WebsiteScraperController();
        var settingsCt = new SettingsController(ioCt, exportCt);
        var importCt = new ImportController(calendarEntriesModel, websiteCt);

        var contacts = new ContactsModel();
        var mailTemplates = new MailTemplateModel();
        
        var plannerVCt = new PlannerViewController(calendarEntriesModel, appointmentEntryCreator, importCt, exportCt);
        var searchVCt = new SearchViewController(smartSearch, appointmentEntryCreator, contacts, mailTemplates, settingsCt, apiCt);
        var statsVCt = new StatsViewController(contacts, calendarEntriesModel);
        var contactsVCt = new ContactsViewController(contacts, apiCt, ioCt);
        var mailVCt = new MailTemplateViewController(ioCt, settingsCt);
        var settingsVCt = new SettingsViewController();
        
        allViews = new ViewRootsModel(plannerVCt, searchVCt, statsVCt, contactsVCt, mailVCt, settingsVCt);        
        guiSetup = new GuiSetupController(jMetroStyle, allViews);        
        guiSetup.init(); 
    }

    
    
    
      
}
