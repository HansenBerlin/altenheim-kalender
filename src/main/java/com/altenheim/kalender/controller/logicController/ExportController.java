package com.altenheim.kalender.controller.logicController;

import java.util.List;
import com.altenheim.kalender.controller.viewController.CalendarViewOverride;
import com.altenheim.kalender.interfaces.IAppointmentEntryFactory;
import com.altenheim.kalender.interfaces.IExportController;
import com.altenheim.kalender.models.ContactModel;
import com.altenheim.kalender.models.SettingsModel;

public class ExportController extends IOController implements IExportController
{

    public ExportController(IAppointmentEntryFactory administrateEntries, CalendarViewOverride calendarView,
        List<ContactModel> allContacts, SettingsModel settings) 
    {
        super(administrateEntries, calendarView, allContacts, settings);
    }
    
}
