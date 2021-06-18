package com.altenheim.kalender.controller.viewController;

import com.altenheim.kalender.controller.logicController.SecureAesController;
import com.altenheim.kalender.interfaces.IIOController;
import com.altenheim.kalender.models.SettingsModel;
import javafx.scene.control.TextInputDialog;

public class InitialSetupController
{
    private SettingsModel settings;
    private IIOController ioController;

    public InitialSetupController(SettingsModel settings, IIOController ioController)
    {
        this.settings = settings;
        this.ioController = ioController;
    }

    public void serializeDecryptedAPIKeyHash()
    {
        var password = showDialog();
        try
        {
            var security = new SecureAesController();
            var hashedPasswordAfterUserValidation = security.decrypt(password, "p:,-XQT3pj/^>)g_", SettingsModel.PASSWORDHASH);
            ioController.saveDecryptedPasswordHash(hashedPasswordAfterUserValidation);
            System.out.println("Erfolgreich entschlüsselt und gespeichert");
        }
        catch(Exception e)
        {
            e.printStackTrace();
            System.out.println("Entschlüsselung fehlgeschlagen");
        }
            //var apiKeyDecrypted = security.decrypt(hashedPasswordAfterUserValidation, "e]<J3Grct{~'HJv-", SettingsModel.APICYPHERTEXT);
        //System.out.println(apiKeyDecrypted);
    }

    private String showDialog()
    {
        var dialog = new TextInputDialog();
        dialog.setTitle("Entschlüsselung");
        dialog.setHeaderText("Einmalige Passworteingabe für erweiterte Funktionen zu nutzen.");
        dialog.setContentText("Passwort eingeben: ");
        var result = dialog.showAndWait();
        if (result.isPresent())
            return result.get();
        else
            return "";
    }
}
