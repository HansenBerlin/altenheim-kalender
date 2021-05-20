package controller;

import java.io.IOException;
import java.net.URISyntaxException;

import interfaces.IAppointmentEntryFactory;
import interfaces.IAppointmentSuggestionController;
import interfaces.ICalendarEntriesModel;
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

    @FXML
    private DatePicker datePickerStartDate, datePickerEndDate;
 
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
        entryFactory = new AppointmentEntryFactory();
        savedEntries = new CalendarEntriesModel(entryFactory);
        suggestion = new AppointmentSuggestionController(savedEntries, entryFactory);
        //var userInteraction = new UserInputView(suggestion);
        //userInteraction.askForUserInputInLoop();
    }
        
          
    
    @FXML
    private void buttonClicked(ActionEvent event) throws IOException, URISyntaxException 
    {
        var button = (Button)event.getSource();

        if (button.equals(buttonSendMail))
            sendMail(false);
        else if (button.equals(buttonTemplateOne) || button.equals(buttonTemplateTwo))
        

    }

    @FXML
    private void datePickerChange(ActionEvent event) 
    {

    }

    private void sendMail(boolean useTemplate) throws IOException, URISyntaxException
    {
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
        var testOpeningApi = new OpeningHoursTestView();
        testOpeningApi.userInputSearchQuery();
        var testWayApi = new WayFinding();
        testWayApi.userInputSearchQuery(); 
    }

}
