package com.altenheim.kalender.interfaces;

import java.io.FileNotFoundException;
import java.io.IOException;
import com.altenheim.kalender.models.MailTemplateModel;
import com.altenheim.kalender.models.SettingsModel;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.validate.ValidationException;

public interface IIOController 
{
    public void writeCalendarFiles() throws FileNotFoundException, ValidationException, IOException;
    public void loadCalendarsFromFile() throws FileNotFoundException, IOException, ParserException;
    public void saveContactsToFile() throws IOException;
    public void loadContactsFromFile() throws IOException, ClassNotFoundException;
    public void writeSettings(SettingsModel settings);
    public SettingsModel restoreSettings();
    public void writeMailTemplates(MailTemplateModel templates);
    public MailTemplateModel restoreMailTemplates();    
}
