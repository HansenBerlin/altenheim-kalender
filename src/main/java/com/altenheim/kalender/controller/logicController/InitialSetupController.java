package com.altenheim.kalender.controller.logicController;

import java.io.IOException;
import com.altenheim.kalender.interfaces.*;
import com.altenheim.kalender.models.SettingsModel;

public class InitialSetupController 
{
    private SettingsModel settings;
    private IIOController ioController;
    private IPopupViewController popup;
    private IWebsiteScraperController websiteScraper;
    private ISystemNotificationsController systemNotifications;

    public InitialSetupController(SettingsModel settings, IIOController ioController, IPopupViewController popup,
            IWebsiteScraperController websiteScraper, ISystemNotificationsController systemNotifications) 
    {
        this.settings = settings;
        this.ioController = ioController;
        this.popup = popup;
        this.websiteScraper = websiteScraper;
        this.systemNotifications = systemNotifications;
    }

    public void initializeSettings() 
    {
        ioController.createUserPath();
        ioController.loadCalendarsFromFile();
        try 
        {
            ioController.loadContactsFromFile();
        } 
        catch (ClassNotFoundException | IOException e) 
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
                settings.setAdvancedFeaturesFlag(false);
                return;
            }
        }
        settings.setDecryptedPasswordHash(ioController.loadHashedPassword());
        settings.setAdvancedFeaturesFlag(true);

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
