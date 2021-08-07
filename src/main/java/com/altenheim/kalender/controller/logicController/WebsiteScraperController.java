package com.altenheim.kalender.controller.logicController;

import com.altenheim.kalender.controller.viewController.PopupViewsController;
import com.altenheim.kalender.interfaces.*;
import com.altenheim.kalender.models.SettingsModel;
import java.io.FileOutputStream;
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
        timer.schedule(this, 0, settings.hwrRequestIntervalInMinutes*60*1000);
    }

    public void run() 
    {
        scrapeCalendar();
    }

    public void scrapeCalendar() 
    {
        if (isDownloadIcsSuccessful())
        {
            var calendarFile = settings.getPathToUserDirectory() + "calendars/hwrFile.ics";
            var calendar = importController.importFile(calendarFile);       
            entryFactory.addCalendarToView(calendar, "HWR Calendar");
        }
    }    

    private boolean isDownloadIcsSuccessful() 
    {
        try 
        {
            var fos = new FileOutputStream(settings.getPathToUserDirectory() + "calendars/hwrFile.ics");
            var url = new URL(settings.hwrWebsiteUrl);
            var rbc = Channels.newChannel(url.openStream());
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            fos.close();
            rbc.close();
            return true;
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
            return false;
        }
    }    
}