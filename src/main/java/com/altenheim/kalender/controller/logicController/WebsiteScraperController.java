package com.altenheim.kalender.controller.logicController;

import com.altenheim.kalender.interfaces.*;
import com.altenheim.kalender.models.SettingsModelImpl;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.util.Timer;
import java.util.TimerTask;

public class WebsiteScraperController extends TimerTask implements IWebsiteScraperController 
{
    private SettingsModel settings;
    private IImportController importController;
    private IEntryFactory entryFactory;

    public WebsiteScraperController(SettingsModel settings, IImportController importController, IEntryFactory entryFactory)
    {
        this.settings = settings;
        this.importController = importController;
        this.entryFactory = entryFactory;
    }

    public void startScraperTask() 
    {
        var timer = new Timer();
        timer.schedule(this, 0, settings.getHwrRequestIntervalInMinutes()*60*1000);
    }

    public void run() 
    {
        scrapeCalendar();
    }

    public void scrapeCalendar() 
    {/*
        if (isDownloadIcsSuccessful())
        {
            var calendarFile = settings.getPathToUserDirectory() + "calendars/hwrFile.ics";
            var calendar = importController.importFile(calendarFile);            
            entryFactory.addCalendarToView(calendar, "HWR Calendar");
        }*/
    }    

    private boolean isDownloadIcsSuccessful() 
    {
        try 
        {
            var fos = new FileOutputStream(settings.getPathToUserDirectory() + "calendars/hwrFile.ics");
            var url = new URL(SettingsModelImpl.hwrWebsiteUrl);
            var rbc = Channels.newChannel(url.openStream());
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            fos.close();
            rbc.close();
            return true;
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
            return false;
        }
    }    
}