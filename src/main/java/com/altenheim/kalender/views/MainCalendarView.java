package com.altenheim.kalender.views;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalTime;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Scene;
import com.calendarfx.model.Calendar;
import com.calendarfx.model.Calendar.Style;
import com.calendarfx.model.CalendarSource;

public class MainCalendarView extends Application
{

    public static void main(String[] args) throws IOException, InterruptedException, URISyntaxException
    {
        launch(args);    
    }
    
    public void start(Stage stage) throws FileNotFoundException
    {
        var calendarView = new CalendarViewOverride();      
        Calendar birthdays = new Calendar("Birthdays");
        Calendar holidays = new Calendar("Holidays");
        birthdays.setStyle(Style.STYLE1);
        holidays.setStyle(Style.STYLE2);



        CalendarSource myCalendarSource = new CalendarSource("My Calendars");
        myCalendarSource.getCalendars().addAll(birthdays, holidays);
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


