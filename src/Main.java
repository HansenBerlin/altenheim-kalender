import controller.AppointmentEntryFactory;
import controller.AppointmentSuggestionController;
import controller.MailCreationController;
import interfaces.IAppointmentEntryFactory;
import interfaces.IAppointmentSuggestionController;
import interfaces.ICalendarEntriesModel;
import interfaces.ICalendarEntryModel;
import models.CalendarEntriesModel;
import models.CalendarEntryModel;
import views.OpeningHoursTestView;
import views.UserInputView;

public class Main 
{
    public static void main(String[] args) throws Exception 
    {
        IAppointmentEntryFactory entryFactory = new AppointmentEntryFactory();
        ICalendarEntriesModel savedEntries = new CalendarEntriesModel(entryFactory);
        savedEntries.initializeYear();
        savedEntries.printCalendarDates(5);
        IAppointmentSuggestionController suggestion = new AppointmentSuggestionController(savedEntries, entryFactory);
        var userInteraction = new UserInputView(suggestion);
        userInteraction.askForUserInputInLoop();        

        //var mailTest = new MailCreationController("testmail@test.de", "Arzttermin", 
        //    "Ich brauche einen Termin am ... um ...\nMit freundlichem Gruß");
        //mailTest.sendMail();

        //var testOpeningApi = new OpeningHoursTestView();
        //testOpeningApi.userInputSearchQuery();
    }
}
