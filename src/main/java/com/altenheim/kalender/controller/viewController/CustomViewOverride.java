package com.altenheim.kalender.controller.viewController;

import com.altenheim.kalender.interfaces.SettingsModel;
import com.calendarfx.view.CalendarView;
import javafx.application.Platform;
import java.time.LocalTime;
import java.time.LocalDate;
import com.altenheim.kalender.models.SettingsModelImpl;
import com.altenheim.kalender.resourceClasses.StylePresets;

public class CustomViewOverride extends CalendarView 
{
    private String currentPath;
    private SettingsModel settings;

    public CustomViewOverride(SettingsModel settings)
    {
        this.settings = settings;
        initCss();   
        registerTimeUpdate();     
    }

    private void initCss()
    {
        if (SettingsModelImpl.isDarkmodeActive)
            currentPath = StylePresets.LIGHT_CALENDAR_CSS_FILE; 
        else 
            currentPath = StylePresets.DARK_CALENDAR_CSS_FILE;  
        getStylesheets().add(currentPath); 
    }

    public void updateCss() 
    {
        getStylesheets().remove(currentPath);
        initCss();
    }

    private void registerTimeUpdate()
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