package com.altenheim.kalender.implementations.controller.models;

import java.io.*;

import com.altenheim.kalender.interfaces.models.SettingsModel;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import jfxtras.styles.jmetro.Style;

public class SettingsModelImpl implements SettingsModel
{
    public static final String APICYPHERTEXT = "apCg/0Odtz1r9kYuh011M4sur5xv5UU0hYJQcymI9gpAfMWP1eJWOtgpXu/lawR+";
    public static final String PASSWORDHASH = "54fvHpgroWTcl6h/4SxMEiwchYBcYzqtrXX4eMySjf94gqHjPhjPCPl4d2IH7jg0";
    public static String defaultCalendarForSearchView = "";
    public static String decryptedPassword = "";
    public static String hwrWebsiteUrl = "https://moodle.hwr-berlin.de/fb2-stundenplan/download.php?doctype=.ics&url=./fb2-stundenplaene/wi/semester2/kursc";
    public static boolean useAdvancedFeatures = false;
    public static boolean isDarkmodeActive = false;
    public long entrySystemMessageIntervalInMinutes = 1;
    public long notificationTimeBeforeEntryInMinutes = 15;
    public long hwrRequestIntervalInMinutes = 1440;
    public final transient SimpleStringProperty street = new SimpleStringProperty();
    public final transient SimpleStringProperty houseNumber = new SimpleStringProperty();
    public final transient SimpleStringProperty zipCode = new SimpleStringProperty();
    public final transient SimpleStringProperty city = new SimpleStringProperty();
    public final transient SimpleStringProperty mail = new SimpleStringProperty();
    public final transient SimpleStringProperty specialField = new SimpleStringProperty("Auswahl FB");
    public final transient SimpleStringProperty course = new SimpleStringProperty("Kurs");
    public final transient SimpleStringProperty semester = new SimpleStringProperty("Sem.");
    public final transient SimpleBooleanProperty toolTip = new SimpleBooleanProperty(false);
    public SimpleStringProperty[] getSettingsInputFieldsContainer() { return settingsInputFieldsContainer; }
    public SimpleBooleanProperty getToolTipEnabled() { return toolTip; }
    public void setNotificationTimeBeforeEntryInMinutes(long value) { notificationTimeBeforeEntryInMinutes = value; }
    public long getNotificationTimeBeforeEntryInMinutes() { return notificationTimeBeforeEntryInMinutes; }
    public void setDefaultCalendarForSearchView(String value) { defaultCalendarForSearchView = value; }
    public void setEntrySystemMessageIntervalInMinutes(int value) { entrySystemMessageIntervalInMinutes = value; }
    public long getEntrySystemMessageIntervalInMinutes() { return entrySystemMessageIntervalInMinutes; }
    public void setHwrWebsiteUrl(String value) { hwrWebsiteUrl = value; }
    public long getHwrRequestIntervalInMinutes() { return hwrRequestIntervalInMinutes; }
    public String getPathToUserDirectory() { return userDirectory; }
    public File getPasswordhashFile() { return new File(userDirectory + "savedHash"); }
    private final transient SimpleStringProperty[] settingsInputFieldsContainer = { street, houseNumber, zipCode, city, mail };
    private final transient SimpleStringProperty[] settingsDropdownTitlesContainer = { specialField, course, semester };
    private final String userDirectory = "userfiles/";

    public Style getCssStyle() 
    {
        if (isDarkmodeActive)
            return Style.DARK;
        else
            return Style.LIGHT;
    }

    public void saveSettings()
    {
        String path = "userFiles/userSettings/settings.file";
        try 
        {
            var writeToFile = new FileOutputStream(path);
            var streamOut = new ObjectOutputStream(writeToFile);

            for (var simpleStringProperty : settingsInputFieldsContainer) 
            {
                streamOut.writeUTF(simpleStringProperty.getValueSafe());
            }
            for (var simpleStringProperty : settingsDropdownTitlesContainer) 
            {
                streamOut.writeUTF(simpleStringProperty.getValueSafe());
            }
            streamOut.writeBoolean(toolTip.getValue());
            streamOut.writeLong(hwrRequestIntervalInMinutes);
            streamOut.writeLong(notificationTimeBeforeEntryInMinutes);
            streamOut.writeLong(entrySystemMessageIntervalInMinutes);
            streamOut.writeBoolean(useAdvancedFeatures);
            streamOut.writeUTF(defaultCalendarForSearchView);
            streamOut.writeUTF(hwrWebsiteUrl);
            streamOut.writeBoolean(isDarkmodeActive);
            streamOut.close();
            writeToFile.close();
        }
        catch (IOException e)
        {
            System.err.println(e.getMessage());
        }
    }

    public void loadSettings() 
    {
        String path = "userFiles/userSettings/settings.file";
        var file = new File(path);
        if (!file.exists())
            return;
        try 
        {
            var loadFile = new FileInputStream(path);
            var inputStream = new ObjectInputStream(loadFile);

            for (var simpleStringProperty : settingsInputFieldsContainer) 
            {
                simpleStringProperty.set(inputStream.readUTF());
            }
            for (var simpleStringProperty : settingsDropdownTitlesContainer) 
            {
                simpleStringProperty.set(inputStream.readUTF());
            }
            toolTip.set(inputStream.readBoolean());
            hwrRequestIntervalInMinutes = inputStream.readLong();
            notificationTimeBeforeEntryInMinutes = inputStream.readLong();
            entrySystemMessageIntervalInMinutes = inputStream.readLong();
            useAdvancedFeatures = inputStream.readBoolean();
            defaultCalendarForSearchView = inputStream.readUTF();
            hwrWebsiteUrl = inputStream.readUTF();
            isDarkmodeActive = inputStream.readBoolean();
            inputStream.close();
            loadFile.close();
        } 
        catch (IOException e)
        {
            System.err.println((e.getMessage()));
        }
    }    
}