package com.altenheim.kalender.controller.viewController;

import com.altenheim.kalender.interfaces.*;

public class PlannerViewController extends ResponsiveController
{    
    private CalendarViewOverride customCalendar; 
    private ICalendarEntriesModel allEntries;
    private IEntryFactory entryFactory;
    private IImportController importController;
    private IExportController exportController;


    public PlannerViewController(ICalendarEntriesModel allEntries, IEntryFactory entryFactory, 
        IImportController importController, IExportController exportController, CalendarViewOverride custumCalendar)
    {
        this.allEntries = allEntries;
        this.entryFactory = entryFactory;
        this.importController = importController;
        this.exportController = exportController;
        this.customCalendar = custumCalendar;        
    }
    
    public void addCustomCalendarView()
    {
        childContainer.add(this.customCalendar, 0, 0, 1, 1);
    }

    public void changeContentPosition() 
    {
        
    }    
}


