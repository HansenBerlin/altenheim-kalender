package com.altenheim.kalender.controller.viewController;

import com.calendarfx.view.CalendarView;
import com.altenheim.kalender.models.SettingsModel;
import com.altenheim.kalender.resourceClasses.StylePresets;

public class CustomViewOverride extends CalendarView 
{
    private String currentPath;
    private SettingsModel settings;

    public CustomViewOverride(SettingsModel settings) 
    {
        this.settings = settings;
        initCss();        
    }

    private void initCss()
    {
        if (settings.isDarkmodeActive)
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
}