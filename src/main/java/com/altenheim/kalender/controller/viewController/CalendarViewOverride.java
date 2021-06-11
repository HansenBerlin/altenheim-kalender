package com.altenheim.kalender.controller.viewController;

import com.calendarfx.view.CalendarView;

public class CalendarViewOverride extends CalendarView
{
    public CalendarViewOverride()
    {
        //getStylesheets().add(getClass().getResource("/calendarDark.css").toExternalForm());
        getStylesheets().add(getClass().getResource("/calendarLight.css").toExternalForm());

    }
}