package com.altenheim.kalender.controller.logicController;

import java.io.File;

import com.altenheim.kalender.controller.viewController.CustomViewOverride;
import com.altenheim.kalender.interfaces.ICalendarEntriesModel;
import com.altenheim.kalender.interfaces.IEntryFactory;
import com.altenheim.kalender.interfaces.IExportController;
import com.altenheim.kalender.interfaces.IIOController;
import com.altenheim.kalender.interfaces.IImportController;
import com.altenheim.kalender.models.ContactModel;
import com.altenheim.kalender.models.MailTemplateModel;
import com.altenheim.kalender.models.SettingsModel;

import net.fortuna.ical4j.validate.ValidationException;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

public class IOController implements IIOController {
    protected SettingsModel settings;
    private ContactModel contacts;
    private String hashedPassword;
    private ICalendarEntriesModel calendarEntriesModel;
    private IExportController exportCt;
    private IImportController importCt;
    private IEntryFactory entryFactory;
    private CustomViewOverride calendarView;

    public IOController(IEntryFactory entryFactory, SettingsModel settings, ContactModel contacts,
            ICalendarEntriesModel calendarEntriesModel, IExportController exportCt, IImportController importCt,
            CustomViewOverride calendarView) {
        this.settings = settings;
        this.contacts = contacts;
        this.calendarEntriesModel = calendarEntriesModel;
        this.exportCt = exportCt;
        this.importCt = importCt;
        this.entryFactory = entryFactory;
        this.calendarView = calendarView;
    }

    public void saveDecryptedPasswordHash(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public String getDecryptedPasswordHash() {
        return hashedPassword;
    }

    public void writeCalendarFiles() {
        String path = settings.getPathToUserDirectory() + "calendarBackup";
        var folder = new File(path);
        for (var file : folder.listFiles())
            file.delete();

        for (var calSource : calendarView.getCalendarSources()) {
            String pathSource = path + "/" + calSource.getName();
            var newFolder = new File(pathSource);
            if (!newFolder.exists())
                newFolder.mkdir();

            for (var cal : calSource.getCalendars()) 
                if (cal.findEntries(LocalDate.MIN, LocalDate.MAX, ZoneId.systemDefault()).size() > 0) 
                    try {
                        exportCt.exportCalendarAsFile(cal, pathSource);
                    } catch (ValidationException | IOException e) {
                        e.printStackTrace();
                    }   
        }
    }

    public void createUserPath() {
        var parentFolder = new File("userFiles");
        if (!parentFolder.exists())
            parentFolder.mkdir();
        String[] folderNames = { "calendarBackup", "contacts", "crawledCalendarFiles", "exportedCalendars",
                "userSettings" };

        for (var folderName : folderNames) {
            var newFolder = new File("userFiles/" + folderName);
            if (!newFolder.exists())
                newFolder.mkdir();
        }
    }

    public void loadCalendarsFromFile() {
        var calDirectorys = new File(settings.getPathToUserDirectory() + "calendarBackup/");
        for (var calDirectory : calDirectorys.listFiles())
            if (calDirectory.isDirectory())
                for (var calFile : calDirectory.listFiles())
                    if (calFile.getAbsolutePath().contains(".ics")) {
                        var cal = importCt.importFile(calFile.getAbsolutePath());
                        if (cal != null && cal.findEntries(LocalDate.MIN, LocalDate.MAX, ZoneId.systemDefault()).size() > 0) {
                            calendarEntriesModel.addCalendar(cal);
                            entryFactory.addCalendarToView(cal, calDirectory.getName());
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
            contacts.rebuildObservablaListFromSerializedData(loadedContacts);
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