package com.altenheim.kalender.controller.logicController;

import java.io.File;

import com.altenheim.kalender.interfaces.ICalendarEntriesModel;
import com.altenheim.kalender.interfaces.IEntryFactory;
import com.altenheim.kalender.interfaces.IIOController;
import com.altenheim.kalender.models.ContactModel;
import com.altenheim.kalender.models.MailTemplateModel;
import com.altenheim.kalender.models.SettingsModel;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import net.fortuna.ical4j.data.*;
import net.fortuna.ical4j.model.property.*;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.validate.ValidationException;

public class IOController implements IIOController
{
    private ICalendarEntriesModel allEntries;
    private List<ContactModel> allContacts;
    protected SettingsModel settings;
    private List<MailTemplateModel> mailTemplates;
    private String hashedPassword;

    public IOController(IEntryFactory administrateEntries, List<ContactModel> allContacts, 
        SettingsModel settings, List<MailTemplateModel> mailTemplates, ICalendarEntriesModel allEntries)
    {
        this.allEntries = allEntries;
        this.allContacts = allContacts;
        this.settings = settings;
        this.mailTemplates = mailTemplates;
    }


    public void saveDecryptedPasswordHash(String hashedPassword) { this.hashedPassword = hashedPassword; }
    public String getDecryptedPasswordHash() { return hashedPassword; }

    public void writeCalendarFiles() throws ValidationException, IOException
    {

    }


    public void loadCalendarsFromFile() throws IOException, ParserException
    {
    }


    public void saveContactsToFile() throws IOException
    {
        var path = settings.getPathToHwrScrapedFIle();
        if (path == null)
            path = "contactFiles/contacts.file";
        var writeToFile = new FileOutputStream(path);
        var convert = new ObjectOutputStream(writeToFile);
        convert.writeObject(allContacts);
        convert.close();
    }


    public void loadContactsFromFile() throws IOException, ClassNotFoundException
    {
        var path = settings.getPathToIcsExportedFile();
        if (path == null)
            path = "contactFiles/contacts.file";
        var loadFile = new FileInputStream(path);
        var inputStream = new ObjectInputStream(loadFile);
        var loadedContacts = (List<ContactModel>)inputStream.readObject();
        allContacts.addAll(loadedContacts);
        inputStream.close();
    }

    public void saveHashedPassword(String passwordHash) throws IOException
    {
        var path = settings.getPathToUserDirectory() + "savedHash";
        if (path == null)
            return;
        var writeToFile = new FileOutputStream(path);
        var convert = new ObjectOutputStream(writeToFile);
        convert.writeObject(passwordHash);
        convert.close();
    }


    public String loadHashedPassword() throws IOException, ClassNotFoundException
    {
        var path = settings.getPathToIcsExportedFile() + "savedHash";
        if (path == null)
            return "";
        var loadFile = new FileInputStream(path);
        var inputStream = new ObjectInputStream(loadFile);
        var passwordHash = (String)inputStream.readObject();
        inputStream.close();
        return passwordHash;
    }

    public void writeSettings(SettingsModel settings)
    {
    }


    public SettingsModel restoreSettings()
    {
        return null;
    }


    public void writeMailTemplates(MailTemplateModel templates)
    {
    }




    public MailTemplateModel restoreMailTemplates()
    {
        return null;
    }  


    private Entry<String> createCalendarFXEntryFromMillis(long start, long end)
	{
		var entry = new Entry<String>();
		var dateStart = LocalDateTime.ofInstant(Instant.ofEpochMilli(start), ZoneId.systemDefault());
		var dateEnd = LocalDateTime.ofInstant(Instant.ofEpochMilli(end), ZoneId.systemDefault());		
		entry.changeStartTime(dateStart.toLocalTime());
		entry.changeStartDate(dateStart.toLocalDate());
		entry.changeEndTime(dateEnd.toLocalTime());
		entry.changeEndDate(dateEnd.toLocalDate());
		return entry;
	} 

    

}