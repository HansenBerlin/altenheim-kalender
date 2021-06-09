package com.altenheim.kalender.controller.viewController;

import com.altenheim.kalender.interfaces.*;

public class PlannerViewController extends ResponsiveController
{    
    private CalendarViewOverride customCalendar; 
    private ICalendarEntriesModel allEntries;
    private IAppointmentEntryFactory entryFactory;
    private IImportController importController;
    private IExportController exportController;


    public PlannerViewController(ICalendarEntriesModel allEntries, IAppointmentEntryFactory entryFactory,
        IImportController importController, IExportController exportController)
    {
        this.allEntries = allEntries;
        this.entryFactory = entryFactory;
        this.importController = importController;
        this.exportController = exportController;
    }
    
    public void addCustomCalendarView()
    {
        customCalendar = new CalendarViewOverride();
        childContainer.add(customCalendar, 0, 0, 1, 1);
    }

    public void changeContentPosition() 
    {
        
    }    
}


