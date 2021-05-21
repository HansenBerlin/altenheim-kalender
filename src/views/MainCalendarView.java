package views;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import com.calendarfx.model.Calendar;
import com.calendarfx.model.Calendar.Style;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.view.CalendarView;
import controller.AppointmentEntryFactory;
import interfaces.ICalendarEntryModel;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.CalendarEntryModel;

public class MainCalendarView 
{
    public void startCalendar(List<CalendarEntryModel> suggestions, ICalendarEntryModel[] dummys)
    {
        var stage = new Stage();
        var calendarView = new CalendarView();
        var dummyEntries = new Calendar("Dummys");
        var smartAppointments = new Calendar("Smart Appointments");      

        for (ICalendarEntryModel entry : dummys) 
        {
            var newEntry = new Entry<>();
            newEntry.changeStartDate(entry.getStartDate().toZonedDateTime().toLocalDate());
            newEntry.changeEndDate(entry.getEndDate().toZonedDateTime().toLocalDate());
            newEntry.changeStartTime(entry.getStartDate().toZonedDateTime().toLocalTime());
            newEntry.changeStartTime(entry.getEndDate().toZonedDateTime().toLocalTime());
            newEntry.setTitle(entry.getAppointmentEntryName());
            dummyEntries.addEntry(newEntry);            
        }

        for (CalendarEntryModel entry : suggestions) 
        {            
            var newEntry = new Entry<>();
            newEntry.changeStartDate(entry.getStartDate().toZonedDateTime().toLocalDate());
            newEntry.changeEndDate(entry.getEndDate().toZonedDateTime().toLocalDate());
            newEntry.changeStartTime(entry.getStartDate().toZonedDateTime().toLocalTime());
            newEntry.changeStartTime(entry.getEndDate().toZonedDateTime().toLocalTime());
            smartAppointments.addEntry(newEntry);            
        }
       

        dummyEntries.setStyle(Style.STYLE1);
        smartAppointments.setStyle(Style.STYLE2);
        

        var myCalendarSource = new CalendarSource("Meine Kalender");
        myCalendarSource.getCalendars().addAll(dummyEntries, smartAppointments);

        calendarView.getCalendarSources().addAll(myCalendarSource);

        calendarView.setRequestedTime(LocalTime.now());

        var updateTimeThread = new Thread("Calendar: Update Time Thread") 
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
        var scene = new Scene(calendarView);
        stage.setTitle("Calendar");
        stage.setScene(scene);
        stage.setWidth(1300);
        stage.setHeight(1000);
        stage.centerOnScreen();
        stage.show();
    }    
}


