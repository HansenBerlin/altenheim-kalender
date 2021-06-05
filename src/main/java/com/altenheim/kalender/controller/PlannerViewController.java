package com.altenheim.kalender.controller;

import com.altenheim.kalender.views.CalendarViewOverride;
import javafx.scene.layout.AnchorPane;

public class PlannerViewController extends ParentViewController
{    
    private CalendarViewOverride calendarViewTotal;

    public PlannerViewController(AnchorPane parent)
    {        
        super(parent);
        calendarViewTotal = new CalendarViewOverride();
    }   

    public void addCustomCalendarView()
    {
        childContainer.add(calendarViewTotal, 0, 0, 1, 1);
    }
}


