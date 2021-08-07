package com.altenheim.kalender.implementations.controller.logicController;

import com.altenheim.kalender.interfaces.factorys.EntryFactory;
import com.altenheim.kalender.interfaces.logicController.ImportController;
import com.altenheim.kalender.interfaces.logicController.UrlRequestController;
import com.altenheim.kalender.interfaces.models.SettingsModel;
import com.altenheim.kalender.implementations.controller.models.SettingsModelImpl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.util.Timer;
import java.util.TimerTask;

public class UrlRequestControllerImpl extends TimerTask implements UrlRequestController
{
    private final SettingsModel settings;
    private final ImportController importController;
    private final EntryFactory entryFactory;

    public UrlRequestControllerImpl(SettingsModel settings, ImportController importController, EntryFactory entryFactory)
    {
        this.settings = settings;
        this.importController = importController;
        this.entryFactory = entryFactory;
        startScraperTask();
    }

    public void startScraperTask()
    {
        var timer = new Timer();
        timer.schedule(this, 0, settings.getHwrRequestIntervalInMinutes()*60*1000);
    }

    public void run() 
    {
        if(!isCalendarImportedSuccesfully());
            System.err.println("ERROR: Kalender konnte nicht heruntergeladen werden.");
    }

    public boolean isCalendarImportedSuccesfully()
    {
        clearHwrCalendarFiles();
        if (isDownloadIcsSuccessful())
        {
            var calendarFilesPath = settings.getPathToUserDirectory() + "hwr-calendars/HWR-Kalender.ics";
            if (!importController.canCalendarFileBeImported(calendarFilesPath))
                return false;
            if (!importController.canCalendarFileBeParsed())
                return false;
            importController.importCalendar("HWR-Kalender");
            return true;
        }
        return false;
    }    

    private boolean isDownloadIcsSuccessful() 
    {
        try 
        {
            var fos = new FileOutputStream(settings.getPathToUserDirectory() + "hwr-calendars/HWR-Kalender.ics");
            var url = new URL(SettingsModelImpl.hwrWebsiteUrl);
            var rbc = Channels.newChannel(url.openStream());
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            fos.close();
            rbc.close();
            return true;
        } 
        catch (IOException e) 
        {
            System.err.println(e.getMessage());
            return false;
        }
    }

    private void clearHwrCalendarFiles()
    {
        var directory = new File(settings.getPathToUserDirectory() + "hwr-calendars");
        for (var file: directory.listFiles())
            file.delete();
    }
}