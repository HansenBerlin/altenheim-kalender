package com.altenheim.kalender.controller.logicController;

import com.altenheim.kalender.interfaces.*;
import com.altenheim.kalender.models.ContactModel;
import com.altenheim.kalender.models.SettingsModel;

public class InitialSetupController 
{
    private SettingsModel settings;
    private IIOController ioController;
    private IPopupViewController popup;
    private IWebsiteScraperController websiteScraper;
    private ISystemNotificationsController systemNotifications;
    private IEntryFactory entryFactory;
    private IImportController importController;
    private ContactModel contacts;

    public InitialSetupController(SettingsModel settings, IIOController ioController, IPopupViewController popup,
            IWebsiteScraperController websiteScraper, ISystemNotificationsController systemNotifications,
            IEntryFactory entryFactory, IImportController importController, ContactModel contacts)
    {
        this.settings = settings;
        this.ioController = ioController;
        this.popup = popup;
        this.websiteScraper = websiteScraper;
        this.systemNotifications = systemNotifications;
        this.entryFactory = entryFactory;
        this.importController = importController;
        this.contacts = contacts;
    }

    public void initializeSettings() 
    {
        ioController.createUserPath();
        try 
        {
            ioController.loadCalendarsFromFile(entryFactory, importController);
            ioController.loadContactsFromFile(contacts);
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        websiteScraper.startScraperTask();
        if (systemNotifications.initializeSystemTrayAccess()) 
        {
            systemNotifications.startNotificationTask();
        }
    }

    public void initialValidationCheck() 
    {
        if (ioController.loadHashedPassword().isBlank()) 
        {
            var userValidationPassed = validateUserPassword();
            if (!userValidationPassed) 
            {
                settings.useAdvancedFeatures = false;
                return;
            }
        }
        settings.decryptedPassword = ioController.loadHashedPassword();
        settings.useAdvancedFeatures = true;

    }

    private boolean validateUserPassword() 
    {
        var password = popup.showPasswordInputDialog();
        var security = new SecureAesController();
        var hashedPasswordAfterUserValidation = security.decrypt(password, "p:,-XQT3pj/^>)g_",
                SettingsModel.PASSWORDHASH);
        while (hashedPasswordAfterUserValidation.isBlank()) 
        {
            if (popup.isRevalidationWanted()) 
            {
                password = popup.showPasswordInputDialog();
                hashedPasswordAfterUserValidation = security.decrypt(password, "p:,-XQT3pj/^>)g_",
                        SettingsModel.PASSWORDHASH);
            } 
            else 
            {
                popup.showCancelDialog();
                return false;
            }
        }
        ioController.saveHashedPassword(hashedPasswordAfterUserValidation);
        popup.showConfirmationDialog();
        return true;
    }    
}
