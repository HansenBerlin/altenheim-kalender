package com.altenheim.kalender.controller.viewController;

import com.altenheim.kalender.interfaces.*;
import com.altenheim.kalender.models.SettingsModel;
import com.altenheim.kalender.resourceClasses.ComboBoxCreate;

import javafx.beans.property.SimpleLongProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import java.io.IOException;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
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
    private IComboBoxFactory comboBoxFactory;
    private IPopupViewController popupViewController;
    private ComboBox<String> comboBoxNotificationMin, comboBoxSelectionSpecialField, comboBoxSelectionCourse, comboBoxSelectionSemester;    

    @FXML
    private Button btnImport, btnExport, btnSave, btnCrawl, btnGenerate;
    @FXML
    private TextField txtTFStreet, txtTFCity, txtTFZipCode, txtTFHouseNumber, txtTFMail;
    @FXML
    private Text txtScrappingURL, txtAdressTitle, txtStreet, txtHouseNumber, txtCity, txtZipCode, txtMail, 
        txtNotifocationMin, txtNotificationHour, txtError;
    @FXML 
    private MenuItem menuItSpecialFieldInsurance, selectionSpecialFieldWi;
    @FXML 
    private CheckBox cBToolTips = new CheckBox();
    @FXML 
    private VBox topContainer, bottomContainer, containerComboBoxNotificationMin;
    @FXML
    private HBox containerComboBoxSelectorScrapping;


    public SettingsViewController(SettingsModel settings, IImportController importController, IEntryFactory calendarFactory,
                                  IExportController exportController, ICalendarEntriesModel allCalendars,
                                  IWebsiteScraperController websiteScraper, IPopupViewController popupViewController, IComboBoxFactory comboBoxFactory)
    {
        this.settings = settings;
        this.importController = importController;
        this.exportController = exportController;
        this.allCalendars = allCalendars;
        this.websiteScraper = websiteScraper;
        this.calendarFactory = calendarFactory;
        this.popupViewController = popupViewController;
        this.comboBoxFactory = comboBoxFactory;

    }

    @FXML
    private void initialize ()
    {
        txtStreet.textProperty().bind(settings.getStreet());
        txtHouseNumber.textProperty().bind(settings.getHouseNumber());
        txtZipCode.textProperty().bind(settings.getZipCOde());
        txtCity.textProperty().bind(settings.getCity());
        txtMail.textProperty().bind(settings.getMail());
        cBToolTips.selectedProperty().bindBidirectional(settings.getToolTip());
        createComboBoxes();
    }

    private void createComboBoxes()
    {
        comboBoxNotificationMin = comboBoxFactory.create(ComboBoxCreate.MENUNOTIFICATIONMIN);
        comboBoxSelectionSpecialField = comboBoxFactory.create(ComboBoxCreate.SELECTIONSPECIALFIELD);
        comboBoxSelectionCourse = comboBoxFactory.create(ComboBoxCreate.SELECTIONCOURSE);
        comboBoxSelectionSemester = comboBoxFactory.create(ComboBoxCreate.SELECTIONSEMESTER);
 
        containerComboBoxNotificationMin.getChildren().add(comboBoxNotificationMin);
        containerComboBoxSelectorScrapping.getChildren().add(comboBoxSelectionSpecialField);
        containerComboBoxSelectorScrapping.getChildren().add(comboBoxSelectionCourse);
        containerComboBoxSelectorScrapping.getChildren().add(comboBoxSelectionSemester);

        comboBoxNotificationMin.promptTextProperty().bind(settings.getnotificationTimeBeforeEntryInMinutes2());

        

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
        if (comboBoxSelectionSpecialField.getValue() == null || comboBoxSelectionCourse.getValue() == null || comboBoxSelectionSemester.getValue() == null) {
            txtError.setText("Nicht alle HWR Komponenten ausgewählt!");
            txtError.setVisible(true);
            txtError.setFill(Color.RED);
        } else {
            txtError.setVisible(false);
            String resultURL = String.format("https://moodle.hwr-berlin.de/fb2-stundenplan/download.php?doctype=.ics&url=./fb2-stundenplaene/%s/semester%s/kurs%s", 
            comboBoxSelectionSpecialField.getValue(), comboBoxSelectionSemester.getValue(), comboBoxSelectionCourse.getValue().replaceFirst("keine Kurse", ""));
        settings.setCalendarParser(resultURL);    
        }
        cBToolTips.setTooltip(cBToolTips.getTooltip());


    }
    public void changeContentPosition() {} 
    
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
