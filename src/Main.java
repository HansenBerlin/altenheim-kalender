import controller.*;
import interfaces.*;
import views.*;
import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Calendar.Style;
import com.calendarfx.view.CalendarView;
import models.CalendarEntriesModel;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalTime;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
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
import views.WayFinding;

public class Main extends Application
{    
    @Override
    public void start(Stage primaryStage) throws Exception 
    {
        CalendarView calendarView = new CalendarView();
        Calendar birthdays = new Calendar("Birthdays");
        Calendar holidays = new Calendar("Holidays");
        birthdays.setStyle(Style.STYLE1);
        holidays.setStyle(Style.STYLE2);
        CalendarSource myCalendarSource = new CalendarSource("My Calendars");
        myCalendarSource.getCalendars().addAll(birthdays, holidays);
        calendarView.getCalendarSources().addAll(myCalendarSource);
        calendarView.setRequestedTime(LocalTime.now());
        Thread updateTimeThread = new Thread("Calendar: Update Time Thread") 
        {
            @Override
            public void run() 
            {
                while (true) 
                {
                    Platform.runLater(() -> {
                        calendarView.setToday(LocalDate.now());
                        calendarView.setTime(LocalTime.now());
                    });
                    try 
                    {
                        // update every 10 seconds
                        sleep(10000);
                    } 
                    catch (InterruptedException e) 
                    {
                        e.printStackTrace();
                    }
                }
            };
        };
        updateTimeThread.setPriority(Thread.MIN_PRIORITY);
        updateTimeThread.setDaemon(true);
        updateTimeThread.start();
        Scene scene = new Scene(calendarView);
        primaryStage.setTitle("Calendar");
        primaryStage.setScene(scene);
        primaryStage.setWidth(1300);
        primaryStage.setHeight(1000);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }
    
    public static void main(String[] args) throws IOException, InterruptedException, URISyntaxException
    {
        launch(args);
        IAppointmentEntryFactory entryFactory = new AppointmentEntryFactory();
        ICalendarEntriesModel savedEntries = new CalendarEntriesModel(entryFactory);
        savedEntries.initializeYear();
        savedEntries.printCalendarDates(5);
        IAppointmentSuggestionController suggestion = new AppointmentSuggestionController(savedEntries, entryFactory);
        var userInteraction = new UserInputView(suggestion);
        userInteraction.askForUserInputInLoop();
        var mailTest = new MailCreationController("testmail@test.de", "Arzttermin", 
            "Ich brauche einen Termin am ... um ...\nMit freundlichem Gruß");
        mailTest.sendMail();
        var testOpeningApi = new OpeningHoursTestView();
        testOpeningApi.userInputSearchQuery();
        userInteraction.askForUserInputInLoop(); 
        var testWayApi = new WayFinding();
        testWayApi.userInputSearchQuery();

    }
}




