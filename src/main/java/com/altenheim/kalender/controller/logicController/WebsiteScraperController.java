package com.altenheim.kalender.controller.logicController;

import com.altenheim.kalender.interfaces.IEntryFactory;
import com.altenheim.kalender.interfaces.IImportController;
import com.altenheim.kalender.interfaces.IWebsiteScraperController;
import com.altenheim.kalender.models.SettingsModel;
import com.calendarfx.model.Calendar;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.file.Paths;
import java.util.Timer;
import java.util.TimerTask;

public class WebsiteScraperController extends TimerTask implements IWebsiteScraperController {
    private SettingsModel settings;
    private IImportController icsImport;
    private IEntryFactory entryFactory;

    public WebsiteScraperController(SettingsModel settings, IImportController icsImport, IEntryFactory entryFactory) {
        this.settings = settings;
        this.icsImport = icsImport;
        this.entryFactory = entryFactory;
    }

    public void startScraperTask() {
        var timer = new Timer();
        timer.schedule(this, 0, settings.getScrapingInterval() * 60000);
    }

    public void run() {
        scrapeCalendar();
    }

    public void scrapeCalendar() 
    {
        /*

        if (isDownloadIcsSuccessful())
        {
            var calendarFile = settings.getPathToUserDirectory() + "calendars/hwrFile.ics";
            var calendar = icsImport.importFile(calendarFile);
            //var calendarHwr = importedHwrIcsFile();
            entryFactory.addCalendarToView(calendar, "HWR Calendar");
        }*/
    }    

    private boolean isDownloadIcsSuccessful() {
        try {
            var fos = new FileOutputStream(settings.getPathToUserDirectory() + "calendars/hwrFile.ics");
            var url = new URL(settings.getUrl());
            var rbc = Channels.newChannel(url.openStream());
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            fos.close();
            rbc.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }    
}