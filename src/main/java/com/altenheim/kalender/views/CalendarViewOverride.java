package com.altenheim.kalender.views;

import com.calendarfx.view.CalendarView;

public class CalendarViewOverride extends CalendarView
{
    public CalendarViewOverride()
    {
        getStylesheets().add(CalendarViewOverride.class.getResource("calendar.css").toExternalForm());
    }
}