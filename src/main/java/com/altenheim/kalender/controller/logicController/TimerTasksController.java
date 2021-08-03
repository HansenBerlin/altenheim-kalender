package com.altenheim.kalender.controller.logicController;

import java.time.LocalTime;
import java.util.Timer;
import java.util.TimerTask;

import com.altenheim.kalender.controller.viewController.CustomViewOverride;
import com.altenheim.kalender.interfaces.ISystemNotificationsController;
import com.altenheim.kalender.interfaces.IWebsiteScraperController;
import com.altenheim.kalender.models.SettingsModel;

public class TimerTasksController extends TimerTask {
    private CustomViewOverride customViewOverride;
    private SettingsModel settings;
    private ISystemNotificationsController systemNotifications;
    private IWebsiteScraperController websiteScraper;
    private boolean possibleSystemNotifications = false;

    public void setpossibleSystemNotifications(boolean possibleSystemNotifications) {this.possibleSystemNotifications = possibleSystemNotifications; }

    private long seconds = 0;

    public TimerTasksController(CustomViewOverride customViewOverride, SettingsModel settings, ISystemNotificationsController systemNotifications, IWebsiteScraperController websiteScraper) {
        this.customViewOverride = customViewOverride;
        this.settings = settings;
        this.systemNotifications = systemNotifications;
        this.websiteScraper = websiteScraper;
    }

    public void run() {
        seconds+=10;
        if (seconds%10==0) 
            customViewOverride.setTime(LocalTime.now());
        if (seconds%(settings.entrySystemMessageIntervalInMinutes*60)==0 && possibleSystemNotifications)
            systemNotifications.prepareSystemMessagesForEntrys();
        if (seconds%(settings.getScrapingInterval()*60)==0)
            websiteScraper.scrapeCalendar();
      
    }

    public void startNotificationTasks() {
        var timer = new Timer();
        timer.schedule(this, 0, 10000);
    }
}
