package com.altenheim.kalender.interfaces;

public interface ISystemNotificationsController {
    void startScraperTask();
    boolean initializeSystemTrayAccess();
    void outputSystemMessage(String title, String message);
}
