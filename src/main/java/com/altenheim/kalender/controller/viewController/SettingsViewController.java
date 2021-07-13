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
import javafx.scene.layout.VBox;
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
    private IPopupViewController popupViewController;

<<<<<<< HEAD
    @FXML
    private MenuButton btnMenuSpecialField, btnMenuCourse, btnMenuSemester, btnMenuImportColour,
        btnMenuNotificationMin, btnMenuNotificationHour;
    @FXML
    private Button btnImport, btnExport, btnSave, btnCrawl, btnGenerate;
    @FXML
    private TextField txtTFStreet, txtTFCity, txtTFZipCode, txtTFHouseNumber, txtTFMail;
    @FXML
    private Text txtScrappingURL, txtAdressTitle, txtStreet, txtHouseNumber, txtCity, txtZipCode, txtMail, 
=======
    @FXML private MenuButton btnMenuSpecialField, btnMenuCourse, btnMenuSemester, btnMenuImportColour,
        btnMenuCheckEvent, btnMenuNotificationMin, btnMenuNotificationHour;
    @FXML private Button btnImport, btnExport, btnSave, btnCrawl, btnGenerate, btnCreateDummy;
    @FXML private TextField txtTFStreet, txtTFCity, txtTFZipCode, txtTFHouseNumber, txtTFMail;
    @FXML private Text txtAdressTitle, txtStreet, txtHouseNumber, txtCity, txtZipCode, txtMail, 
>>>>>>> parent of 7afeec6 (Revert "Merge pull request #214 from HansenBerlin/issue-27-terminvorschlage-automatisierte-erinnerungen")
        txtNotifocationMin, txtNotificationHour;
    @FXML private MenuItem menuItSpecialFieldInsurance, selectionSpecialFieldWi;
    @FXML private CheckBox cBToolTips = new CheckBox();
    @FXML private VBox topContainer, bottomContainer;

    public SettingsViewController(SettingsModel settings, IImportController importController, IEntryFactory calendarFactory,
                                  IExportController exportController, ICalendarEntriesModel allCalendars,
                                  IWebsiteScraperController websiteScraper, IPopupViewController popupViewController)
    {
        this.settings = settings;
        this.importController = importController;
        this.exportController = exportController;
        this.allCalendars = allCalendars;
        this.websiteScraper = websiteScraper;
        this.calendarFactory = calendarFactory;
        this.popupViewController = popupViewController;
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
    }  
    
    @FXML
    void buttonClicked(ActionEvent event) throws IOException, InterruptedException 
    {
        var button = (Button)event.getSource();
        
        if (button.equals(btnImport))
        {
            var stage = button.getScene().getWindow();
            popupViewController.importDialog(importController, calendarFactory, stage);
        }
        else if (button.equals(btnExport))
        {
            var stage = button.getScene().getWindow();
            popupViewController.exportDialog(exportController, allCalendars, stage);
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
        cBToolTips.setTooltip(cBToolTips.getTooltip());
<<<<<<< HEAD

    }
    public void changeContentPosition() {}
=======
    }    
>>>>>>> parent of 7afeec6 (Revert "Merge pull request #214 from HansenBerlin/issue-27-terminvorschlage-automatisierte-erinnerungen")

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

    
    
    
    
    public void changeContentPosition(double width, double height) 
    {
        int row = 1;
        int col = 0;
        if (height < 950)
        {            
            row = 0;
            col = 1;
        }       
        childContainer.getChildren().remove(bottomContainer);
        childContainer.add(bottomContainer, col, row, 1, 1);
    }
}