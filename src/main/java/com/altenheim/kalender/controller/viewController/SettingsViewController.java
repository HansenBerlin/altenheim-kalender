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
    private MenuButton selectionSpecialField, selectionCourse, selectionSemester;
    @FXML
    private Button btnImport, btnExport, btnSave;
    @FXML
    private TextField inputStreet, inputCity, inputZipCode, inputHouseNumber, inputMail;
    @FXML
    private RadioButton adressMailFirst, toolTipsOn, toolTipsOff;
    @FXML
    private Text scrappingURL, showStreet, showHouseNumber, showCity, showZipCode, showMail;
    @FXML
    private MenuItem selectionSpecialFieldInsurance, selectionSpecialFieldWi;
    
    
    public SettingsViewController(SettingsModel settings)
    {
        this.settings = settings;
    }
    
    @FXML
    private void initialize ()
    {
        inputStreet.setText("Straße");
        inputHouseNumber.setText(settings.getHouseNumber());
        inputZipCode.setText(settings.getZipCOde());
        inputCity.setText(settings.getCity());
        scrappingURL.setText(settings.getCalendarParser());
    }

    @FXML
    void changeSpecialField(ActionEvent event) 
    {
        selectionSpecialField.setText(((MenuItem)event.getSource()).getText());
    }

    @FXML
    void changeCourse(ActionEvent event) 
    {
        selectionCourse.setText(((MenuItem)event.getSource()).getText());
    }

    @FXML
    void changeSemester(ActionEvent event) 
    {
        selectionSemester.setText(((MenuItem)event.getSource()).getText());
    }
    
    @FXML
    void saveSettings(ActionEvent event) 
    {
        settings.setStreet(inputStreet.getText());
        showStreet.setText(settings.getStreet());

        settings.setHouseNumber(inputHouseNumber.getText());
        showHouseNumber.setText(settings.getHouseNumber());

        settings.setZipCode(inputZipCode.getText());
        showZipCode.setText(settings.getZipCOde());

        settings.setCity(inputCity.getText());
        showCity.setText(settings.getCity());
        
        settings.setMail(inputMail.getText());
        showMail.setText(settings.getMail());
        
        String resultURL = "https://moodle.hwr-berlin.de/fb2-stundenplan/download.php?doctype=.ics&url=./fb2-stundenplaene/"+ selectionSpecialField.getText() + "/semester"+ selectionSemester.getText().charAt(5) + "/kurs" + selectionCourse.getText().replaceFirst("keine Kurse", "");
        settings.setCalendarParser(resultURL);
        scrappingURL.setText(settings.getCalendarParser());
    }
    public void changeContentPosition() 
    {
        
    }
}