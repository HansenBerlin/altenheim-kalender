package com.altenheim.kalender.interfaces;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import com.altenheim.kalender.models.MailTemplateModel;
import com.altenheim.kalender.models.SettingsModel;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.validate.ValidationException;

public interface IIOController 
{
    public void writeCalendarFiles() throws FileNotFoundException, ValidationException, IOException;
    public void writeSettings(SettingsModel settings);
    public void writeMailTeamplates(MailTemplateModel templates);
    public void saveExportedCalendar(File file);    
    public boolean restoreCalendars();
    public SettingsModel restoreSettings();
    public MailTemplateModel restoreTemplates();
    public File readImportedCalendar();
    public void loadCalendarsFromFile() throws FileNotFoundException, IOException, ParserException;     
}
