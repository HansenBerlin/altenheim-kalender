﻿package controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Date;
import interfaces.IAppointmentEntryFactory;
import interfaces.IAppointmentSuggestionController;
import interfaces.ICalendarEntriesModel;
import interfaces.IGoogleAPIController;
import interfaces.IMailCreationController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import models.CalendarEntriesModel;
import views.MainCalendarView;

public class MainViewController 
{
    private IAppointmentEntryFactory entryFactory;
    private ICalendarEntriesModel savedEntries;
    private IAppointmentSuggestionController suggestion;
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

    @FXML
    private void initialize()
    {
        googleApis = new GoogleAPIController();
        mailController = new MailCreationController();
        entryFactory = new AppointmentEntryFactory();
        savedEntries = new CalendarEntriesModel(entryFactory);
        suggestion = new AppointmentSuggestionController(savedEntries, entryFactory); 
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
            textDurationToDestination.setText(String.valueOf(timeInMinutes) + " min");
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

    }

    private void openCalendar()
    {
        var calendar = new MainCalendarView();
        calendar.startCalendar();
    }

}