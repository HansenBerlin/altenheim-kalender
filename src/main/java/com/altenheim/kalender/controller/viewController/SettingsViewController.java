package com.altenheim.kalender.controller.viewController;

import com.altenheim.kalender.interfaces.*;
import com.altenheim.kalender.models.SettingsModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import java.io.IOException;

public class SettingsViewController extends ResponsiveController
{
    @FXML private Button btnExport, btnImport, btnCrawl, btnGenerate;
    private SettingsModel settings;
    private IImportController importController;
    private IExportController exportController;
    private ICalendarEntriesModel allCalendars;
    private IWebsiteScraperController websiteScraper;
    private IEntryFactory calendarFactory;


    public SettingsViewController(SettingsModel settings, IImportController importController, IEntryFactory calendarFactory,
                                  IExportController exportController, ICalendarEntriesModel allCalendars,
                                  IWebsiteScraperController websiteScraper )
    {
        this.settings = settings;
        this.importController = importController;
        this.exportController = exportController;
        this.allCalendars = allCalendars;
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
            exportController.exportCalendarAsFile(allCalendars.getSpecificCalendarByIndex(0),
                    settings.getPathToIcsExportedFile());
        else if (button.equals(btnImport))
        {
            var calendar = importController.importFile(settings.getPathToIcsExportedFile());
            calendarFactory.addCalendarToView(calendar);
        }
        else if (button.equals(btnCrawl))
        {
            var calendar = importController.importFile(settings.getPathToHwrScrapedFIle());
            calendarFactory.addCalendarToView(calendar);
        }
        else if (button.equals(btnGenerate))
        {
            calendarFactory.createRandomCalendarList();
        }
    }
}




