package com.altenheim.kalender;

import com.altenheim.kalender.TempTestClasses.ReadWriteICal;
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
        
               
        var mailTemplates = new MailTemplateModel();
        var contacts = new ContactModel();
        var settings = new SettingsModel();
        var customCalendarView = new CalendarViewOverride();
        IExportController exportCt = new ExportController();
        IIOController ioCt = new IOController();
        ISettingsController settingsCt = new SettingsController(ioCt, exportCt);
        IGoogleAPIController apiCt = new GoogleAPIController();
        IMailCreationController mailCreationCt = new MailCreationController(); 
        IWebsiteScraperController websiteCt = new WebsiteScraperController(settings);
        IImportController importCt = new ImportController(calendarEntriesModel, websiteCt);        
        // TODO refactor und Implementierung Tests in richtige Konstruktoren
        var iowriteTest = new ReadWriteICal(calendarEntriesModel, customCalendarView);
        var searchVCt = new SearchViewController(smartSearch, appointmentEntryCreator, contacts, mailTemplates, settings, apiCt, ioCt, iowriteTest);
        var plannerVCt = new PlannerViewController(calendarEntriesModel, appointmentEntryCreator, importCt, exportCt, customCalendarView);
        var mailVCt = new MailTemplateViewController(ioCt, settingsCt, mailCreationCt);
        var statsVCt = new StatsViewController(contacts, calendarEntriesModel);
        var contactsVCt = new ContactsViewController(contacts, apiCt, ioCt);
        var settingsVCt = new SettingsViewController(settingsCt, settings);
        
        allViews = new ViewRootsModel(plannerVCt, searchVCt, statsVCt, contactsVCt, mailVCt, settingsVCt);        
        guiSetup = new GuiSetupController(jMetroStyle, allViews);

        guiSetup.init();
        ioCt.init(customCalendarView);
        iowriteTest.loadCalendars();
        settings.addPropertyChangeListener(new ChangeListener());
        //websiteCt.startScraperTask();
    }      
}
