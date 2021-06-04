package com.altenheim.kalender.controller;

import com.altenheim.kalender.views.CalendarViewOverride;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class PlannerViewController extends ParentViewController
{    
    private CalendarViewOverride calendarViewTotal;

    public PlannerViewController(Stage stage, AnchorPane parent)
    {        
        super(stage, parent);
        calendarViewTotal = new CalendarViewOverride();
    }   

    public void addCustomCalendarView()
    {
        childContainerView.add(calendarViewTotal, 0, 0, 1, 1);
    }
}


