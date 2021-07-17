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
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class SettingsViewController extends ResponsiveController
{
    private SettingsModel settings;
    private IImportController importController;
    private IExportController exportController;
    private ICalendarEntriesModel allCalendars;
    private IEntryFactory calendarFactory;
    private IPopupViewController popupViewController;
    private IIOController iOController;
    private IComboBoxFactory comboBoxFactory;
    private ComboBox<Integer> comboBoxNotificationHour, comboBoxNotificationMin;    

    @FXML
    private MenuButton btnMenuSpecialField, btnMenuCourse, btnMenuSemester, btnMenuImportColour;
    @FXML
    private Button btnImport, btnExport, btnSave, btnCrawl, btnGenerate;
    @FXML
    private TextField txtTFStreet, txtTFCity, txtTFZipCode, txtTFHouseNumber, txtTFMail;
    @FXML
    private Text txtScrappingURL, txtAdressTitle, txtStreet, txtHouseNumber, txtCity, txtZipCode, txtMail, 
        txtNotifocationMin, txtNotificationHour;
    @FXML private MenuItem menuItSpecialFieldInsurance, selectionSpecialFieldWi;
    @FXML private CheckBox cBToolTips;
    @FXML private VBox topContainer, bottomContainer;

    public SettingsViewController(SettingsModel settings, IImportController importController, IEntryFactory calendarFactory,
                                  IExportController exportController, ICalendarEntriesModel allCalendars,
                                  IPopupViewController popupViewController, IIOController iOController)
    {
        this.settings = settings;
        this.importController = importController;
        this.exportController = exportController;
        this.allCalendars = allCalendars;
        this.calendarFactory = calendarFactory;
        this.popupViewController = popupViewController;
        this.iOController = iOController;
        this.comboBoxFactory = comboBoxFactory;
    }

    @FXML
    private void initialize ()
    {   

        Text[] stringPropertiesCollectionText = { txtStreet, txtHouseNumber, txtZipCode, txtCity, txtMail };
        TextField[] stringPropertiesCollectionTextField = { txtTFStreet, txtTFHouseNumber, txtTFZipCode, txtTFCity, txtTFMail };

        for (int i = 0; i < stringPropertiesCollectionTextField.length; i++) 
        {
            stringPropertiesCollectionTextField[i].textProperty().bindBidirectional(settings.getSettingsInputFieldsContainer()[i]);   
            stringPropertiesCollectionText[i].textProperty().bindBidirectional(settings.getSettingsInputFieldsContainer()[i]); 
        }
        btnMenuSpecialField.textProperty().bindBidirectional(settings.specialField);
        btnMenuCourse.textProperty().bindBidirectional(settings.course);
        btnMenuSemester.textProperty().bindBidirectional(settings.semester); 
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
        settings.writeSimpleProperties();


        /*
        String resultURL = String.format("https://moodle.hwr-berlin.de/fb2-stundenplan/download.php?doctype=.ics&url=./fb2-stundenplaene/%s/semester%c/kurs%s", 
            btnMenuSpecialField.getText(), btnMenuSemester.getText().charAt(5), btnMenuCourse.getText().replaceFirst("keine Kurse", ""));
               */
    }    


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