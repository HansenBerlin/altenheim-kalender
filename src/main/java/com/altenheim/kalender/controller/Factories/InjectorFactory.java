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
    private IViewRootsModel allViews;
    private GuiUpdateController guiSetup;
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
        var mailTemplates = new MailTemplateModel();
        var contacts = new ArrayList<ContactModel>();
        var settings = new SettingsModel();

        IComboBoxFactory comboBoxFactory = new ComboBoxFactory(mailTemplates);
        IAnimationController animationController = new AnimationController();
        IPopupViewController popupViewController = new PopupViewsController();
        ICalendarEntriesModel calendarEntriesModel = new CalendarEntriesModel();
        IDateSuggestionController dateSuggestionController = new DateSuggestionController();
        IImportController importCt = new ImportController(settings);
        IContactFactory contactFactory = new ContactFactory(contacts);
        IGoogleAPIController apiCt = new GoogleAPIController(settings);
        IMailCreationController mailCreationCt = new MailCreationController(mailTemplates); 
        ISmartSearchController smartSearch = new SmartSearchController(calendarEntriesModel);
        IExportController exportCt = new ExportController(settings, calendarEntriesModel);
        IWebsiteScraperController websiteCt = new WebsiteScraperController(settings, importCt);
        IEntryFactory entryFactory = new EntryFactory(calendarEntriesModel, customCalendarView, contacts);
        IIOController ioCt = new IOController(entryFactory, contacts, settings, mailTemplates, calendarEntriesModel);

        var statsVCt = new StatsViewController(contacts, calendarEntriesModel);
        var contactsVCt = new ContactsViewController(contacts, contactFactory, apiCt, ioCt);
        var plannerVCt = new PlannerViewController(calendarEntriesModel, entryFactory, importCt, exportCt, customCalendarView, popupViewController);
        var settingsVCt = new SettingsViewController(settings, importCt, entryFactory, exportCt, calendarEntriesModel, websiteCt, popupViewController, comboBoxFactory);
        var mailVCt = new MailTemplateViewController(ioCt, settings, mailCreationCt, contacts, mailTemplates, comboBoxFactory);
        var searchVCt = new SearchViewController(smartSearch, entryFactory, contacts, contactFactory, mailTemplates, settings, 
            apiCt, ioCt, animationController, comboBoxFactory, dateSuggestionController);
        var systemNotificationsCt = new SystemNotificationsController(settings, calendarEntriesModel);
        allViews = new ViewRootsModel(plannerVCt, searchVCt, statsVCt, contactsVCt, mailVCt, settingsVCt);
        guiSetup = new GuiUpdateController(jMetroStyle, allViews);
        initialSettingsLoader = new InitialSetupController(settings, ioCt, popupViewController, websiteCt, systemNotificationsCt);
    }
}