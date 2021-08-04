package com.altenheim.kalender.controller.viewController;

import com.calendarfx.view.CalendarView;
import javafx.application.Platform;
import java.time.LocalDate;
import java.time.LocalTime;
import com.altenheim.kalender.resourceClasses.StylePresets;

public class CustomViewOverride extends CalendarView {
    public CustomViewOverride(String cssMode) {
        switch (cssMode) {
            case "Light":
                getStylesheets().add(com.altenheim.kalender.resourceClasses.StylePresets.LIGHT_CALENDAR_CSS_FILE);
                break;
            case "Dark":
                getStylesheets().add(StylePresets.DARK_CALENDAR_CSS_FILE);
                break;
            default:
                getStylesheets().add(StylePresets.LIGHT_CALENDAR_CSS_FILE);
                break;
        }
        registerTimeUpdate();
    }

    public void updateCss(String oldPath, String newPath) {
        getStylesheets().remove(oldPath);
        getStylesheets().add(newPath);
    }

public void registerTimeUpdate()
{
setRequestedTime(LocalTime.now());

    Thread updateTimeThread = new Thread("Calendar: Update Time Thread") 
    {
        public void run() 
        {
            while (true) 
            {
                Platform.runLater(() -> {
                    setToday(LocalDate.now());
                    setTime(LocalTime.now());
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
}
    
}