package com.altenheim.kalender.interfaces;

import java.io.IOException;
import com.altenheim.kalender.models.*;
import com.calendarfx.model.Calendar;

public interface IIOController 
{
    void saveCalendar(Calendar calendar);
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
    void addEntryFactory(IEntryFactory entryFactory);
}
