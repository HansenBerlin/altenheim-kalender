package com.altenheim.kalender.controller.logicController;

import com.altenheim.kalender.interfaces.IWebsiteScraperController;
import com.altenheim.kalender.models.SettingsModel;
import net.fortuna.ical4j.data.ParserException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.Timer;
import java.util.TimerTask;

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
        try {
			importHwrIcs();
		} catch (IOException | ParseException | ParserException e) {
			e.printStackTrace();
		}
    }
	
	public void importHwrIcs() throws IOException, ParseException, ParserException {
		URL url = new URL(settings.geturlvariables());
		downloadIcs(url, "");
		//Übergabe String path an Import-Funktion
		Path ics = Paths.get(settings.getPath());
		var pathOfIcs = ics.toAbsolutePath().toString();
		var icsimport = new ImportExportTest();
		icsimport.importFile(pathOfIcs);
	}

	public void downloadIcs(URL url, String fileName) throws IOException 
	{
		ReadableByteChannel rbc = Channels.newChannel(url.openStream());
		FileOutputStream fos = new FileOutputStream(settings.getPath());
		fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
		fos.close();
    }
	
	

}
