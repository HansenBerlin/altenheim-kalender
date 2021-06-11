package com.altenheim.kalender.controller.logicController;

import java.util.List;
import com.altenheim.kalender.interfaces.*;
import com.altenheim.kalender.controller.viewController.CalendarViewOverride;
import com.altenheim.kalender.models.ContactModel;
import com.altenheim.kalender.models.SettingsModel;

public class ImportController extends IOController implements IImportController
{
    private ICalendarEntriesModel allEntries;
    private IWebsiteScraperController scraper;

    public ImportController(ICalendarEntriesModel allEntries, IWebsiteScraperController scraper, IAppointmentEntryFactory administrateEntries, 
        CalendarViewOverride calendarView, List<ContactModel> allContacts, SettingsModel settings)
    {
        super(administrateEntries, calendarView, allContacts, settings);
        this.allEntries = allEntries;
        this.scraper = scraper;
    }
    
}
