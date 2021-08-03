package com.altenheim.kalender.interfaces;

import com.altenheim.kalender.models.*;
import com.calendarfx.model.Calendar;

public interface IIOController 
{
    void saveCalendar(Calendar calendar, IExportController exportCt);
    void loadCalendarsFromFile(IEntryFactory entryFactory, IImportController importCt);
    void saveMailTemplatesToFile(MailTemplateModel templates);
    void writeSettings(SettingsModel settings);
    void addSettingsModel(SettingsModel settings);
    void createUserPath();
    void saveContactsToFile(ContactModel contacts);
    void loadContactsFromFile(ContactModel contacts);
    void saveDecryptedPasswordHash(String hashedPassword);  
    String getDecryptedPasswordHash();
    void saveHashedPassword(String passwordHash);
    String loadHashedPassword();
    SettingsModel restoreSettings();
    MailTemplateModel loadMailTemplatesFromFile();

}