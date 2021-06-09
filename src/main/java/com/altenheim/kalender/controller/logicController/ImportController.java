package com.altenheim.kalender.controller.logicController;

import com.altenheim.kalender.interfaces.ICalendarEntriesModel;
import com.altenheim.kalender.interfaces.IImportController;

public class ImportController extends IOController implements IImportController
{
    private ICalendarEntriesModel allEntries;
    private WebsiteScraperController scraper;

    public ImportController(ICalendarEntriesModel allEntries, WebsiteScraperController scraper)
    {
        this.allEntries = allEntries;
        this.scraper = scraper;
    }
    
}
