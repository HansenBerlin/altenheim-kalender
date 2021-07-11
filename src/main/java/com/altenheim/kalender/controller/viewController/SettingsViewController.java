package com.altenheim.kalender.controller.viewController;

import com.altenheim.kalender.interfaces.*;
import com.altenheim.kalender.models.SettingsModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import java.io.IOException;
import javafx.scene.control.CheckBox;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class SettingsViewController extends ResponsiveController
{
    private SettingsModel settings;
    private IImportController importController;
    private IExportController exportController;
    private ICalendarEntriesModel allCalendars;
    private IWebsiteScraperController websiteScraper;
    private IEntryFactory calendarFactory;
    private IGoogleAPIController googleApis;

    @FXML
    private MenuButton btnMenuSpecialField, btnMenuCourse, btnMenuSemester, btnMenuImportColour;
    @FXML
    private Button btnImport, btnExport, btnSave, btnCrawl, btnGenerate;
    @FXML
    private TextField txtTFStreet, txtTFCity, txtTFZipCode, txtTFHouseNumber, txtTFMail;
    @FXML
    private Text txtScrappingURL, txtAdressTitle, txtStreet, txtHouseNumber, txtCity, txtZipCode, txtMail;
    @FXML
    private MenuItem menuItSpecialFieldInsurance, selectionSpecialFieldWi;
    @FXML
    private CheckBox cBToolTips = new CheckBox ();

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
    @FXML
    void saveSettings(ActionEvent event) 
    {
        settings.setStreet(txtStreet.getText());
        settings.setHouseNumber(txtHouseNumber.getText());
        settings.setZipCode(txtZipCode.getText());
        settings.setCity(txtCity.getText());
        settings.setMail(txtMail.getText());
        String resultURL = String.format("https://moodle.hwr-berlin.de/fb2-stundenplan/download.php?doctype=.ics&url=./fb2-stundenplaene/%s/semester%c/kurs%s", 
            btnMenuSpecialField.getText(), btnMenuSemester.getText().charAt(5), btnMenuCourse.getText().replaceFirst("keine Kurse", ""));
        settings.setCalendarParser(resultURL);
        settings.setSpecialField(btnMenuSpecialField.getText());
        settings.setCourse(btnMenuCourse.getText());
        settings.setSemester(btnMenuSemester.getText());
        //kann später entfernt werden
        txtScrappingURL.setText(settings.getCalendarParser());
        cBToolTips.setTooltip(cBToolTips.getTooltip());
    }
    public void changeContentPosition() {}

    @FXML 
    void selectionScrapper(ActionEvent event)
    {
        var item = (MenuItem)event.getSource();
     if (item.getId().contains("selection_AuswahlFB_")) 
     {
        btnMenuSpecialField.setText(item.getText());
     } 
     else if (item.getId().contains("selection_AuswahlKurs_"))
     {
        btnMenuCourse.setText(item.getText());
     } 
     else if (item.getId().contains("selection_AuswahlSemester_"))
     {
        btnMenuSemester.setText(item.getText());
     }
    }
    
    @FXML
    private void initialize ()
    {
        txtStreet.textProperty().bind(settings.getStreet());
        txtHouseNumber.textProperty().bind(settings.getHouseNumber());
        txtZipCode.textProperty().bind(settings.getZipCOde());
        txtCity.textProperty().bind(settings.getCity());
        txtMail.textProperty().bind(settings.getMail());
        btnMenuSpecialField.idProperty().bind(settings.getSpecialField());
        btnMenuCourse.idProperty().bind(settings.getCourse());
        btnMenuSemester.idProperty().bind(settings.getSemester());
        cBToolTips.selectedProperty().bindBidirectional(settings.getToolTip());
        //entfernen sobald es funktioniert
        txtScrappingURL.setText(settings.getUrl());
    }
    
}


