package com.altenheim.kalender.controller.logicController;

import java.io.File;
import com.altenheim.kalender.interfaces.*;
import com.altenheim.kalender.interfaces.models.ContactModel;
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
    private IExportController exportController;
    private IImportController importController;    

    public IOController(SettingsModel settings, IExportController exportController, IImportController importController)
    {
        this.settings = settings;
        this.importController = importController;
        this.exportController = exportController;
    }

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
            exportController.exportCalendarAsFile(calendar, path);
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        } 
    }

    public void loadCalendarsFromFile(EntryFactory entryFactory)
    {
        entryFactory.clearCalendarSourceList();
        var allCalendarFiles = new File(settings.getPathToUserDirectory() + "calendars").listFiles();
        for (var calendarFile : allCalendarFiles)
        {
            if (calendarFile.getAbsolutePath().contains(".ics")) 
            {
                var calendar = importController.importFile(calendarFile.getAbsolutePath());
                if (calendar != null)                 
                    entryFactory.addCalendarToView(calendar, calendar.getName());                
            }
        }  
        if (allCalendarFiles.length == 0)
            entryFactory.addCalendarToView(new Calendar(), "Standardkalender");
    }

    public void saveContactsToFile(ContactModel contacts)
    {
        var path = settings.getPathToUserDirectory() + "/contacts/contacts.file";
        try 
        {
            var writeToFile = new FileOutputStream(path);
            var convert = new ObjectOutputStream(writeToFile);
            convert.writeObject(contacts.getDataToSerialize());
            convert.close();
            writeToFile.close();
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }

    public void loadContactsFromFile(ContactModel contacts)
    {
        var file = new File(settings.getPathToUserDirectory() + "/contacts/contacts.file");
        if (file.exists() == false)
            return;

        try 
        {
            var loadFile = new FileInputStream(file);
            var inputStream = new ObjectInputStream(loadFile);
            var loadedContacts = (List<ContactModelImpl>) inputStream.readObject();
            contacts.rebuildObservableListFromSerializedData(loadedContacts);
            inputStream.close();
            loadFile.close();
        } 
        catch (ClassNotFoundException | IOException e) 
        {
            e.printStackTrace();
        }
    }

    public void saveMailTemplatesToFile(MailTemplateModel templates)
    {
        var path = settings.getPathToUserDirectory() + "/mailTemplates/templates.file";
        try 
        {
            var writeToFile = new FileOutputStream(path);
            var convert = new ObjectOutputStream(writeToFile);
            convert.writeObject(templates);
            convert.close();
            writeToFile.close();
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }

    public MailTemplateModel loadMailTemplatesFromFile()
    {
        var file = new File(settings.getPathToUserDirectory() + "/mailTemplates/templates.file");
        if (!file.exists())
            return new MailTemplateModelImpl();
        try 
        {
            var loadFile = new FileInputStream(file);
            var inputStream = new ObjectInputStream(loadFile);
            var mailTemplates = (MailTemplateModelImpl) inputStream.readObject();
            inputStream.close();
            loadFile.close();
            return mailTemplates;
        } 
        catch (IOException | ClassNotFoundException e) 
        {
            e.printStackTrace();
            return new MailTemplateModelImpl();
        }
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
        if (!file.exists())
            return "";
        try 
        {
            var loadFile = new FileInputStream(settings.getPathToUserDirectory() + "savedHash");
            var inputStream = new ObjectInputStream(loadFile);
            var passwordHash = (String) inputStream.readObject();
            inputStream.close();
            loadFile.close();
            return passwordHash;
        } 
        catch (IOException | ClassNotFoundException e) 
        {
            e.printStackTrace();
            return "";
        }
    }
}