package com.altenheim.kalender.controller.logicController;

import java.io.File;
import com.altenheim.kalender.controller.viewController.CalendarViewOverride;
import com.altenheim.kalender.interfaces.ICalendarEntriesModel;
import com.altenheim.kalender.interfaces.IIOController;
import com.altenheim.kalender.models.MailTemplateModel;
import com.altenheim.kalender.models.SettingsModel;
import com.calendarfx.model.Calendar;

public class IOController implements IIOController
{

    public void init(CalendarViewOverride calendarView) 
    {
        
    } 
    

    public void writeCalendarFiles(ICalendarEntriesModel allCalendars)
    {
    }

    public void loadCalendarsFromFile()
    {
    }


    // TODO in entry Factory create calendar, create entry, add to view, add to calendar etc.
    private void addCalendarToView(Calendar calendar)
    {
        
        
    }


    public ICalendarEntriesModel restoreCalendars()
    {
        return null;
    }

    public void writeSettings(SettingsModel settings)
    {

    }

    public SettingsModel restoreSettings()
    {
        return null;
    }

    public void writeMailTeamplates(MailTemplateModel templates)
    {

    }

    public MailTemplateModel restoreTemplates()
    {
        return null;
    }

    public void saveExportedCalendar(File file)
    {
        
    }

    public File readImportedCalendar()
    {
        return null;
    }

    
}