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
    private IGoogleAPIController googleApis;


    public SettingsViewController(SettingsModel settings, IImportController importController, IEntryFactory calendarFactory,
                                  IExportController exportController, ICalendarEntriesModel allCalendars,
                                  IWebsiteScraperController websiteScraper, IGoogleAPIController googleApis)
    {
        this.settings = settings;
        this.importController = importController;
        this.exportController = exportController;
        this.allCalendars = allCalendars;
        this.websiteScraper = websiteScraper;
        this.calendarFactory = calendarFactory;
        this.googleApis = googleApis;
    }
    
    public void changeContentPosition() 
    {
        
    }

    @FXML
    void buttonClicked(ActionEvent event) throws IOException, InterruptedException {
        var button = (Button)event.getSource();
        if(button.equals(btnExport))
        {
            var returnValue = googleApis.getOpeningHours("Casablanca, 10247 Berlin, Rigaer Straße");
            var reise = googleApis.searchForDestinationDistance("Ring Center, Potsdam, Germany", "Berlin Hauptbahnhof");
            for (var entry : reise)
            {
                System.out.println(entry);
            }
            System.out.println(returnValue);
        }
        else if (button.equals(btnImport))
        {
            var calendar = importController.importFile(settings.getPathToIcsExportedFile());
            calendarFactory.addCalendarToView(calendar);
        }
        else if (button.equals(btnCrawl))
        {
            var calendar = importController.importFile(settings.getPathToHwrScrapedFile());
            calendarFactory.addCalendarToView(calendar);
        }
        else if (button.equals(btnGenerate))
        {
            calendarFactory.createRandomCalendarList();
        }
    }
}




