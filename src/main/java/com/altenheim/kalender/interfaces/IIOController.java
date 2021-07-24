package com.altenheim.kalender.interfaces;

import java.io.FileNotFoundException;
import java.io.IOException;
import com.altenheim.kalender.models.MailTemplateModel;
import com.altenheim.kalender.models.SettingsModel;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.validate.ValidationException;

public interface IIOController 
{
    void writeCalendarFiles();
    void loadCalendarsFromFile();
    void saveContactsToFile() throws IOException;
    void loadContactsFromFile() throws IOException, ClassNotFoundException;
    void writeSettings(SettingsModel settings);
    SettingsModel restoreSettings();
    void writeMailTemplates(MailTemplateModel templates);
    MailTemplateModel restoreMailTemplates();
    void saveDecryptedPasswordHash(String hashedPassword);
    void saveHashedPassword(String passwordHash);
    String loadHashedPassword();
    void createUserPath();
    String getDecryptedPasswordHash();
    
}
