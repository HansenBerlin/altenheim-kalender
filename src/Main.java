import controller.AppointmentEntryFactory;
import controller.AppointmentSuggestionController;
import controller.MailCreationController;
import interfaces.IAppointmentEntryFactory;
import interfaces.IAppointmentSuggestionController;
import interfaces.ICalendarEntriesModel;
import models.CalendarEntriesModel;
import views.OpeningHoursTestView;
import views.UserInputView;
import views.WayFinding;

public class Main 
{
    public static void main(String[] args) throws Exception 
    {
        IAppointmentEntryFactory entryFactory = new AppointmentEntryFactory();
        ICalendarEntriesModel savedEntries = new CalendarEntriesModel(entryFactory);
        IAppointmentSuggestionController suggestion = new AppointmentSuggestionController(savedEntries, entryFactory);
        var userInteraction = new UserInputView(suggestion);
        userInteraction.askForUserInputInLoop();

        //var mailTest = new MailCreationController("testmail@test.de", "Arzttermin", 
        //    "Ich brauche einen Termin am ... um ...\nMit freundlichem Gruß");
        //mailTest.sendMail();

//        var testOpeningApi = new OpeningHoursTestView();
//        testOpeningApi.userInputSearchQuery();
        var testWayApi = new WayFinding();
        testWayApi.userInputSearchQuery();
    }
}
