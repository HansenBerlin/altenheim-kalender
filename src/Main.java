import controller.AppointmentEntryFactory;
import controller.AppointmentSuggestionController;
import controller.MailCreationController;
import interfaces.IAppointmentEntryFactory;
import interfaces.IAppointmentSuggestionController;
import interfaces.ICalendarEntriesModel;
import interfaces.ICalendarEntryModel;
import models.CalendarEntriesModel;
import models.CalendarEntryModel;
import views.UserInputView;

public class Main 
{
    public static void main(String[] args) throws Exception 
    {
        ICalendarEntryModel newEntry = new CalendarEntryModel();
        IAppointmentEntryFactory entryFactory = new AppointmentEntryFactory(newEntry);
        ICalendarEntriesModel savedEntries = new CalendarEntriesModel(entryFactory);
        IAppointmentSuggestionController suggestion = new AppointmentSuggestionController(savedEntries, entryFactory);
        var userInteraction = new UserInputView(suggestion);
        userInteraction.askForUserInputInLoop();

        //var mailTest = new MailCreationController("testmail@test.de", "Arzttermin", 
        //    "Ich brauche einen Termin am ... um ...\nMit freundlichem Gruß");
        //mailTest.sendMail();
    }
}
