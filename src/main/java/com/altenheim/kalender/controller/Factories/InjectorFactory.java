package com.altenheim.kalender.controller.Factories;

import java.io.File;
import jfxtras.styles.jmetro.JMetro;
import com.altenheim.kalender.controller.logicController.*;
import com.altenheim.kalender.controller.viewController.*;
import com.altenheim.kalender.interfaces.*;
import com.altenheim.kalender.models.*;

public class InjectorFactory
{    
    private IViewRootsModel allViews;
    private IIOController ioCt;
    private GuiUpdateController guiSetup;
    private JMetro jMetroStyle;
    private InitialSetupController initialSettingsLoader;
    private CustomViewOverride customCalendarView;
    private SettingsModel settings;
    public IIOController getIOController() { return ioCt; }
    public IViewRootsModel getAllViews() { return allViews; }
    public CustomViewOverride getCustomCalendarView() { return customCalendarView; }
    public GuiUpdateController getGuiController() { return guiSetup; }
    public JMetro getJMetroSetup() { return jMetroStyle; }
    public InitialSetupController getInitialSettingsLoader() { return initialSettingsLoader; }
    public SettingsModel getSettingsModel() { return settings; }

    public void createServices() 
    {     
        jMetroStyle = new JMetro();   
        var jsonParser = new JsonParser();
        
        ioCt = new IOController();
        settings = ioCt.restoreSettings();
        //var settingsFile = new File("userFiles/userSettings/settingsTest.file");   
        //if (settingsFile.exists())        
          //  settings.readSimpleProperties();
        ioCt.addSettingsModel(settings);
        //settings.readSimpleProperties();
        customCalendarView = new CustomViewOverride(settings.cssMode);                
        
        var contacts = new ContactModel();
        var mailTemplates = ioCt.loadMailTemplatesFromFile();
        IExportController exportCt = new ExportController(settings);
        IImportController importCt = new ImportController(settings);
        IComboBoxFactory comboBoxFactory = new ComboBoxFactory();
        IAnimationController animationController = new AnimationController();
        IPopupViewController popupViewController = new PopupViewsController(settings);
        IGoogleAPIController apiCt = new GoogleAPIController(settings, jsonParser);
        IMailCreationController mailCreationCt = new MailCreationController(mailTemplates);       
        ICalendarEntriesModel calendarEntriesModel = new CalendarEntriesModel(customCalendarView);
        IDateSuggestionController dateSuggestionController = new DateSuggestionController();
        ISmartSearchController smartSearch = new SmartSearchController(calendarEntriesModel);
        IEntryFactory entryFactory = new EntryFactory(calendarEntriesModel, customCalendarView, ioCt, settings, exportCt);
        //ioCt.addEntryFactory(entryFactory);
        IWebsiteScraperController websiteCt = new WebsiteScraperController(settings, importCt, entryFactory);
        
        var plannerVCt = new PlannerViewController(customCalendarView, ioCt, entryFactory, popupViewController, importCt, calendarEntriesModel, exportCt);
        var contactsVCt = new ContactsViewController(ioCt, contacts);
        var settingsVCt = new SettingsViewController(settings, importCt, entryFactory, exportCt, calendarEntriesModel, comboBoxFactory, popupViewController, ioCt);
        var mailVCt = new MailTemplateViewController(mailTemplates, comboBoxFactory, ioCt);
        var searchVCt = new SearchViewController(smartSearch, entryFactory, mailCreationCt, settings, apiCt, animationController, comboBoxFactory, popupViewController, dateSuggestionController, calendarEntriesModel);
        var systemNotificationsCt = new SystemNotificationsController(settings, calendarEntriesModel);
        allViews = new ViewRootsModel(plannerVCt, searchVCt, contactsVCt, mailVCt, settingsVCt);
        guiSetup = new GuiUpdateController(jMetroStyle, allViews);
        initialSettingsLoader = new InitialSetupController(settings, ioCt, popupViewController, websiteCt, systemNotificationsCt, entryFactory, importCt, contacts);
    }    
}