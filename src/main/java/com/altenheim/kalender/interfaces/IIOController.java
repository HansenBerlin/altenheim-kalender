package com.altenheim.kalender.interfaces;

import com.altenheim.kalender.models.*;
import com.calendarfx.model.Calendar;

public interface IIOController 
{
    void saveCalendar(Calendar calendar);
    void loadCalendarsFromFile(IEntryFactory entryFactory);
    void saveMailTemplatesToFile(MailTemplateModel templates);
    void createUserPath();
    void saveContactsToFile(ContactModel contacts);
    void loadContactsFromFile(ContactModel contacts);
    void saveHashedPassword(String passwordHash);
    String loadHashedPassword();
    MailTemplateModel loadMailTemplatesFromFile();
}