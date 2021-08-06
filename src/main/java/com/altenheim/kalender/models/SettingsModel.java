package com.altenheim.kalender.models;

import java.io.*;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import jfxtras.styles.jmetro.Style;

public class SettingsModel
{
    public final static String APICYPHERTEXT = "apCg/0Odtz1r9kYuh011M4sur5xv5UU0hYJQcymI9gpAfMWP1eJWOtgpXu/lawR+";
    public final static String PASSWORDHASH = "54fvHpgroWTcl6h/4SxMEiwchYBcYzqtrXX4eMySjf94gqHjPhjPCPl4d2IH7jg0";

    private String userDirectory = "userfiles/";
    public String decryptedPassword = "";
    public String hwrWebsiteUrl = "https://moodle.hwr-berlin.de/fb2-stundenplan/download.php?doctype=.ics&url=./fb2-stundenplaene/wi/semester2/kursc";
    //public String cssMode = "Light";
    public static String defaultCalendarForSearchView = "";
    public long entrySystemMessageIntervalInMinutes = 1;
    public long notificationTimeBeforeEntryInMinutes = 15;
    public long hwrRequestIntervalInMinutes = (long) 1440;    
    public boolean useAdvancedFeatures = false;
    public boolean isDarkmodeActive = false;

    public transient SimpleStringProperty street = new SimpleStringProperty();
    public transient SimpleStringProperty houseNumber = new SimpleStringProperty();
    public transient SimpleStringProperty zipCode = new SimpleStringProperty();
    public transient SimpleStringProperty city = new SimpleStringProperty();
    public transient SimpleStringProperty mail = new SimpleStringProperty();
    public transient SimpleStringProperty specialField = new SimpleStringProperty("Auswahl FB");
    public transient SimpleStringProperty course = new SimpleStringProperty("Kurs");
    public transient SimpleStringProperty semester = new SimpleStringProperty("Sem.");
    public transient SimpleBooleanProperty toolTip = new SimpleBooleanProperty(false);
    private transient SimpleStringProperty[] settingsInputFieldsContainer = { street, houseNumber, zipCode, city, mail };
    private transient SimpleStringProperty[] settingsDropdownTitlesContainer = { specialField, course, semester };
    public SimpleStringProperty[] getSettingsInputFieldsContainer() { return settingsInputFieldsContainer; }
    public SimpleStringProperty[] getSettingsDropdownTitleCOntainer() { return settingsDropdownTitlesContainer; }  
    public SimpleBooleanProperty getToolTipEnabled() { return toolTip; } 
    public String getPathToUserDirectory() { return userDirectory; }
    public File getPasswordhashFile() { return new File(userDirectory + "savedHash"); }

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
            streamOut.writeBoolean(isDarkmodeActive);
            streamOut.writeUTF(defaultCalendarForSearchView);
            streamOut.writeUTF(hwrWebsiteUrl);
            streamOut.close();
            writeToFile.close();
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }

    public void loadSettings() 
    {
        String path = "userFiles/userSettings/settings.file";
        var file = new File(path);
        if (file.exists() == false)
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
            isDarkmodeActive = inputStream.readBoolean();
            defaultCalendarForSearchView = inputStream.readUTF();
            hwrWebsiteUrl = inputStream.readUTF();
            inputStream.close();
            loadFile.close();
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }    
}