package com.altenheim.kalender.controller.logicController;

import java.io.File;

import com.altenheim.kalender.interfaces.ICalendarEntriesModel;
import com.altenheim.kalender.interfaces.IEntryFactory;
import com.altenheim.kalender.interfaces.IIOController;
import com.altenheim.kalender.models.ContactModel;
import com.altenheim.kalender.models.MailTemplateModel;
import com.altenheim.kalender.models.SerializableEntry;
import com.altenheim.kalender.models.SettingsModel;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import net.fortuna.ical4j.data.*;
import net.fortuna.ical4j.validate.ValidationException;

public class IOController implements IIOController
{
    private ICalendarEntriesModel allEntries;
    private List<ContactModel> allContacts;
    protected SettingsModel settings;
    private MailTemplateModel mailTemplates;
    private String hashedPassword;

    public IOController(IEntryFactory administrateEntries, List<ContactModel> allContacts, 
        SettingsModel settings, MailTemplateModel mailTemplates, ICalendarEntriesModel allEntries)
    {
        this.allEntries = allEntries;
        this.allContacts = allContacts;
        this.settings = settings;
        this.mailTemplates = mailTemplates;
    }


    public void saveDecryptedPasswordHash(String hashedPassword)
    {
        this.hashedPassword = hashedPassword;
    }
    public String getDecryptedPasswordHash() { return hashedPassword; }

    public void writeCalendarFiles() throws ValidationException, IOException
    {

    }

    public void createUserPath()
    {
        var parentFolder = new File("userFiles");
        if (!parentFolder.exists())
            parentFolder.mkdir();
        String[] folderNames = {"calendarBackup", "contacts", "crawledCalendarFiles", "exportedCalendars", "userSettings" };

        for (var folderName : folderNames)
        {
            var newFolder = new File("userFiles/" + folderName);
            if (!newFolder.exists())
                newFolder.mkdir();
        }
    }


    public void loadCalendarsFromFile() throws IOException, ParserException
    {
    }


    public void saveContactsToFile() throws IOException
    {
        var path = settings.getPathToHwrScrapedFile();
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

    public void saveHashedPassword(String passwordHash)
    {
        var path = settings.getPathToUserDirectory() + "savedHash";
        try
        {
            var writeToFile = new FileOutputStream(path);
            var convert = new ObjectOutputStream(writeToFile);
            convert.writeObject(passwordHash);
            convert.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    public String loadHashedPassword()
    {
        var file = settings.getPasswordhashFile();
        if(!file.exists())
            return "";
        try
        {
            var loadFile = new FileInputStream(settings.getPathToUserDirectory() + "savedHash");
            var inputStream = new ObjectInputStream(loadFile);
            var passwordHash = (String)inputStream.readObject();
            inputStream.close();
            return passwordHash;
        }
        catch (IOException | ClassNotFoundException e)
        {
            e.printStackTrace();
            return "";
        }
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


    private SerializableEntry createCalendarFXEntryFromMillis(long start, long end)
	{
		var entry = new SerializableEntry();
		var dateStart = LocalDateTime.ofInstant(Instant.ofEpochMilli(start), ZoneId.systemDefault());
		var dateEnd = LocalDateTime.ofInstant(Instant.ofEpochMilli(end), ZoneId.systemDefault());		
		entry.changeStartTime(dateStart.toLocalTime());
		entry.changeStartDate(dateStart.toLocalDate());
		entry.changeEndTime(dateEnd.toLocalTime());
		entry.changeEndDate(dateEnd.toLocalDate());
		return entry;
	} 

    

}