package com.altenheim.kalender.controller.Factories;

import java.io.File;
import jfxtras.styles.jmetro.JMetro;
import com.altenheim.kalender.controller.logicController.*;
import com.altenheim.kalender.controller.viewController.*;
import com.altenheim.kalender.interfaces.*;
import com.altenheim.kalender.models.*;
import com.altenheim.kalender.resourceClasses.StylePresets;

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

    public void createServices() throws Exception 
    {     
        jMetroStyle = new JMetro();   
        var jsonParser = new JsonParser();
        
        settings = new SettingsModel();
        var mailTemplates = new MailTemplateModel();
        var contacts = new ContactModel();

        var settingsFile = new File("userFiles/settingsTest.file");    
        if (settingsFile.exists())        
            settings.readSimpleProperties();
        customCalendarView =new CustomViewOverride(settings.cssMode);
                

        IComboBoxFactory comboBoxFactory = new ComboBoxFactory();
        IAnimationController animationController = new AnimationController();
        IPopupViewController popupViewController = new PopupViewsController();
        IGoogleAPIController apiCt = new GoogleAPIController(settings, jsonParser);
        IMailCreationController mailCreationCt = new MailCreationController(mailTemplates);       
        ICalendarEntriesModel calendarEntriesModel = new CalendarEntriesModel();
        IDateSuggestionController dateSuggestionController = new DateSuggestionController();
        IImportController importCt = new ImportController(settings);
        ISmartSearchController smartSearch = new SmartSearchController(calendarEntriesModel);
        IExportController exportCt = new ExportController(settings, calendarEntriesModel);
        IEntryFactory entryFactory = new EntryFactory(calendarEntriesModel, customCalendarView);
        IWebsiteScraperController websiteCt = new WebsiteScraperController(settings, importCt, entryFactory);
        ioCt = new IOController(entryFactory, settings, mailTemplates, calendarEntriesModel, contacts, calendarEntriesModel, exportCt, importCt, entryFactory, customCalendarView);

        
        var contactsVCt = new ContactsViewController(apiCt, ioCt);
        var plannerVCt = new PlannerViewController(calendarEntriesModel, entryFactory, importCt, exportCt, customCalendarView, popupViewController);
        var settingsVCt = new SettingsViewController(settings, importCt, entryFactory, exportCt, calendarEntriesModel, comboBoxFactory, popupViewController, ioCt);
        var mailVCt = new MailTemplateViewController(ioCt, settings, mailTemplates, mailCreationCt, mailTemplates, comboBoxFactory);
        var searchVCt = new SearchViewController(smartSearch, entryFactory, mailTemplates, settings, apiCt, ioCt, animationController, comboBoxFactory, dateSuggestionController);
        var systemNotificationsCt = new SystemNotificationsController(settings, calendarEntriesModel);
        allViews = new ViewRootsModel(plannerVCt, searchVCt, contactsVCt, mailVCt, settingsVCt);
        guiSetup = new GuiUpdateController(jMetroStyle, allViews);
        initialSettingsLoader = new InitialSetupController(settings, ioCt, popupViewController, websiteCt, systemNotificationsCt);
    }
}