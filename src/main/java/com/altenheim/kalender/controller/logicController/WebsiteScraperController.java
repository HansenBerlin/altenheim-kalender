package com.altenheim.kalender.controller.logicController;

import com.altenheim.kalender.interfaces.IImportController;
import com.altenheim.kalender.interfaces.IWebsiteScraperController;
import com.altenheim.kalender.models.SettingsModel;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.file.Paths;
import java.util.Timer;
import java.util.TimerTask;


public class WebsiteScraperController extends TimerTask implements IWebsiteScraperController
{
    private SettingsModel settings;
    private IImportController icsImport;

    public WebsiteScraperController(SettingsModel settings, IImportController icsImport)
    {
        this.settings = settings;
        this.icsImport = icsImport;
    }

    public void startScraperTask()
    {
        var timer = new Timer();
        timer.schedule(this, 0, settings.getScrapingInterval());
    }

    public void run()
    {
        downloadIcs();
        importHwrIcs();
    }

    public void importHwrIcs()  {
        var ics = Paths.get(settings.getPathToHwrScrapedFIle());
        var pathOfIcs = ics.toAbsolutePath().toString();
        icsImport.importFile(pathOfIcs);
    }

    public void downloadIcs()
    {
        try {
            var fos = new FileOutputStream(settings.getPathToHwrScrapedFIle());
            var url = new URL(settings.getUrl());
            var rbc = Channels.newChannel(url.openStream());
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}