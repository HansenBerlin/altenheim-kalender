package controller;

import java.io.IOException;
import java.net.URISyntaxException;
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
import views.OpeningHoursTestView;
import views.UserInputView;
import views.WayFinding;

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
    private TextField textFieldRecipient, textFieldSubject, textFieldStartLocation, textFieldDestination,
                      textFieldSearchFromDay, textFieldInterval, textFieldTolerance, textFieldAvailableDatesCount;
    
    @FXML
    private Button buttonTemplateOne, buttonTemplateTwo, buttonShowOpeningHours, buttonShowDurationToDestination,
                   buttonShowAvaliableDates, buttonOpenCalendar, buttonSendMail;

    @FXML
    private TextArea textAreaMailBody;

    @FXML
    private Text textOpeningHoursStartLocation, textOpeningHoursDestination, textDurationToDestination, textAvailableDates;

    @FXML
    public void initialize()
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
            sendMail(false);
        else if (button.equals(buttonTemplateOne) || button.equals(buttonTemplateTwo))
            sendMail(true);
        else if (button.equals(buttonShowOpeningHours))
            checkApis();
        else if (button.equals(buttonShowDurationToDestination))
            checkApis();
        else if (button.equals(buttonShowAvaliableDates))
            checkAvaliableDates();
        else if (button.equals(buttonOpenCalendar))
            openCalendar();
        

    }   

    private void sendMail(int useTemplateNumber) throws IOException, URISyntaxException
    {
        String mailBody = textAreaMailBody.getText();
        String subject = textFieldSubject.getText();
        String recipient = textFieldRecipient.getText();
        String date = datePickerStartDate.getValue().of(year, month, dayOfMonth)
        String bodyProcessed = mailController.processPlaceholders(mailBody, date, time, useTemplateNumber)


        if (useTemplate)
        {

        }
        else
        {
            mailController = new MailCreationController(textFieldRecipient.getText(), 
                textFieldSubject.getText(), textAreaMailBody.getText());
            mailController.sendMail();
        }
    }

    private void checkApis() throws IOException, InterruptedException
    {
        var googleApi = new 
        var openingHours = new OpeningHoursTestView();
        openingHours.userInputSearchQuery();
        var distanceToDestination = new WayFinding();
        distanceToDestination.userInputSearchQuery(); 
    }

    private void checkAvaliableDates()
    {

    }

    private void openCalendar()
    {

    }

}
