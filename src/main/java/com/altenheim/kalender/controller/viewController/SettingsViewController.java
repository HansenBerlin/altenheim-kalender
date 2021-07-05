package com.altenheim.kalender.controller.viewController;

import com.altenheim.kalender.models.SettingsModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class SettingsViewController extends ResponsiveController
{ 
    private SettingsModel settings;

    @FXML
    private MenuButton btnMenuSelectionSpecialField, btnMenuSelectionCourse, btnMenuSelectionSemester, btnMenuScrapingIntervall;
    @FXML
    private Button btnImport, btnExport, btnSave;
    @FXML
    private TextField txtInputStreet, txtInputCity, txtInputZipCode, txtInputHouseNumber, txtInputMail;
    @FXML
    private RadioButton rdbtnadressMailFirst, rdbtntoolTipsOn, rdbtntoolTipsOff;
    @FXML
    private Text txtShowScrappingURL, txtShowAdressTitle, txtShowStreet, txtShowHouseNumber, txtShowCity, txtShowZipCode, txtShowMail;
    @FXML
    private MenuItem selectionSpecialFieldInsurance, selectionSpecialFieldWi;
    
    
    public SettingsViewController(SettingsModel settings)
    {
        this.settings = settings;
    }
    
    @FXML
    private void initialize ()
    {
        txtShowStreet.textProperty().bind(settings.getStreet());
        txtShowHouseNumber.textProperty().bind(settings.getHouseNumber());
        txtShowZipCode.textProperty().bind(settings.getZipCOde());
        txtShowCity.textProperty().bind(settings.getCity());
        txtShowMail.textProperty().bind(settings.getMail());
        //entfernen sobald es funktioniert
        txtShowScrappingURL.setText(settings.getCalendarParser());
    }
    
    @FXML 
    void selectionScrapper(ActionEvent event)
    {
        var item = (MenuItem)event.getSource();
     if (item.getId().contains("selection_AuswahlFB_")) 
     {
        btnMenuSelectionSpecialField.setText(item.getText());
     } 
     else if (item.getId().contains("selection_AuswahlKurs_"))
     {
        btnMenuSelectionCourse.setText(item.getText());
     } 
     else if (item.getId().contains("selection_AuswahlSemester_"))
     {
        btnMenuSelectionSemester.setText(item.getText());
     }
    }
      
    @FXML
    void saveSettings(ActionEvent event) 
    {
        settings.setStreet(txtInputStreet.getText());
        settings.setHouseNumber(txtInputHouseNumber.getText());
        settings.setZipCode(txtInputZipCode.getText());
        settings.setCity(txtInputCity.getText());
        settings.setMail(txtInputMail.getText());
        String resultURL = String.format("https://moodle.hwr-berlin.de/fb2-stundenplan/download.php?doctype=.ics&url=./fb2-stundenplaene/%s/semester%c/kurs%s", btnMenuSelectionSpecialField.getText(), btnMenuSelectionSemester.getText().charAt(5), btnMenuSelectionCourse.getText().replaceFirst("keine Kurse", ""));
        settings.setCalendarParser(resultURL);
        //kann später entfernt werden
        txtShowScrappingURL.setText(settings.getCalendarParser());
    }
    public void changeContentPosition() {}
}
