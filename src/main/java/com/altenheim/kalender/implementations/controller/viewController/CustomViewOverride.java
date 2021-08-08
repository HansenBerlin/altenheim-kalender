package com.altenheim.kalender.implementations.controller.viewController;

import com.altenheim.kalender.interfaces.models.SettingsModel;
import com.calendarfx.view.CalendarView;
import javafx.application.Platform;
import java.time.LocalTime;
import java.time.LocalDate;
import com.altenheim.kalender.implementations.controller.models.SettingsModelImpl;
import com.altenheim.kalender.resourceClasses.StylePresets;

public class CustomViewOverride extends CalendarView 
{
    private String currentPath;

    public CustomViewOverride(SettingsModel settings)
    {
        initCss(SettingsModelImpl.isDarkmodeActive);
        registerTimeUpdate();     
    }

    private void initCss(boolean isDarkModeActive)
    {
        if (isDarkModeActive)
            currentPath = StylePresets.DARK_CALENDAR_CSS_FILE;
        else 
            currentPath = StylePresets.LIGHT_CALENDAR_CSS_FILE;
        getStylesheets().add(currentPath); 
    }

    public void updateCss() 
    {
        getStylesheets().remove(currentPath);
        initCss(!SettingsModelImpl.isDarkmodeActive);
    }

    private void registerTimeUpdate()
    {
        setRequestedTime(LocalTime.now());

        Thread updateTimeThread = new Thread("Calendar: Update Time Thread") 
        {
            @SuppressWarnings("InfiniteLoopStatement")
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
            }
        };

        updateTimeThread.setPriority(Thread.MIN_PRIORITY);
        updateTimeThread.setDaemon(true);
        updateTimeThread.start();        
    }  
}