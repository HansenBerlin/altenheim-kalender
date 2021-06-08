package com.altenheim.kalender.controller.viewController;

import com.altenheim.kalender.models.*;
import com.altenheim.kalender.controller.logicController.AppointmentEntryFactory;
import com.altenheim.kalender.controller.logicController.SmartSearchController;
import com.altenheim.kalender.controller.logicController.GoogleAPIController;
import com.altenheim.kalender.controller.logicController.MailCreationController;
import com.altenheim.kalender.interfaces.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class MainViewController implements Initializable 
{
    private IAppointmentEntryFactory entryFactory;
    private ICalendarEntriesModel allCalendars;
    private ISmartSearchController suggestion;
    private IMailCreationController mailController;
    private IGoogleAPIController googleApis;

    @FXML
    private DatePicker datePickerStartDate;
 
    @FXML
    private TextField textFieldRecipient, textFieldSubject, textFieldStartLocation, textFieldDestination, textFieldTime,
                      textFieldSearchFromDay, textFieldInterval, textFieldTolerance, textFieldAvailableDatesCount;
    
    @FXML
    private Button buttonTemplateOne, buttonTemplateTwo, buttonShowOpeningHours, buttonShowDurationToDestination,
                   buttonShowAvaliableDates, buttonOpenCalendar, buttonSendMail;

    @FXML
    private TextArea textAreaMailBody;

    @FXML
    private Text textOpeningHoursStartLocation, textOpeningHoursDestination, textDurationToDestination, textAvailableDates;
    

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) 
    {
        googleApis = new GoogleAPIController();
        mailController = new MailCreationController();
        allCalendars = new CalendarEntriesModel();
        //entryFactory = new AppointmentEntryFactory();
        
        //allCalendars.addCalendar(entryFactory.createEntrys("Test Kalender 1"));      
        suggestion = new SmartSearchController(allCalendars);         
    }    
    
    @FXML
    private void buttonClicked(ActionEvent event) throws IOException, URISyntaxException, InterruptedException 
    {
        var button = (Button)event.getSource();

        if (button.equals(buttonSendMail))
            sendMail(0);
        else if (button.equals(buttonTemplateOne))
            sendMail(1);
        else if (button.equals(buttonTemplateTwo))
            sendMail(2);
        else if (button.equals(buttonShowOpeningHours))
        {
            var openingHoursStartLocation = googleApis.showOpeningHours(textFieldStartLocation.getText());
            var openingHoursDestination = googleApis.showOpeningHours(textFieldDestination.getText());
            textOpeningHoursStartLocation.setText(openingHoursStartLocation);
            textOpeningHoursDestination.setText(openingHoursDestination);
        }
        else if (button.equals(buttonShowDurationToDestination))
        {            
            var timeToDestination = googleApis.searchForDestinationDistance(
                textFieldStartLocation.getText(), textFieldDestination.getText());
            int timeInMinutes = (timeToDestination[0] - timeToDestination[0]%60)/60;
            textDurationToDestination.setText(String.valueOf(timeInMinutes));
        }
        else if (button.equals(buttonShowAvaliableDates))
            checkAvaliableDates();
        else if (button.equals(buttonOpenCalendar))
            openCalendar();
    }   

    private void sendMail(int useTemplateNumber) throws IOException, URISyntaxException
    {
        var mailBody = textAreaMailBody.getText();
        var subject = textFieldSubject.getText();
        var recipient = textFieldRecipient.getText();
        var time = textFieldTime.getText();
        var date = datePickerStartDate.getValue();
        String bodyProcessed = mailController.processPlaceholders(mailBody, date.toString(), time, useTemplateNumber);
        mailController.sendMail(recipient, subject, bodyProcessed);        
    }

    private void checkAvaliableDates()
    {
        //int fromDay = Integer.parseInt(textFieldSearchFromDay.getText());
        //int interval = Integer.parseInt(textFieldInterval.getText());
        //int tolerance = Integer.parseInt(textFieldTolerance.getText());
        //int count = Integer.parseInt(textFieldAvailableDatesCount.getText());
        //int duration = Integer.parseInt(textDurationToDestination.getText());
        //suggestions = suggestion.getAvailableAppointments(fromDay, interval, tolerance, count, 60, duration, 8, 20);
        //suggestions = suggestion.getAvailableAppointments(10, 10, 4, 50, 60, 30, 8, 18);

        var startDate = LocalDate.of(2021, 1, 1);
        var endDate = LocalDate.of(2021, 2, 1);        
        var testGetDates = allCalendars.getSpecificRange(startDate, endDate);

    }

    private void openCalendar()
    {
        //var calendar = new MainCalendarView();
        //calendar.startCalendar(allCalendars.getSpecificCalendarByIndex(0));
    }
}
