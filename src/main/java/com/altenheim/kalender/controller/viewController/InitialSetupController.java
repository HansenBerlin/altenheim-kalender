package com.altenheim.kalender.controller.viewController;

import com.altenheim.kalender.controller.logicController.SecureAesController;
import com.altenheim.kalender.interfaces.IIOController;
import com.altenheim.kalender.models.SettingsModel;


public class InitialSetupController
{
    private SettingsModel settings;
    private IIOController ioController;
    private PopupViewsController popup;

    public InitialSetupController(SettingsModel settings, IIOController ioController, PopupViewsController popup)
    {
        this.settings = settings;
        this.ioController = ioController;
        this.popup = popup;
    }

    private String decryptPassword(String password)
    {
        try
        {
            var security = new SecureAesController();
            var hashedPasswordAfterUserValidation = security.decrypt(password, "p:,-XQT3pj/^>)g_", SettingsModel.PASSWORDHASH);
            return hashedPasswordAfterUserValidation;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return "";
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
        var hashedPasswordAfterUserValidation = security.decrypt(password, "p:,-XQT3pj/^>)g_", SettingsModel.PASSWORDHASH);
        while(hashedPasswordAfterUserValidation.isBlank())
        {
            if (popup.isRevalidationWanted())
            {
                password = popup.showPasswordInputDialog();
                hashedPasswordAfterUserValidation = security.decrypt(password, "p:,-XQT3pj/^>)g_", SettingsModel.PASSWORDHASH);
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
