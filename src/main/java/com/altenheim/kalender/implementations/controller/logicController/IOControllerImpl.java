package com.altenheim.kalender.implementations.controller.logicController;

import java.io.File;

import com.altenheim.kalender.interfaces.factorys.EntryFactory;
import com.altenheim.kalender.interfaces.logicController.ExportController;
import com.altenheim.kalender.interfaces.logicController.IOController;
import com.altenheim.kalender.interfaces.logicController.ImportController;
import com.altenheim.kalender.interfaces.models.ContactModel;
import com.altenheim.kalender.interfaces.models.MailTemplateModel;
import com.altenheim.kalender.interfaces.models.SettingsModel;
import com.altenheim.kalender.implementations.controller.models.*;
import com.calendarfx.model.Calendar;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Objects;

public record IOControllerImpl(SettingsModel settings,
                               ExportController exportController,
                               ImportController importController) implements IOController
{

    public void createUserPath() {
        var parentFolder = new File("userFiles");
        if (!parentFolder.exists())
            parentFolder.mkdir();
        String[] folderNames = {"contacts", "calendars", "userSettings", "mailTemplates", "hwr-calendars"};
        for (var folderName : folderNames) {
            var newFolder = new File("userFiles/" + folderName);
            if (!newFolder.exists())
                newFolder.mkdir();
        }
    }

    public void saveCalendar(Calendar calendar) {
        try {
            String path = SettingsModelImpl.userDirectory + "calendars";
            exportController.exportCalendarAsFile(calendar, path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadCalendarsFromFile(EntryFactory entryFactory) {
        entryFactory.clearCalendarSourceList();
        var allCalendarFiles = new File(SettingsModelImpl.userDirectory + "calendars").listFiles();
        for (var calendarFile : Objects.requireNonNull(allCalendarFiles)) {
            if (calendarFile.getAbsolutePath().contains(".ics")) {
                if (!importController.canCalendarFileBeImported(calendarFile.getAbsolutePath()))
                    continue;
                if (importController.canCalendarFileBeParsed())
                    importController.importCalendar("");
            }
        }
    }

    public void saveContactsToFile(ContactModel contacts) {
        var path = SettingsModelImpl.userDirectory + "contacts/contacts.file";
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

    public void loadContactsFromFile(ContactModel contacts) {
        var file = new File(SettingsModelImpl.userDirectory + "contacts/contacts.file");
        if (!file.exists())
            return;

        try {
            var loadFile = new FileInputStream(file);
            var inputStream = new ObjectInputStream(loadFile);
            var loadedContacts = (List<ContactModelImpl>) inputStream.readObject();
            contacts.rebuildObservableListFromSerializedData(loadedContacts);
            inputStream.close();
            loadFile.close();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }

    public void saveMailTemplatesToFile(MailTemplateModel templates) {
        var path = SettingsModelImpl.userDirectory + "mailTemplates/templates.file";
        try {
            var writeToFile = new FileOutputStream(path);
            var convert = new ObjectOutputStream(writeToFile);
            convert.writeObject(templates);
            convert.close();
            writeToFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public MailTemplateModel loadMailTemplatesFromFile() {
        var file = new File(SettingsModelImpl.userDirectory + "mailTemplates/templates.file");
        if (!file.exists())
            return new MailTemplateModelImpl();
        try {
            var loadFile = new FileInputStream(file);
            var inputStream = new ObjectInputStream(loadFile);
            var mailTemplates = (MailTemplateModelImpl) inputStream.readObject();
            inputStream.close();
            loadFile.close();
            return mailTemplates;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new MailTemplateModelImpl();
        }
    }

    public void saveHashedPassword(String passwordHash) {
        var path = SettingsModelImpl.userDirectory + "savedHash";
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
            var loadFile = new FileInputStream(SettingsModelImpl.userDirectory + "savedHash");
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
}