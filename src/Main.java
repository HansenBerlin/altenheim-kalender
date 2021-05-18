import controller.AppointmentEntryFactory;
import controller.AppointmentSuggestionController;
import interfaces.IAppointmentEntryFactory;
import interfaces.IAppointmentSuggestionController;
import interfaces.ICalendarEntriesModel;
import models.CalendarEntriesModel;
import views.UserInputView;

public class Main 
{
    public static void main(String[] args) throws Exception 
    {
        IAppointmentEntryFactory entryFactory = new AppointmentEntryFactory();
        ICalendarEntriesModel savedEntries = new CalendarEntriesModel(entryFactory);
        IAppointmentSuggestionController suggestion = new AppointmentSuggestionController(savedEntries);
        var userInteraction = new UserInputView(suggestion);
        userInteraction.askForUserInputInLoop();
    }
}
