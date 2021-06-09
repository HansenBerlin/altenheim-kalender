package com.altenheim.kalender.controller.logicController;

import com.altenheim.kalender.interfaces.ICalendarEntriesModel;
import com.altenheim.kalender.interfaces.IImportController;
import com.altenheim.kalender.interfaces.IWebsiteScraperController;

public class ImportController extends IOController implements IImportController
{
    private ICalendarEntriesModel allEntries;
    private IWebsiteScraperController scraper;

    public ImportController(ICalendarEntriesModel allEntries, IWebsiteScraperController scraper)
    {
        this.allEntries = allEntries;
        this.scraper = scraper;
    }
    
}
