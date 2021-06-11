package com.altenheim.kalender.controller.logicController;

import com.altenheim.kalender.controller.viewController.CalendarViewOverride;
import com.altenheim.kalender.interfaces.IAppointmentEntryFactory;
import com.altenheim.kalender.interfaces.IExportController;

public class ExportController extends IOController implements IExportController
{

    public ExportController(IAppointmentEntryFactory administrateEntries, CalendarViewOverride calendarView) 
    {
        super(administrateEntries, calendarView);
    }
    
}
