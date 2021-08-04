package com.altenheim.kalender.controller.Factories;

import jfxtras.styles.jmetro.JMetro;
import com.altenheim.kalender.controller.logicController.*;
import com.altenheim.kalender.controller.viewController.*;
import com.altenheim.kalender.interfaces.*;
import com.altenheim.kalender.models.*;

public class InjectorFactory
{    
    
    private GuiUpdateController guiSetup;
    private MainWindowController mainWindowController;
    private InitialSetupController initialSettingsLoader;
    private SettingsModel settings;    
    public GuiUpdateController getGuiController() { return guiSetup; }
    public InitialSetupController getInitialSettingsLoader() { return initialSettingsLoader; }
    public SettingsModel getSettingsModel() { return settings; }
    public MainWindowController getMainWindowController() { return mainWindowController; }

    public void createServices() 
    {     
        var jsonParser = new JsonParser();
        settings = new SettingsModel();
        settings.loadSettings();
        var jMetroStyle = new JMetro();
        jMetroStyle.setStyle(settings.getCssStyle());
        System.out.println(settings.getCssStyle().toString());       
        
        var customCalendarView = new CustomViewOverride(settings);         
        var contacts = new ContactModel();
        IIOController ioCt = new IOController(settings);
        var mailTemplates = ioCt.loadMailTemplatesFromFile();
        IExportController exportCt = new ExportController(settings);
        IImportController importCt = new ImportController();
        IComboBoxFactory comboBoxFactory = new ComboBoxFactory();
        IAnimationController animationController = new AnimationController();
        IPopupViewController popupViewController = new PopupViewsController(settings);
        IGoogleAPIController apiCt = new GoogleAPIController(settings, jsonParser);
        IMailCreationController mailCreationCt = new MailCreationController(mailTemplates);       
        ICalendarEntriesModel calendarEntriesModel = new CalendarEntriesModel(customCalendarView);
        IDateSuggestionController dateSuggestionController = new DateSuggestionController();
        ISmartSearchController smartSearch = new SmartSearchController(calendarEntriesModel);
        IEntryFactory entryFactory = new EntryFactory(calendarEntriesModel, customCalendarView, ioCt, settings, exportCt);
        IWebsiteScraperController websiteCt = new WebsiteScraperController(settings, importCt, entryFactory);
        
        var plannerVCt = new PlannerViewController(customCalendarView, ioCt, entryFactory, popupViewController, importCt, calendarEntriesModel, exportCt);
        var contactsVCt = new ContactsViewController(ioCt, contacts);
        var settingsVCt = new SettingsViewController(settings, importCt, entryFactory, exportCt, calendarEntriesModel, comboBoxFactory, popupViewController);
        var mailVCt = new MailTemplateViewController(mailTemplates, comboBoxFactory, ioCt);
        var searchVCt = new SearchViewController(smartSearch, entryFactory, mailCreationCt, settings, apiCt, animationController, comboBoxFactory, popupViewController, dateSuggestionController, calendarEntriesModel);
        var systemNotificationsCt = new SystemNotificationsController(settings, calendarEntriesModel);
        var allViews = new ViewRootsModel(plannerVCt, searchVCt, contactsVCt, mailVCt, settingsVCt);
        mainWindowController = new MainWindowController(allViews, customCalendarView, settings);
        allViews.setMainWindowController(mainWindowController);
        guiSetup = new GuiUpdateController(jMetroStyle, allViews, settings);
        initialSettingsLoader = new InitialSetupController(settings, ioCt, popupViewController, websiteCt, systemNotificationsCt, entryFactory, importCt, contacts);
    }    
}