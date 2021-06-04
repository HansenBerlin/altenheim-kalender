package com.altenheim.kalender.views;

import com.calendarfx.view.CalendarView;

public class CalendarViewOverride extends CalendarView
{
    public CalendarViewOverride()
    {
        getStylesheets().add(getClass().getResource("/calendar2.css").toExternalForm());

    }
}