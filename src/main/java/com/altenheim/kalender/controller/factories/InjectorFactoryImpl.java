package com.altenheim.kalender.controller.factories;
import com.altenheim.kalender.interfaces.factorys.InitialSetupController;
import com.altenheim.kalender.interfaces.factorys.InjectorFactory;
import com.altenheim.kalender.interfaces.logicController.EncryptionController;
import com.altenheim.kalender.interfaces.models.CalendarEntriesModel;
import com.altenheim.kalender.interfaces.models.ContactModel;
import jfxtras.styles.jmetro.JMetro;
import com.altenheim.kalender.controller.logicController.*;
import com.altenheim.kalender.controller.viewController.*;
import com.altenheim.kalender.interfaces.*;
import com.altenheim.kalender.models.*;

public class InjectorFactoryImpl implements InjectorFactory
{   
    private GuiUpdateController guiSetup;
    private MainWindowController mainWindowController;
    private InitialSetupController initialSettingsLoader;
    private SettingsModelImpl settings;
    public GuiUpdateController getGuiController() { return guiSetup; }
    public InitialSetupController getInitialSettingsLoader() { return initialSettingsLoader; }
    public SettingsModelImpl getSettingsModel() { return settings; }
    public MainWindowController getMainWindowController() { return mainWindowController; }


    public void createServices() 
    {     
        var jsonParser = new JsonParserImpl();
        SettingsModel settings = new SettingsModelImpl();
        settings.loadSettings();
        var jMetroStyle = new JMetro();
        jMetroStyle.setStyle(settings.getCssStyle());
        
        var customCalendarView = new CustomViewOverride(settings);         
        ContactModel contacts = new ContactModelImpl();
        EncryptionController encryptionController = new EncryptionControllerImpl();
        ExportController exportCt = new ExportControllerImpl(settings);
        ImportController importCt = new ImportControllerImpl();
        IOController ioCt = new IOControllerImpl(settings, exportCt, importCt);
        MailTemplateModel mailTemplates = ioCt.loadMailTemplatesFromFile();
        ComboBoxFactory comboBoxFactory = new ComboBoxFactoryImpl();
        AnimationController animationController = new AnimationControllerImpl();
        IPopupViewController popupViewController = new PopupViewsController(settings, importCt, exportCt);
        GoogleAPIController apiCt = new GoogleAPIControllerImpl(jsonParser, encryptionController);
        MailClientAccessController mailCreationCt = new MailClientAccessControllerImpl(mailTemplates);
        CalendarEntriesModel calendarEntriesModel = new CalendarEntriesModelImpl(customCalendarView);
        DateSuggestionController dateSuggestionController = new DateSuggestionControllerImpl();
        ISmartSearchController smartSearch = new SmartSearchController(calendarEntriesModel);
        EntryFactory entryFactory = new EntryFactoryImpl(calendarEntriesModel, customCalendarView, ioCt);
        IWebsiteScraperController websiteCt = new WebsiteScraperController(settings, importCt, entryFactory);

        var plannerVCt = new PlannerViewController(ioCt, entryFactory, popupViewController, calendarEntriesModel);
        var contactsVCt = new ContactsViewController(ioCt, contacts);
        var settingsVCt = new SettingsViewController(settings, entryFactory, calendarEntriesModel, comboBoxFactory, popupViewController);
        var mailVCt = new MailTemplateViewController(mailTemplates, comboBoxFactory, ioCt);
        var searchVCt = new SearchViewController(apiCt, calendarEntriesModel, animationController, comboBoxFactory, popupViewController,
            mailCreationCt, entryFactory, smartSearch, dateSuggestionController);
        var systemNotificationsCt = new SystemNotificationsController(settings, calendarEntriesModel);
        var allViews = new ViewRootsModelImpl(plannerVCt, searchVCt, contactsVCt, mailVCt, settingsVCt);
        mainWindowController = new MainWindowController(allViews, customCalendarView, settings);
        allViews.setMainWindowController(mainWindowController);
        guiSetup = new GuiUpdateController(jMetroStyle, allViews, settings);
        initialSettingsLoader = new InitialSetupControllerImpl(settings, ioCt, popupViewController, websiteCt,
                systemNotificationsCt, entryFactory, contacts);
    }    
}