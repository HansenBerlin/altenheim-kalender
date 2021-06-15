package com.altenheim.kalender.controller.Factories;

import java.util.ArrayList;
import com.altenheim.kalender.controller.logicController.*;
import com.altenheim.kalender.controller.viewController.*;
import com.altenheim.kalender.interfaces.*;
import com.altenheim.kalender.models.*;
import jfxtras.styles.jmetro.JMetro;

public class InjectorFactory
{    
    private GuiSetupController guiSetup;
    private IViewRootsModel allViews;
    private JMetro jMetroStyle;

    public GuiSetupController getGuiController() { return guiSetup; }
    public IViewRootsModel getAllViews() { return allViews; }
    public JMetro getJMetroSetup() { return jMetroStyle; }

    public void createServices() throws Exception 
    {     
        jMetroStyle = new JMetro();   
        var customCalendarView = new CalendarViewOverride();       
        var mailTemplates = new MailTemplateModel();
        var contacts = new ArrayList<ContactModel>();
        var settings = new SettingsModel();

        IGoogleAPIController apiCt = new GoogleAPIController();
        IMailCreationController mailCreationCt = new MailCreationController(); 
        ICalendarEntriesModel calendarEntriesModel = new CalendarEntriesModel();
        IContactFactory contactFactory = new ContactFactory(contacts);
        IWebsiteScraperController websiteCt = new WebsiteScraperController(settings);
        ISmartSearchController smartSearch = new SmartSearchController(calendarEntriesModel);
        IAppointmentEntryFactory appointmentEntryCreator = new EntryFactory(calendarEntriesModel, customCalendarView, contacts);
        IExportController exportCt = new ExportController(appointmentEntryCreator, customCalendarView, contacts, settings);
        IIOController ioCt = new IOController(appointmentEntryCreator, customCalendarView, contacts, settings);
        IImportController importCt = new ImportController(calendarEntriesModel, websiteCt, appointmentEntryCreator, customCalendarView, contacts, settings);        
       
        var settingsVCt = new SettingsViewController(settings);        
        var statsVCt = new StatsViewController(contacts, calendarEntriesModel);
        var contactsVCt = new ContactsViewController(contacts, contactFactory, apiCt, ioCt);
        var mailVCt = new MailTemplateViewController(ioCt, settings, mailCreationCt, contacts);
        var searchVCt = new SearchViewController(smartSearch, appointmentEntryCreator, contacts, contactFactory, mailTemplates, settings, apiCt, ioCt);
        var plannerVCt = new PlannerViewController(calendarEntriesModel, appointmentEntryCreator, importCt, exportCt, customCalendarView);
        allViews = new ViewRootsModel(plannerVCt, searchVCt, statsVCt, contactsVCt, mailVCt, settingsVCt);        
        guiSetup = new GuiSetupController(jMetroStyle, allViews);

        guiSetup.init();        
        ioCt.loadCalendarsFromFile();     
        settings.addPropertyChangeListener(new ChangeListener());
        //websiteCt.startScraperTask();
    }      
}
