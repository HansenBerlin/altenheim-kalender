package com.altenheim.kalender.controller.logicController;

import java.io.File;
import com.altenheim.kalender.interfaces.*;
import com.altenheim.kalender.models.*;
import com.calendarfx.model.Calendar;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

public class IOController implements IIOController 
{
    protected SettingsModel settings;
    private ContactModel contacts;
    private String hashedPassword;
    private IExportController exportCt;
    private IImportController importCt;
    private IEntryFactory entryFactory;

    public IOController(SettingsModel settings, ContactModel contacts, 
        IExportController exportCt, IImportController importCt, IEntryFactory entryFactory) 
    {
        this.settings = settings;
        this.contacts = contacts;
        this.exportCt = exportCt;
        this.importCt = importCt;
        this.entryFactory = entryFactory;
    }

    public void addEntryFactory(IEntryFactory entryFactory)
    {
        this.entryFactory = entryFactory;
    }

    public void saveDecryptedPasswordHash(String hashedPassword) 
    {
        this.hashedPassword = hashedPassword;
    }

    public String getDecryptedPasswordHash() { return hashedPassword; }    

    public void createUserPath() 
    {
        var parentFolder = new File("userFiles");
        if (!parentFolder.exists())
            parentFolder.mkdir();
        String[] folderNames = { "contacts", "calendars", "userSettings", "mailTemplates" };
        for (var folderName : folderNames) {
            var newFolder = new File("userFiles/" + folderName);
            if (!newFolder.exists())
                newFolder.mkdir();
        }
    }

    public void saveCalendar(Calendar calendar) 
    {        
        try 
        {
            String path = settings.getPathToUserDirectory() + "calendars";
            exportCt.exportCalendarAsFile(calendar, path);
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        } 
    }

    public void loadCalendarsFromFile() 
    {
        entryFactory.clearCalendarSourceList();
        var allCalendarFiles = new File(settings.getPathToUserDirectory() + "calendars").listFiles();
        for (var calendarFile : allCalendarFiles)
        {
            if (calendarFile.getAbsolutePath().contains(".ics")) 
            {
                var calendar = importCt.importFile(calendarFile.getAbsolutePath());
                if (calendar != null)                 
                    entryFactory.addCalendarToView(calendar, calendar.getName());                
            }
        }  
    }

    public void saveContactsToFile() {
        var path = settings.getPathToUserDirectory() + "/contacts/contacts.file";
        try {
            var writeToFile = new FileOutputStream(path);
            var convert = new ObjectOutputStream(writeToFile);
            convert.writeObject(contacts.getDataToSerialize());
            convert.close();
            writeToFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadContactsFromFile() {

        var file = new File(settings.getPathToUserDirectory() + "/contacts/contacts.file");
        if (file.exists() == false)
            return;

        try {
            var loadFile = new FileInputStream(file);
            var inputStream = new ObjectInputStream(loadFile);
            var loadedContacts = (List<ContactModel>) inputStream.readObject();
            contacts.rebuildObservableListFromSerializedData(loadedContacts);
            inputStream.close();
            loadFile.close();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }

    }

    public void saveHashedPassword(String passwordHash) {
        var path = settings.getPathToUserDirectory() + "savedHash";
        try {
            var writeToFile = new FileOutputStream(path);
            var convert = new ObjectOutputStream(writeToFile);
            convert.writeObject(passwordHash);
            convert.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String loadHashedPassword() {
        var file = settings.getPasswordhashFile();
        if (!file.exists())
            return "";
        try {
            var loadFile = new FileInputStream(settings.getPathToUserDirectory() + "savedHash");
            var inputStream = new ObjectInputStream(loadFile);
            var passwordHash = (String) inputStream.readObject();
            inputStream.close();
            loadFile.close();
            return passwordHash;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    public void writeSettings(SettingsModel settings) {
        var path = settings.getPathToUserDirectory() + "settings";
        try {
            var writeToFile = new FileOutputStream(path);
            var convert = new ObjectOutputStream(writeToFile);
            convert.writeObject(settings);
            convert.close();
            writeToFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public SettingsModel restoreSettings() {
        var file = new File("userFiles/settings");
        if (!file.exists())
            return null;
        try {
            var loadFile = new FileInputStream("userFiles/settings");
            var inputStream = new ObjectInputStream(loadFile);
            var settings = (SettingsModel) inputStream.readObject();
            inputStream.close();
            loadFile.close();
            return settings;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void writeMailTemplates(MailTemplateModel templates) {
    }

    public MailTemplateModel restoreMailTemplates() {
        return null;
    }

}