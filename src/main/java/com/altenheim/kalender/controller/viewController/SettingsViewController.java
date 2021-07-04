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
    private MenuButton selectionSpecialField, selectionCourse, selectionSemester, btnMenuScrapingIntervall;
    @FXML
    private Button btnImport, btnExport, btnSave;
    @FXML
    private TextField inputStreet, inputCity, inputZipCode, inputHouseNumber, inputMail;
    @FXML
    private RadioButton adressMailFirst, toolTipsOn, toolTipsOff;
    @FXML
    private Text scrappingURL, adressTitle, showStreet, showHouseNumber, showCity, showZipCode, showMail;
    @FXML
    private MenuItem selectionSpecialFieldInsurance, selectionSpecialFieldWi;
    
    
    public SettingsViewController(SettingsModel settings)
    {
        this.settings = settings;
    }
    
    @FXML
    private void initialize ()
    {
        showStreet.textProperty().bind(settings.getStreet());
        showHouseNumber.textProperty().bind(settings.getHouseNumber());
        showZipCode.textProperty().bind(settings.getZipCOde());
        showCity.textProperty().bind(settings.getCity());
        showMail.textProperty().bind(settings.getMail());
        //entfernen sobald es funktioniert
        scrappingURL.setText(settings.getCalendarParser());
    }
    
    @FXML 
    void selectionScrapper(ActionEvent event)
    {
        System.out.println(((MenuItem)event.getSource()).getId());
     if (((MenuItem)event.getSource()).getId().contains("selection_AuswahlFB_")) 
     {
        selectionSpecialField.setText(((MenuItem)event.getSource()).getText());
     } 
     else if (((MenuItem)event.getSource()).getId().contains("selection_AuswahlKurs_"))
     {
        selectionCourse.setText(((MenuItem)event.getSource()).getText());
     } 
     else if (((MenuItem)event.getSource()).getId().contains("selection_AuswahlSemester_"))
     {
        selectionSemester.setText(((MenuItem)event.getSource()).getText());
     }
    }
      
    @FXML
    void saveSettings(ActionEvent event) 
    {
        settings.setStreet(inputStreet.getText());
        settings.setHouseNumber(inputHouseNumber.getText());
        settings.setZipCode(inputZipCode.getText());
        settings.setCity(inputCity.getText());
        settings.setMail(inputMail.getText());
        String resultURL = String.format("https://moodle.hwr-berlin.de/fb2-stundenplan/download.php?doctype=.ics&url=./fb2-stundenplaene/%s/semester%c/kurs%s", selectionSpecialField.getText(), selectionSemester.getText().charAt(5), selectionCourse.getText().replaceFirst("keine Kurse", ""));
        //String resultURL = "https://moodle.hwr-berlin.de/fb2-stundenplan/download.php?doctype=.ics&url=./fb2-stundenplaene/"+ selectionSpecialField.getText() + "/semester"+ selectionSemester.getText().charAt(5) + "/kurs" + selectionCourse.getText().replaceFirst("keine Kurse", "");
        settings.setCalendarParser(resultURL);
        //kann später entfernt werden
        scrappingURL.setText(settings.getCalendarParser());
    }
    public void changeContentPosition() {}
}