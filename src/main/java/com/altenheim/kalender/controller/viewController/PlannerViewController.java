package com.altenheim.kalender.controller.viewController;

import com.altenheim.kalender.interfaces.*;
import com.calendarfx.view.CalendarView;

public class PlannerViewController extends ResponsiveController
{    
    private CalendarView customCalendar;
    private ICalendarEntriesModel allEntries;
    private IEntryFactory entryFactory;
    private IImportController importController;
    private IExportController exportController;


    public PlannerViewController(ICalendarEntriesModel allEntries, IEntryFactory entryFactory, 
        IImportController importController, IExportController exportController, CalendarView custumCalendar)
    {
        this.allEntries = allEntries;
        this.entryFactory = entryFactory;
        this.importController = importController;
        this.exportController = exportController;
        this.customCalendar = custumCalendar;
    }
    
    public void updateCustomCalendarView(CalendarView calendarView)
    {
        if (childContainer.getChildren().contains(this.customCalendar))
        {
            childContainer.getChildren().remove(this.customCalendar);
            this.customCalendar = calendarView;
        }
        childContainer.add(this.customCalendar, 0, 0, 1, 1);
    }

    public void changeContentPosition() 
    {
    }    
}


