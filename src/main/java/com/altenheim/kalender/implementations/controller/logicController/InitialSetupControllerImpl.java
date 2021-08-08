package com.altenheim.kalender.implementations.controller.logicController;

import com.altenheim.kalender.interfaces.factorys.EntryFactory;
import com.altenheim.kalender.interfaces.factorys.InitialSetupController;
import com.altenheim.kalender.interfaces.logicController.IOController;
import com.altenheim.kalender.interfaces.logicController.SystemTrayNotificationsController;
import com.altenheim.kalender.interfaces.logicController.UrlRequestController;
import com.altenheim.kalender.interfaces.models.ContactModel;
import com.altenheim.kalender.interfaces.viewController.PopupViewController;
import com.altenheim.kalender.implementations.controller.models.SettingsModelImpl;
import com.calendarfx.model.Calendar;
import java.io.File;

public record InitialSetupControllerImpl(IOController ioController,
                                         PopupViewController popup,
                                         UrlRequestController websiteScraper,
                                         SystemTrayNotificationsController systemNotifications,
                                         EntryFactory entryFactory,
                                         ContactModel contacts) implements InitialSetupController {

    public void initializeSettings()
    {
        ioController.createUserPath();
        ioController.loadCalendarsFromFile(entryFactory);
        ioController.loadContactsFromFile(contacts);
        websiteScraper.startScraperTask();
        if (systemNotifications.isSystemTrayAccessAllowed())
            systemNotifications.startNotificationTask();
        createDefaultCalendarWhenDirectoryIsEmpty();
    }

    private void createDefaultCalendarWhenDirectoryIsEmpty()
    {
        var calendarDirectory = new File(SettingsModelImpl.userDirectory + "calendars/");
        if (calendarDirectory.listFiles().length == 0)
            entryFactory.addCalendarToView(new Calendar(), "Standardkalender");
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
                popup.showPasswordWrongDialog();
                return false;
            }
        }
        ioController.saveHashedPassword(hashedPasswordAfterUserValidation);
        popup.showPasswordCorrectConfirmationDialog();
        return true;
    }

}
