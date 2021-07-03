package com.altenheim.kalender;

import com.altenheim.kalender.controller.logicController.SystemNotificationsController;


public class SystemNotificationsControllerTest {

    public static void main(String[] args) {
        
        var test = new SystemNotificationsController();
        test.initializeSystemTrayAccess();
        test.outputSystemMessage();


    }
}
