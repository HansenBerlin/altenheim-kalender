package com.altenheim.kalender.implementations.controller.logicController;

import com.altenheim.kalender.interfaces.logicController.ImportController;
import com.altenheim.kalender.interfaces.logicController.UrlRequestController;
import com.altenheim.kalender.interfaces.models.SettingsModel;
import com.altenheim.kalender.implementations.controller.models.SettingsModelImpl;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class UrlRequestControllerImpl extends TimerTask implements UrlRequestController
{
    private final SettingsModel settings;
    private final ImportController importController;

    public UrlRequestControllerImpl(SettingsModel settings, ImportController importController)
    {
        this.settings = settings;
        this.importController = importController;
    }

    public void startScraperTask()
    {
        var timer = new Timer();
        timer.schedule(this, 0, settings.getHwrRequestIntervalInMinutes()*60*1000);
    }

    public void run() 
    {
        if(!isCalendarImportedSuccesfully())
            System.out.println("ERROR: Kalender konnte nicht heruntergeladen werden.");
    }

    public boolean isCalendarImportedSuccesfully()
    {
        clearHwrCalendarFiles();
        boolean isCalendarDownloaded = false;
        try
        {
            isCalendarDownloaded = isDownloadIcsSuccessful();
        }
        catch (IOException e)
        {
            System.err.println(e.getMessage());
        }
        if (isCalendarDownloaded)
        {
            var calendarFilesPath = SettingsModelImpl.userDirectory + "hwr-calendars/HWR-Kalender.ics";
            if (!importController.canCalendarFileBeImported(calendarFilesPath))
                return false;
            if (!importController.canCalendarFileBeParsed())
                return false;
            importController.importCalendar("HWR-Kalender-" + settings.getSelectedHwrCourseName());
            return true;
        }
        return false;
    }    

    private boolean isDownloadIcsSuccessful() throws IOException {
        FileOutputStream fos = null;
        ReadableByteChannel rbc = null;
        try 
        {
            fos = new FileOutputStream(SettingsModelImpl.userDirectory + "hwr-calendars/HWR-Kalender.ics");
            var url = new URL(SettingsModelImpl.hwrWebsiteUrl);
            rbc = Channels.newChannel(url.openStream());
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            return true;
        } 
        catch (IOException e) 
        {
            System.err.println(e.getMessage());
            return false;
        }
        finally
        {
            Objects.requireNonNull(fos).close();
            if (rbc != null)
                rbc.close();
        }
    }

    private void clearHwrCalendarFiles()
    {
        var directory = new File(SettingsModelImpl.userDirectory + "hwr-calendars");
        for (var file: Objects.requireNonNull(directory.listFiles()))
            file.delete();
    }
}