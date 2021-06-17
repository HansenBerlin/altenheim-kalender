package com.altenheim.kalender;

import com.altenheim.kalender.controller.logicController.SecureAesController;
import com.altenheim.kalender.models.SettingsModel;

import java.io.IOException;
import java.net.URISyntaxException;

public class Main  
{
    public static void main(String[] args) throws IOException, InterruptedException, URISyntaxException 
    {       
        //JavaFXLauncher.main(args);

        var security = new SecureAesController();
        String userPw = "v;Z3.Z$2M((e,}euJ";
        var hashedPasswordAfterUserValidation = security.decrypt(userPw, "112233%$§", SettingsModel.PASSWORDHASH);
        var apiKeyDecrypted = security.decrypt(hashedPasswordAfterUserValidation, "ff&%hf((", SettingsModel.APICYPHER);
        System.out.println(apiKeyDecrypted);
    }
}
