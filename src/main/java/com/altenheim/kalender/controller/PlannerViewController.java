package com.altenheim.kalender.controller;

import com.altenheim.kalender.views.CalendarViewOverride;

public class PlannerViewController extends ResponsiveController
{    
    private CalendarViewOverride customCalendar;     

    public void addCustomCalendarView()
    {
        customCalendar = new CalendarViewOverride();
        childContainer.add(customCalendar, 0, 0, 1, 1);
    }

    public void changeContentPosition() 
    {
        
    }    
}


