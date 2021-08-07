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

    //public SearchViewController searchVCt;

    public void createServices() 
    {     
        var jsonParser = new JsonParser();
        settings = new SettingsModel();
        settings.loadSettings();
        var jMetroStyle = new JMetro();
        jMetroStyle.setStyle(settings.getCssStyle());
        
        var customCalendarView = new CustomViewOverride(settings);         
        var contacts = new ContactModel();
        IExportController exportCt = new ExportController(settings);
        IImportController importCt = new ImportController();
        IIOController ioCt = new IOController(settings, exportCt, importCt);
        var mailTemplates = ioCt.loadMailTemplatesFromFile();
        IComboBoxFactory comboBoxFactory = new ComboBoxFactory();
        IAnimationController animationController = new AnimationController();
        IPopupViewController popupViewController = new PopupViewsController(settings, importCt, exportCt);
        IGoogleAPIController apiCt = new GoogleAPIController(settings, jsonParser);
        IMailCreationController mailCreationCt = new MailCreationController(mailTemplates);       
        ICalendarEntriesModel calendarEntriesModel = new CalendarEntriesModel(customCalendarView);
        IDateSuggestionController dateSuggestionController = new DateSuggestionController();
        ISmartSearchController smartSearch = new SmartSearchController(calendarEntriesModel);
        IEntryFactory entryFactory = new EntryFactory(calendarEntriesModel, customCalendarView, ioCt);
        IWebsiteScraperController websiteCt = new WebsiteScraperController(settings, importCt, entryFactory);
        //SearchViewRequestHandlerController requestHandler = new SearchViewRequestHandlerController(apiCt, calendarEntriesModel, 
          //  dateSuggestionController, smartSearch, entryFactory, popupViewController, mailCreationCt);

        var plannerVCt = new PlannerViewController(ioCt, entryFactory, popupViewController, calendarEntriesModel);
        var contactsVCt = new ContactsViewController(ioCt, contacts);
        var settingsVCt = new SettingsViewController(settings, entryFactory, calendarEntriesModel, comboBoxFactory, popupViewController);
        var mailVCt = new MailTemplateViewController(mailTemplates, comboBoxFactory, ioCt);
        //var searchTest = new SearchViewValidationController(apiCt, calendarEntriesModel, popupViewController, mailCreationCt, entryFactory);
        var searchVCt = new SearchViewController(apiCt, calendarEntriesModel, animationController, comboBoxFactory, popupViewController, 
            mailCreationCt, entryFactory, smartSearch, dateSuggestionController);
        var systemNotificationsCt = new SystemNotificationsController(settings, calendarEntriesModel);
        var allViews = new ViewRootsModel(plannerVCt, searchVCt, contactsVCt, mailVCt, settingsVCt);
        mainWindowController = new MainWindowController(allViews, customCalendarView, settings);
        allViews.setMainWindowController(mainWindowController);
        guiSetup = new GuiUpdateController(jMetroStyle, allViews, settings);
        initialSettingsLoader = new InitialSetupController(settings, ioCt, popupViewController, websiteCt, systemNotificationsCt, entryFactory, contacts);
    }    
}