package com.altenheim.kalender.implementations.controller.factories;

import com.altenheim.kalender.interfaces.factorys.ComboBoxFactory;
import com.altenheim.kalender.interfaces.factorys.EntryFactory;
import com.altenheim.kalender.interfaces.factorys.InitialSetupController;
import com.altenheim.kalender.interfaces.factorys.InjectorFactory;
import com.altenheim.kalender.interfaces.logicController.*;
import com.altenheim.kalender.interfaces.viewController.CalendarEntriesController;
import com.altenheim.kalender.interfaces.models.ContactModel;
import com.altenheim.kalender.interfaces.models.MailTemplateModel;
import com.altenheim.kalender.interfaces.models.SettingsModel;
import com.altenheim.kalender.interfaces.viewController.AnimationController;
import com.altenheim.kalender.interfaces.viewController.GuiUpdateController;
import com.altenheim.kalender.interfaces.viewController.PopupViewController;
import jfxtras.styles.jmetro.JMetro;
import com.altenheim.kalender.implementations.controller.logicController.*;
import com.altenheim.kalender.implementations.controller.viewController.*;
import com.altenheim.kalender.implementations.controller.models.*;

public class InjectorFactoryImpl implements InjectorFactory
{   
    private GuiUpdateController guiSetup;
    private MainWindowController mainWindowController;
    private InitialSetupController initialSettingsLoader;
    public GuiUpdateController getGuiController() { return guiSetup; }
    public InitialSetupController getInitialSettingsLoader() { return initialSettingsLoader; }
    public MainWindowController getMainWindowController() { return mainWindowController; }

    public void createServices() 
    {     
        JsonParser jsonParser = new JsonParserImpl();
        SettingsModel settings = new SettingsModelImpl();
        settings.loadSettings();
        var jMetroStyle = new JMetro();
        jMetroStyle.setStyle(settings.getCssStyle());

        var customCalendarView = new CustomViewOverride(settings);
        CalendarEntriesController calendarEntriesController = new CalendarEntriesControllerImpl(customCalendarView);
        ContactModel contacts = new ContactModelImpl();
        ComboBoxFactory comboBoxFactory = new ComboBoxFactoryImpl();
        DecryptionController encryptionController = new DecryptionControllerImpl();
        ExportController exportCt = new ExportControllerImpl(settings);
        EntryFactory entryFactory = new EntryFactoryImpl(calendarEntriesController, customCalendarView);
        ImportController importCt = new ImportControllerImpl(entryFactory);
        IOController ioCt = new IOControllerImpl(settings, exportCt, importCt);
        entryFactory.addIOController(ioCt);
        AnimationController animationController = new AnimationControllerImpl();
        GoogleAPIController apiCt = new GoogleAPIControllerImpl(jsonParser, encryptionController);
        MailTemplateModel mailTemplates = ioCt.loadMailTemplatesFromFile();
        MailClientAccessController mailCreationCt = new MailClientAccessControllerImpl(mailTemplates);
        DateSuggestionController dateSuggestionController = new DateSuggestionControllerImpl();
        SmartSearchController smartSearch = new SmartSearchControllerImpl(calendarEntriesController, entryFactory);
        UrlRequestController urlRequestController = new UrlRequestControllerImpl(settings, importCt);
        PopupViewController popupViewController = new PopupViewsControllerImpl(settings, importCt, exportCt);

        var plannerVCt = new PlannerViewController(ioCt, entryFactory, popupViewController, calendarEntriesController);
        var contactsVCt = new ContactsViewController(ioCt, contacts);
        var settingsVCt = new SettingsViewController(settings, entryFactory, calendarEntriesController, comboBoxFactory, 
                popupViewController, urlRequestController);
        var mailVCt = new MailTemplateViewController(mailTemplates, comboBoxFactory, ioCt);
        var searchVCt = new SearchViewController(apiCt, calendarEntriesController, animationController, comboBoxFactory, 
                popupViewController, mailCreationCt, entryFactory, smartSearch, dateSuggestionController);
        var systemNotificationsCt = new SystemTrayTrayNotificationControllerImpl(settings, calendarEntriesController);
        var allViews = new ViewRootsModelImpl(plannerVCt, searchVCt, contactsVCt, mailVCt, settingsVCt);
        mainWindowController = new MainWindowController(allViews, customCalendarView, settings);
        allViews.setMainWindowController(mainWindowController);
        guiSetup = new GuiUpdateControllerImpl(jMetroStyle, allViews);
        initialSettingsLoader = new InitialSetupControllerImpl(ioCt, popupViewController, urlRequestController,
                systemNotificationsCt, entryFactory, contacts);
    }
        
}
