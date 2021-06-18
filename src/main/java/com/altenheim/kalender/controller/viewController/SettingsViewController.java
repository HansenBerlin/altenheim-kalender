package com.altenheim.kalender.controller.viewController;

import com.altenheim.kalender.controller.logicController.WebsiteScraperController;
import com.altenheim.kalender.interfaces.*;
import com.altenheim.kalender.models.SettingsModel;
import com.calendarfx.model.CalendarSource;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import net.fortuna.ical4j.data.ParserException;

import java.io.IOException;
import java.text.ParseException;

public class SettingsViewController extends ResponsiveController
{

    @FXML
    private Button btnExport, btnImport, btnCrawl, btnGenerate;

    private SettingsModel settings;
    private IImportController importController;
    private IExportController exportController;
    private ICalendarEntriesModel allCalendars;
    private IWebsiteScraperController websiteScraper;
    private CalendarViewOverride customCalendarView;
    private IEntryFactory calendarFactory;

    public SettingsViewController(SettingsModel settings, IImportController importController, IEntryFactory calendarFactory,
                                  IExportController exportController, ICalendarEntriesModel allCalendars,
                                  IWebsiteScraperController websiteScraper, CalendarViewOverride customCalendarView)
    {
        this.settings = settings;
        this.importController = importController;
        this.exportController = exportController;
        this.allCalendars = allCalendars;
        this.customCalendarView = customCalendarView;
        this.websiteScraper = websiteScraper;
        this.calendarFactory = calendarFactory;
    }
    
    public void changeContentPosition() 
    {
        
    }

    @FXML
    void buttonClicked(ActionEvent event) throws IOException
    {
        var button = (Button)event.getSource();
        if(button.equals(btnExport))
            exportController.exportFile(allCalendars.getSpecificCalendarByIndex(0));
        else if (button.equals(btnImport))
        {
            var calendar = importController.importFile(settings.getPathToIcsExportedFile());
            allCalendars.addCalendar(calendar);
            var calendarSource = new CalendarSource("Saved Calendars");
            calendarSource.getCalendars().addAll(calendar);
            customCalendarView.getCalendarSources().addAll(calendarSource);
        }
        else if (button.equals(btnCrawl))
        {
            var calendar = importController.importFile(settings.getPathToHwrScrapedFIle());
            allCalendars.addCalendar(calendar);
            var calendarSource = new CalendarSource("Saved Calendars");
            calendarSource.getCalendars().addAll(calendar);
            customCalendarView.getCalendarSources().addAll(calendarSource);
        }
        else if (button.equals(btnGenerate))
        {
            calendarFactory.createRandomCalendarList();
            var calendar = allCalendars.getSpecificCalendarByIndex(0);
            var calendarSource = new CalendarSource("Saved Calendars");
            calendarSource.getCalendars().addAll(calendar);
            customCalendarView.getCalendarSources().addAll(calendarSource);
        }
    }
}




