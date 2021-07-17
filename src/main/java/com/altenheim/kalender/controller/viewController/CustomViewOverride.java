package com.altenheim.kalender.controller.viewController;

import com.calendarfx.view.CalendarView;

public class CustomViewOverride extends CalendarView
{
    public CustomViewOverride()
    {
        getStylesheets().add(getClass().getResource("/calendarLight.css").toExternalForm());
        //getStylesheets().add(getClass().getResource("/calendarDark.css").toExternalForm());
    }
}