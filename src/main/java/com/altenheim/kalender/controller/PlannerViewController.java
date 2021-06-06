package com.altenheim.kalender.controller;

import com.altenheim.kalender.views.CalendarViewOverride;

public class PlannerViewController extends ResponsiveController
{    
    private CalendarViewOverride calendarViewTotal;

    public PlannerViewController()
    {        
        calendarViewTotal = new CalendarViewOverride();
    }   

    public void addCustomCalendarView()
    {
        childContainer.add(calendarViewTotal, 0, 0, 1, 1);
    }

    public void changeContentPosition() 
    {
        
    }    
}


