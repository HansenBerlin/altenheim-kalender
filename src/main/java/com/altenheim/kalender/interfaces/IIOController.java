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
    void saveMailTemplatesToFile(MailTemplateModel templates);
    void writeSettings(SettingsModel settings);
    void saveDecryptedPasswordHash(String hashedPassword);
    void saveHashedPassword(String passwordHash);
    void createUserPath();
    void addEntryFactory(IEntryFactory entryFactory);
    String loadHashedPassword();
    String getDecryptedPasswordHash();
    MailTemplateModel loadMailTemplatesFromFile();
    SettingsModel restoreSettings();
}