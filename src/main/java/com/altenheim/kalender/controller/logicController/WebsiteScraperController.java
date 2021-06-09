package com.altenheim.kalender.controller.logicController;

import java.util.Timer;
import java.util.TimerTask;
import com.altenheim.kalender.interfaces.IWebsiteScraperController;
import com.altenheim.kalender.models.SettingsModel;

public class WebsiteScraperController extends TimerTask implements IWebsiteScraperController
{
    private SettingsModel settings;

    public WebsiteScraperController(SettingsModel settings)
    {
        this.settings = settings;        
    }

    public void startScraperTask()
    {
        var timer = new Timer();  
        timer.schedule(this, 0, settings.getScrapingInterval());
    }

    public void run() 
    {
        System.out.println("Scraping...");        
    }
}    