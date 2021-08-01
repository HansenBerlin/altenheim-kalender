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
    private GuiUpdateController guiSetup;
    private JMetro jMetroStyle;
    private InitialSetupController initialSettingsLoader;
    private CustomViewOverride customCalendarView;
    private SettingsModel settings;
    private IIOController ioCt;
    public IIOController getIOController() { return ioCt; }
    public CustomViewOverride getCustomCalendarView() { return customCalendarView; }
    public GuiUpdateController getGuiController() { return guiSetup; }
    public IViewRootsModel getAllViews() { return allViews; }
    public JMetro getJMetroSetup() { return jMetroStyle; }
    public InitialSetupController getInitialSettingsLoader() { return initialSettingsLoader; }
    public SettingsModel getSettingsModel() {return settings; }

    public void createServices() 
    {     
        jMetroStyle = new JMetro();   
        var jsonParser = new JsonParser();
        
        settings = new SettingsModel();
        var contacts = new ContactModel();
        var settingsFile = new File("userFiles/userSettings/settingsTest.file");   
        if (settingsFile.exists())        
            settings.readSimpleProperties();
        customCalendarView = new CustomViewOverride(settings.cssMode);
                

        IExportController exportCt = new ExportController(settings);
        IImportController importCt = new ImportController(settings);
        ioCt = new IOController(settings, contacts, exportCt, importCt, null);
        var mailTemplates = ioCt.loadMailTemplatesFromFile();
        IComboBoxFactory comboBoxFactory = new ComboBoxFactory();
        IAnimationController animationController = new AnimationController();
        IPopupViewController popupViewController = new PopupViewsController();
        IGoogleAPIController apiCt = new GoogleAPIController(settings, jsonParser);
        IMailCreationController mailCreationCt = new MailCreationController(mailTemplates);       
        ICalendarEntriesModel calendarEntriesModel = new CalendarEntriesModel(customCalendarView);
        IDateSuggestionController dateSuggestionController = new DateSuggestionController();
        ISmartSearchController smartSearch = new SmartSearchController(calendarEntriesModel);
        IEntryFactory entryFactory = new EntryFactory(calendarEntriesModel, customCalendarView, ioCt, settings);
        ioCt.addEntryFactory(entryFactory);
        IWebsiteScraperController websiteCt = new WebsiteScraperController(settings, importCt, entryFactory);
        
        var plannerVCt = new PlannerViewController(customCalendarView, ioCt, entryFactory, popupViewController);
        var contactsVCt = new ContactsViewController(ioCt);
        var settingsVCt = new SettingsViewController(settings, importCt, entryFactory, exportCt, calendarEntriesModel, comboBoxFactory, popupViewController, ioCt);
        var mailVCt = new MailTemplateViewController(mailTemplates, comboBoxFactory, ioCt);
        var searchVCt = new SearchViewController(smartSearch, entryFactory, mailCreationCt, settings, apiCt, ioCt, animationController, comboBoxFactory, dateSuggestionController, calendarEntriesModel);
        var systemNotificationsCt = new SystemNotificationsController(settings, calendarEntriesModel);
        allViews = new ViewRootsModel(plannerVCt, searchVCt, contactsVCt, mailVCt, settingsVCt);
        guiSetup = new GuiUpdateController(jMetroStyle, allViews);
        initialSettingsLoader = new InitialSetupController(settings, ioCt, popupViewController, websiteCt, systemNotificationsCt);
    }
    
}
