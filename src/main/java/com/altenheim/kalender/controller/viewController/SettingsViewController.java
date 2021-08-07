package com.altenheim.kalender.controller.viewController;

import com.altenheim.kalender.interfaces.*;
import com.altenheim.kalender.models.SettingsModel;
import com.altenheim.kalender.resourceClasses.ComboBoxCreate;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import java.io.IOException;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
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
    private IEntryFactory calendarFactory;
    private IPopupViewController popupViewController;
    private IComboBoxFactory comboBoxFactory;
    private ComboBox<String> comboBoxNotificationMin, comboBoxSelectionSpecialField, comboBoxSelectionCourse,
            comboBoxSelectionSemester, comboBoxDefaultCalendar;

    @FXML private Button btnImport, btnExport, btnSave, btnCrawl, btnGenerate;
    @FXML private TextField txtTFStreet, txtTFCity, txtTFZipCode, txtTFHouseNumber, txtTFMail;
    @FXML private Text txtScrappingURL, txtAdressTitle, txtStreet, txtHouseNumber, txtCity, txtZipCode, txtMail,
            txtNotifocationMin, txtNotificationHour, txtError;
    @FXML private HBox containerComboBoxSelectorScrapping;
    @FXML private MenuItem menuItSpecialFieldInsurance, selectionSpecialFieldWi;
    @FXML private CheckBox cBToolTips;
    @FXML private VBox topContainer, bottomContainer, containerComboBoxNotificationMin, containerComboBoxDefaultCalendar;

    public SettingsViewController(SettingsModel settings, IImportController importController,
            IEntryFactory calendarFactory, IExportController exportController, ICalendarEntriesModel allCalendars,
            IComboBoxFactory comboBoxFactory, IPopupViewController popupViewController) 
    {
        this.settings = settings;
        this.importController = importController;
        this.exportController = exportController;
        this.allCalendars = allCalendars;
        this.calendarFactory = calendarFactory;
        this.popupViewController = popupViewController;
        this.comboBoxFactory = comboBoxFactory;
    }

    @FXML
    private void initialize() 
    {
        createComboBoxes();
        bindInputFieldsToSerializable();
    }

    private void bindInputFieldsToSerializable() 
    {
        Text[] stringPropertiesCollectionText = { txtStreet, txtHouseNumber, txtZipCode, txtCity, txtMail };
        TextField[] stringPropertiesCollectionTextField = { txtTFStreet, txtTFHouseNumber, txtTFZipCode, txtTFCity, txtTFMail };

        for (int i = 0; i < stringPropertiesCollectionTextField.length; i++) 
        {
            stringPropertiesCollectionTextField[i].textProperty()
                    .bindBidirectional(settings.getSettingsInputFieldsContainer()[i]);
            stringPropertiesCollectionText[i].textProperty()
                    .bindBidirectional(settings.getSettingsInputFieldsContainer()[i]);
        }
        cBToolTips.selectedProperty().bindBidirectional(settings.getToolTipEnabled());
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
        
        comboBoxNotificationMin.getSelectionModel().select(String.valueOf(settings.notificationTimeBeforeEntryInMinutes));
        comboBoxSelectionSpecialField.getSelectionModel().select(settings.specialField.getValue());
        comboBoxSelectionSemester.getSelectionModel().select(settings.semester.getValue());
        comboBoxSelectionCourse.getSelectionModel().select(settings.course.getValue());

        comboBoxDefaultCalendar = comboBoxFactory.create(ComboBoxCreate.CALENDERNAMES);
        containerComboBoxDefaultCalendar.getChildren().add(comboBoxDefaultCalendar);
        comboBoxDefaultCalendar.getSelectionModel().select(settings.defaultCalendarForSearchView);
    }

    @FXML
    void buttonClicked(ActionEvent event) throws IOException, InterruptedException 
    {
        var button = (Button) event.getSource();

        if (button.equals(btnImport)) {
            var stage = button.getScene().getWindow();
            popupViewController.importDialog(importController, calendarFactory, stage);
        } else if (button.equals(btnExport)) {
            var stage = button.getScene().getWindow();
            popupViewController.exportDialog(exportController, allCalendars, stage);
        } else if (button.equals(btnGenerate)) {
            calendarFactory.createRandomCalendarList();
        }
    }

    @FXML
    void saveSettings(ActionEvent event) 
    {
        if (comboBoxSelectionSpecialField.getValue() == null || comboBoxSelectionCourse.getValue() == null
                || comboBoxSelectionSemester.getValue() == null) {
            txtError.setText("Nicht alle HWR Komponenten ausgewählt!");
            txtError.setVisible(true);
            txtError.setFill(Color.RED);
        } else {
            txtError.setVisible(false);
            String resultURL = String.format(
                    "https://moodle.hwr-berlin.de/fb2-stundenplan/download.php?doctype=.ics&url=./fb2-stundenplaene/%s/semester%s/kurs%s",
                    comboBoxSelectionSpecialField.getValue().toLowerCase(), comboBoxSelectionSemester.getValue().replace("Sem. ", ""),
                    comboBoxSelectionCourse.getValue().toLowerCase().replaceFirst("keine kurse", ""));
            settings.specialField.set(comboBoxSelectionSpecialField.getValue());
            settings.course.set(comboBoxSelectionCourse.getValue());
            settings.semester.set(comboBoxSelectionSemester.getValue());            
            settings.hwrWebsiteUrl = resultURL;

        }
        //cBToolTips.setTooltip(cBToolTips.getTooltip());
        settings.notificationTimeBeforeEntryInMinutes = (long) Long.valueOf(comboBoxNotificationMin.getValue());
        settings.defaultCalendarForSearchView = comboBoxDefaultCalendar.getValue();
        settings.entrySystemMessageIntervalInMinutes = 1;
        //settings.toolTip = cBToolTips.selectedProperty();
        settings.saveSettings();
    }

   

    public void changeContentPosition(double width, double height) 
    {
        //
    }
}