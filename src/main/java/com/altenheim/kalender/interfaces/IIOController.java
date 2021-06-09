package com.altenheim.kalender.interfaces;

import java.io.File;
import com.altenheim.kalender.models.MailTemplateModel;
import com.altenheim.kalender.models.SettingsModel;

public interface IIOController 
{
    public void writeCalendarFiles(ICalendarEntriesModel allCalendars);
    public void writeSettings(SettingsModel settings);
    public void writeMailTeamplates(MailTemplateModel templates);
    public void saveExportedCalendar(File file);    
    public ICalendarEntriesModel restoreCalendars();
    public SettingsModel restoreSettings();
    public MailTemplateModel restoreTemplates();
    public File readImportedCalendar();    
}
