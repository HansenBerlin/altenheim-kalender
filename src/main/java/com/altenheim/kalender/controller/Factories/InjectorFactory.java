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
        var mailTemplates = new ArrayList<MailTemplateModel>();
        var contacts = new ArrayList<ContactModel>();
        var settings = new SettingsModel();

        IGoogleAPIController apiCt = new GoogleAPIController();
        IMailCreationController mailCreationCt = new MailCreationController(mailTemplates); 
        ICalendarEntriesModel calendarEntriesModel = new CalendarEntriesModel();
        IContactFactory contactFactory = new ContactFactory(contacts);
        ISmartSearchController smartSearch = new SmartSearchController(calendarEntriesModel);
        IEntryFactory entryFactory = new EntryFactory(calendarEntriesModel, customCalendarView, contacts);
        IIOController ioCt = new IOController(entryFactory, contacts, settings, mailTemplates, calendarEntriesModel);
        IExportController exportCt = new ExportController(entryFactory, contacts, settings, mailTemplates, calendarEntriesModel);
        IImportController importCt = new ImportController(entryFactory, contacts, settings, mailTemplates, calendarEntriesModel);
        IWebsiteScraperController websiteCt = new WebsiteScraperController(settings, importCt);


        var settingsVCt = new SettingsViewController(settings, importCt, entryFactory, exportCt, calendarEntriesModel, websiteCt, customCalendarView);
        var statsVCt = new StatsViewController(contacts, calendarEntriesModel);
        var contactsVCt = new ContactsViewController(contacts, contactFactory, apiCt, ioCt);
        var mailVCt = new MailTemplateViewController(ioCt, settings, mailCreationCt, contacts, mailTemplates);
        var searchVCt = new SearchViewController(smartSearch, entryFactory, contacts, contactFactory, mailTemplates, settings, apiCt, ioCt);
        var plannerVCt = new PlannerViewController(calendarEntriesModel, entryFactory, importCt, exportCt, customCalendarView);
        allViews = new ViewRootsModel(plannerVCt, searchVCt, statsVCt, contactsVCt, mailVCt, settingsVCt);        
        guiSetup = new GuiSetupController(jMetroStyle, allViews);

        guiSetup.init();
        //ioCt.loadCalendarsFromFile();
        settings.addPropertyChangeListener(new ChangeListener());
        websiteCt.startScraperTask();
        //entryFactory.createRandomCalendarList();
    }      
}
