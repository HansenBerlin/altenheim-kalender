package com.altenheim.kalender.implementations.controller.viewController;

import com.altenheim.kalender.interfaces.factorys.ComboBoxFactory;
import com.altenheim.kalender.interfaces.factorys.EntryFactory;
import com.altenheim.kalender.interfaces.logicController.UrlRequestController;
import com.altenheim.kalender.interfaces.viewController.CalendarEntriesController;
import com.altenheim.kalender.interfaces.models.SettingsModel;
import com.altenheim.kalender.interfaces.viewController.PopupViewController;
import com.altenheim.kalender.resourceClasses.ComboBoxCreate;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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
    private final SettingsModel settings;
    private final CalendarEntriesController allCalendars;
    private final EntryFactory calendarFactory;
    private final PopupViewController popupViewController;
    private final ComboBoxFactory comboBoxFactory;
    private final UrlRequestController urlRequestController;
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

    public SettingsViewController(SettingsModel settings, EntryFactory calendarFactory, CalendarEntriesController allCalendars,
                                  ComboBoxFactory comboBoxFactory, PopupViewController popupViewController, UrlRequestController urlRequestController)
    {
        this.settings = settings;
        this.allCalendars = allCalendars;
        this.calendarFactory = calendarFactory;
        this.popupViewController = popupViewController;
        this.comboBoxFactory = comboBoxFactory;
        this.urlRequestController = urlRequestController;
    }

    @FXML
    private void initialize() 
    {
        createComboBoxes();
        bindInputFieldsToSerializable();
        txtError.setText("Nicht alle HWR Komponenten ausgewählt!");
        txtError.setVisible(false);
        txtError.setFill(Color.RED);
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
        comboBoxDefaultCalendar = comboBoxFactory.create(ComboBoxCreate.CALENDERNAMES);

        containerComboBoxNotificationMin.getChildren().add(comboBoxNotificationMin);
        containerComboBoxSelectorScrapping.getChildren().add(comboBoxSelectionSpecialField);
        containerComboBoxSelectorScrapping.getChildren().add(comboBoxSelectionCourse);
        containerComboBoxSelectorScrapping.getChildren().add(comboBoxSelectionSemester);
        containerComboBoxDefaultCalendar.getChildren().add(comboBoxDefaultCalendar);

        comboBoxSelectionSpecialField.getSelectionModel().select(String.valueOf(settings.getSettingsDropdownTitlesContainer()[0].getValue()));
        comboBoxSelectionCourse.getSelectionModel().select(String.valueOf(settings.getSettingsDropdownTitlesContainer()[1].getValue()));
        comboBoxSelectionSemester.getSelectionModel().select(String.valueOf(settings.getSettingsDropdownTitlesContainer()[2].getValue()));
        comboBoxNotificationMin.getSelectionModel().select(String.valueOf(settings.getSettingsDropdownTitlesContainer()[3].getValue()));
        comboBoxDefaultCalendar.getSelectionModel().select(String.valueOf(settings.getSettingsDropdownTitlesContainer()[4].getValue()));
    }

    @FXML
    private void buttonClicked(ActionEvent event) {
        var button = (Button) event.getSource();

        if (button.equals(btnImport)) {
            var stage = button.getScene().getWindow();
            popupViewController.importDialog(calendarFactory, stage);
        } else if (button.equals(btnExport)) {
            var stage = button.getScene().getWindow();
            popupViewController.exportDialog(allCalendars, stage);
        } else if (button.equals(btnGenerate)) {
            calendarFactory.createRandomCalendarList();
        }
    }

    @FXML
    private void saveSettings()
    {
        if (comboBoxSelectionSpecialField.getValue() == null || comboBoxSelectionCourse.getValue() == null
                || comboBoxSelectionSemester.getValue() == null) {
            txtError.setVisible(true);
        } else {
            txtError.setVisible(false);
            String resultURL = String.format(
                    "https://moodle.hwr-berlin.de/fb2-stundenplan/download.php?doctype=.ics&url=./fb2-stundenplaene/%s/semester%s/kurs%s",
                    comboBoxSelectionSpecialField.getValue().toLowerCase(),
                    comboBoxSelectionSemester.getValue().replace("Sem. ", ""),
                    comboBoxSelectionCourse.getValue().toLowerCase().replaceFirst("keine kurse", ""));
            settings.setHwrWebsiteUrl(resultURL);
            settings.setSelectedHwrCourseName(comboBoxSelectionSpecialField.getSelectionModel().getSelectedItem());
            settings.getSettingsDropdownTitlesContainer()[0].set(comboBoxSelectionSpecialField.getValue());
            settings.getSettingsDropdownTitlesContainer()[1].set(comboBoxSelectionCourse.getValue());
            settings.getSettingsDropdownTitlesContainer()[2].set(comboBoxSelectionSemester.getValue());
            settings.getSettingsDropdownTitlesContainer()[3].set(comboBoxNotificationMin.getValue());
            settings.getSettingsDropdownTitlesContainer()[4].set(comboBoxDefaultCalendar.getValue());
            settings.setNotificationTimeBeforeEntryInMinutes(Long.parseLong(comboBoxNotificationMin.getValue()));
            settings.setDefaultCalendarForSearchView(comboBoxDefaultCalendar.getValue());
            settings.setEntrySystemMessageIntervalInMinutes(1);
            settings.saveSettings();
            if (urlRequestController.isCalendarImportedSuccesfully())
                popupViewController.showCalendarImportedSuccess();
            else
                popupViewController.showCalendarImportedError();
        }
    }

   

    public void changeContentPosition(double width, double height) 
    {
        //
    }    
}