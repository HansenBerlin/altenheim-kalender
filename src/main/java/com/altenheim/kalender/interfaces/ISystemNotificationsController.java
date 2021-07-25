package com.altenheim.kalender.interfaces;

public interface ISystemNotificationsController {
    void startNotificationTask();

    boolean initializeSystemTrayAccess();
}
