package com.altenheim.kalender.controller.viewController;

import com.altenheim.kalender.controller.logicController.IOController;
import com.altenheim.kalender.interfaces.*;
import com.altenheim.kalender.models.SettingsModel;
import com.altenheim.kalender.models.UserModel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import java.io.IOException;
import java.util.List;

import javafx.scene.control.CheckBox;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
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
    private IIOController iOController;

    @FXML private MenuButton btnMenuSpecialField, btnMenuCourse, btnMenuSemester, btnMenuImportColour,
        btnMenuCheckEvent, btnMenuNotificationMin, btnMenuNotificationHour;
    @FXML private Button btnImport, btnExport, btnSave, btnCrawl, btnGenerate, btnCreateDummy;
    @FXML private TextField txtTFStreet, txtTFCity, txtTFZipCode, txtTFHouseNumber, txtTFMail;
    @FXML private Text txtAdressTitle, txtStreet, txtHouseNumber, txtCity, txtZipCode, txtMail, 
        txtNotifocationMin, txtNotificationHour;
    @FXML private MenuItem menuItSpecialFieldInsurance, selectionSpecialFieldWi;
    @FXML private CheckBox cBToolTips;
    @FXML private VBox topContainer, bottomContainer;

    public SettingsViewController(SettingsModel settings, IImportController importController, IEntryFactory calendarFactory,
                                  IExportController exportController, ICalendarEntriesModel allCalendars,
                                  IWebsiteScraperController websiteScraper, IPopupViewController popupViewController, IIOController iOController)
    {
        this.settings = settings;
        this.importController = importController;
        this.exportController = exportController;
        this.allCalendars = allCalendars;
        this.websiteScraper = websiteScraper;
        this.calendarFactory = calendarFactory;
        this.popupViewController = popupViewController;
        this.iOController = iOController;
    }

    @FXML
    private void initialize ()
    {    
        //txtStreet.textProperty().bind(settings.getStreet());
        /*
        txtStreet.textProperty().bindBidirectional(settings.getStreet());
        txtHouseNumber.textProperty().bindBidirectional(settings.getHouseNumber());
        txtZipCode.textProperty().bindBidirectional(settings.getZipCOde());
        txtCity.textProperty().bindBidirectional(settings.getCity());
        txtMail.textProperty().bindBidirectional(settings.getMail());
        btnMenuSpecialField.idProperty().bindBidirectional(settings.getSpecialField());
        btnMenuCourse.idProperty().bindBidirectional(settings.getCourse());
        btnMenuSemester.idProperty().bindBidirectional(settings.getSemester());
        cBToolTips.selectedProperty().bindBidirectional(settings.getToolTip()); */

        //settings.setCity("sdsdfsdfsdfsdfsdf-------------------------");
        UserModel.addToList("hallo", "nico");
        txtCity.textProperty().bindBidirectional(new SimpleStringProperty(UserModel.data.get(0).getName()));
        
        
    }  

    private void testObservableCollectionModel()
    {
        

        

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
        UserModel.data.get(0).setName(txtTFCity.getText());
        UserModel.data.get(0).setStreet(txtTFStreet.getText());


        //settings.setStreet(txtStreet.getText());
        //settings.setHouseNumber(txtHouseNumber.getText());
        //settings.setZipCode(txtZipCode.getText());
        //settings.setCity(txtCity.getText());
        //settings.setMail(txtMail.getText());
        String resultURL = String.format("https://moodle.hwr-berlin.de/fb2-stundenplan/download.php?doctype=.ics&url=./fb2-stundenplaene/%s/semester%c/kurs%s", 
            btnMenuSpecialField.getText(), btnMenuSemester.getText().charAt(5), btnMenuCourse.getText().replaceFirst("keine Kurse", ""));
        //settings.setCalendarParser(resultURL);
        //settings.setSpecialField(btnMenuSpecialField.getText());
        //settings.setCourse(btnMenuCourse.getText());
        //settings.setSemester(btnMenuSemester.getText());
        //kann später entfernt werden
        //settings.setToolTip(cBToolTips.selectedProperty().getValue());
        //iOController.writeSettings(settings);        
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