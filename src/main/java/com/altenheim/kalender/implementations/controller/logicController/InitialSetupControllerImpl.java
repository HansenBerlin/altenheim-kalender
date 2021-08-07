package com.altenheim.kalender.implementations.controller.logicController;

import com.altenheim.kalender.interfaces.factorys.EntryFactory;
import com.altenheim.kalender.interfaces.factorys.InitialSetupController;
import com.altenheim.kalender.interfaces.logicController.IOController;
import com.altenheim.kalender.interfaces.logicController.SystemTrayNotificationsController;
import com.altenheim.kalender.interfaces.logicController.UrlRequestController;
import com.altenheim.kalender.interfaces.models.ContactModel;
import com.altenheim.kalender.interfaces.models.SettingsModel;
import com.altenheim.kalender.interfaces.viewController.PopupViewController;
import com.altenheim.kalender.implementations.controller.models.SettingsModelImpl;

public class InitialSetupControllerImpl implements InitialSetupController
{
    private final IOController ioController;
    private final PopupViewController popup;
    private final UrlRequestController websiteScraper;
    private final SystemTrayNotificationsController systemNotifications;
    private final EntryFactory entryFactory;
    private final ContactModel contacts;

    public InitialSetupControllerImpl(SettingsModel settings, IOController ioController, PopupViewController popup,
                                      UrlRequestController websiteScraper, SystemTrayNotificationsController systemNotifications,
                                      EntryFactory entryFactory, ContactModel contacts)
    {
        this.ioController = ioController;
        this.popup = popup;
        this.websiteScraper = websiteScraper;
        this.systemNotifications = systemNotifications;
        this.entryFactory = entryFactory;
        this.contacts = contacts;
    }

    public void initializeSettings() 
    {
        ioController.createUserPath();
        try 
        {
            ioController.loadCalendarsFromFile(entryFactory);
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
                SettingsModelImpl.useAdvancedFeatures = false;
                return;
            }
        }
        SettingsModelImpl.decryptedPassword = ioController.loadHashedPassword();
        SettingsModelImpl.useAdvancedFeatures = true;

    }

    private boolean validateUserPassword() 
    {
        var password = popup.showPasswordInputDialog();
        var security = new DecryptionControllerImpl();
        var hashedPasswordAfterUserValidation = security.decrypt(password, "p:,-XQT3pj/^>)g_",
                SettingsModelImpl.PASSWORDHASH);
        while (hashedPasswordAfterUserValidation.isBlank()) 
        {
            if (popup.isRevalidationWanted()) 
            {
                password = popup.showPasswordInputDialog();
                hashedPasswordAfterUserValidation = security.decrypt(password, "p:,-XQT3pj/^>)g_",
                        SettingsModelImpl.PASSWORDHASH);
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
