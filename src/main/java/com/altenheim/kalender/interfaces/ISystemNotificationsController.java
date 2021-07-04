package com.altenheim.kalender.interfaces;

public interface ISystemNotificationsController 
{
    void startNotificationTask();
    boolean initializeSystemTrayAccess();
    void outputSystemMessage(String title, String message);
}
