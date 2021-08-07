package com.altenheim.kalender.implementations.controller.viewController;

import com.altenheim.kalender.interfaces.factorys.ComboBoxFactory;
import com.altenheim.kalender.interfaces.factorys.EntryFactory;
import com.altenheim.kalender.interfaces.models.CalendarEntriesModel;
import com.altenheim.kalender.interfaces.models.SettingsModel;
import com.altenheim.kalender.interfaces.viewController.PopupViewController;
import com.altenheim.kalender.implementations.controller.models.SettingsModelImpl;
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
    private final CalendarEntriesModel allCalendars;
    private final EntryFactory calendarFactory;
    private final PopupViewController popupViewController;
    private final ComboBoxFactory comboBoxFactory;
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

    public SettingsViewController(SettingsModel settings, EntryFactory calendarFactory, CalendarEntriesModel allCalendars,
                                  ComboBoxFactory comboBoxFactory, PopupViewController popupViewController)
    {
        this.settings = settings;
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
        
        comboBoxNotificationMin.getSelectionModel().select(String.valueOf(settings.getNotificationTimeBeforeEntryInMinutes()));
        comboBoxSelectionSpecialField.getSelectionModel().select(settings.specialField.getValue());
        comboBoxSelectionSemester.getSelectionModel().select(settings.semester.getValue());
        comboBoxSelectionCourse.getSelectionModel().select(settings.course.getValue());

        comboBoxDefaultCalendar = comboBoxFactory.create(ComboBoxCreate.CALENDERNAMES);
        containerComboBoxDefaultCalendar.getChildren().add(comboBoxDefaultCalendar);
        comboBoxDefaultCalendar.getSelectionModel().select(SettingsModelImpl.defaultCalendarForSearchView);
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
            txtError.setText("Nicht alle HWR Komponenten ausgewählt!");
            txtError.setVisible(true);
            txtError.setFill(Color.RED);
        } else {
            txtError.setVisible(false);
            String resultURL = String.format(
                    "https://moodle.hwr-berlin.de/fb2-stundenplan/download.php?doctype=.ics&url=./fb2-stundenplaene/%s/semester%s/kurs%s",
                    comboBoxSelectionSpecialField.getValue(), comboBoxSelectionSemester.getValue(),
                    comboBoxSelectionCourse.getValue().replaceFirst("keine Kurse", ""));
            settings.specialField.set(comboBoxSelectionSpecialField.getValue());
            settings.course.set(comboBoxSelectionCourse.getValue());
            settings.semester.set(comboBoxSelectionSemester.getValue());            
            settings.setHwrWebsiteUrl(resultURL);
        }
        //cBToolTips.setTooltip(cBToolTips.getTooltip());
        settings.setNotificationTimeBeforeEntryInMinutes((long) Long.valueOf(comboBoxNotificationMin.getValue()));
        settings.setDefaultCalendarForSearchView(comboBoxDefaultCalendar.getValue());
        settings.setEntrySystemMessageIntervalInMinutes(1);
        //settings.toolTip = cBToolTips.selectedProperty();
        settings.saveSettings();
    }

   

    public void changeContentPosition(double width, double height) 
    {
        //
    }    
}