package com.altenheim.kalender.controller.Factories;

import java.util.ArrayList;
import com.altenheim.kalender.controller.logicController.*;
import com.altenheim.kalender.controller.viewController.*;
import com.altenheim.kalender.interfaces.*;
import com.altenheim.kalender.models.*;
import com.calendarfx.view.CalendarView;
import jfxtras.styles.jmetro.JMetro;

public class InjectorFactory
{    
    private GuiUpdateController guiSetup;
    private IViewRootsModel allViews;
    private JMetro jMetroStyle;
    private InitialSetupController initialSettingsLoader;    

    public GuiUpdateController getGuiController() { return guiSetup; }
    public IViewRootsModel getAllViews() { return allViews; }
    public JMetro getJMetroSetup() { return jMetroStyle; }
    public InitialSetupController getInitialSettingsLoader() { return initialSettingsLoader; }


    public void createServices() throws Exception 
    {     
        jMetroStyle = new JMetro();   
        var customCalendarView = new CalendarView();
        var mailTemplates = new ArrayList<MailTemplateModel>();
        var contacts = new ArrayList<ContactModel>();
        var settings = new SettingsModel();
        var jsonParser = new JsonParser();

        IGoogleAPIController apiCt = new GoogleAPIController(settings, jsonParser);
        IMailCreationController mailCreationCt = new MailCreationController(mailTemplates); 
        ICalendarEntriesModel calendarEntriesModel = new CalendarEntriesModel();
        IContactFactory contactFactory = new ContactFactory(contacts);
        ISmartSearchController smartSearch = new SmartSearchController(calendarEntriesModel);
        IEntryFactory entryFactory = new EntryFactory(calendarEntriesModel, customCalendarView, contacts);
        IIOController ioCt = new IOController(entryFactory, contacts, settings, mailTemplates, calendarEntriesModel);
        IExportController exportCt = new ExportController(settings, calendarEntriesModel);
        IImportController importCt = new ImportController(settings);
        IWebsiteScraperController websiteCt = new WebsiteScraperController(settings, importCt);

        var popupVCt = new PopupViewsController();
        var settingsVCt = new SettingsViewController(settings, importCt, entryFactory, exportCt, calendarEntriesModel, websiteCt, apiCt);
        var statsVCt = new StatsViewController(contacts, calendarEntriesModel);
        var contactsVCt = new ContactsViewController(contacts, contactFactory, apiCt, ioCt);
        var mailVCt = new MailTemplateViewController(ioCt, settings, mailCreationCt, contacts, mailTemplates);
        var searchVCt = new SearchViewController(smartSearch, entryFactory, contacts, contactFactory, mailTemplates, settings, apiCt, ioCt);
        var plannerVCt = new PlannerViewController(calendarEntriesModel, entryFactory, importCt, exportCt, customCalendarView);
        var systemNotificationsCt = new SystemNotificationsController(settings, calendarEntriesModel);
        allViews = new ViewRootsModel(plannerVCt, searchVCt, statsVCt, contactsVCt, mailVCt, settingsVCt);
        guiSetup = new GuiUpdateController(jMetroStyle, allViews);
        initialSettingsLoader = new InitialSetupController(settings, ioCt, popupVCt, websiteCt, systemNotificationsCt);
    }
}