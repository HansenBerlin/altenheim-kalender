package com.altenheim.kalender.controller.viewController;

import com.calendarfx.view.CalendarView;

public class CustomViewOverride extends CalendarView
{
    public CustomViewOverride(String cssPath)
    {
        getStylesheets().add(cssPath);      
    }

    public void updateCss(String oldPath, String newPath) {
        getStylesheets().remove(oldPath);
        getStylesheets().add(newPath); 
    }
}