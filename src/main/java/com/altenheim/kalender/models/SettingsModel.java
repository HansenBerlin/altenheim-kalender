package com.altenheim.kalender.models;

import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

public class SettingsModel implements Serializable 
{
    public final static String APICYPHERTEXT = "apCg/0Odtz1r9kYuh011M4sur5xv5UU0hYJQcymI9gpAfMWP1eJWOtgpXu/lawR+";
    public final static String PASSWORDHASH = "54fvHpgroWTcl6h/4SxMEiwchYBcYzqtrXX4eMySjf94gqHjPhjPCPl4d2IH7jg0";

    private String userDirectory = "userfiles/";
    private String decryptedPassword = "";
    private String pathToSaveBackupFiles = null;
    private String scrappingURL = "";
    public long entrySystemMessageIntervalInMinutes = 1;
    public long notificationTimeBeforeEntryInMinutes = 15;
    private Long scrapingIntervalInMinutes = (long) 60000;    
    public String cssMode = "Light";
    public String defaultCalendarForSearchView = "";
    public String url = "https://moodle.hwr-berlin.de/fb2-stundenplan/download.php?doctype=.ics&url=./fb2-stundenplaene/wi/semester2/kursc";
    private boolean useAdvancedFeatures = false;

    public transient SimpleStringProperty street = new SimpleStringProperty();
    public transient SimpleStringProperty houseNumber = new SimpleStringProperty();
    public transient SimpleStringProperty zipCode = new SimpleStringProperty();
    public transient SimpleStringProperty city = new SimpleStringProperty();
    public transient SimpleStringProperty mail = new SimpleStringProperty();
    public transient SimpleStringProperty specialField = new SimpleStringProperty("Auswahl FB");
    public transient SimpleStringProperty course = new SimpleStringProperty("Kurs");
    public transient SimpleStringProperty semester = new SimpleStringProperty("Sem.");
    public transient SimpleBooleanProperty toolTip = new SimpleBooleanProperty(false);
    public transient PropertyChangeSupport propertyChange = new PropertyChangeSupport(this);
    private transient  SimpleStringProperty[] settingsInputFieldsContainer = { street, houseNumber, zipCode, city, mail };
    private transient  SimpleStringProperty[] settingsDropdownTitlesContainer = { specialField, course, semester };
    public SimpleStringProperty[] getSettingsInputFieldsContainer() { return settingsInputFieldsContainer; }
    public SimpleStringProperty[] getSettingsDropdownTitleCOntainer() { return settingsDropdownTitlesContainer;}  
    
    public String getPathToUserDirectory() { return userDirectory; }
    public File getPasswordhashFile() { return new File(userDirectory + "savedHash"); }
    public String getDecryptedPasswordHash() { return decryptedPassword; }
    public void setDecryptedPasswordHash(String decryptedHash) { decryptedPassword = decryptedHash; }
    public void setAdvancedFeaturesFlag(boolean useAdvancedFeatures) { this.useAdvancedFeatures = useAdvancedFeatures; }
    public String getUrl() { return url; }
    public long getScrapingInterval() { return scrapingIntervalInMinutes; }
    public void setCustomPathToSavedFiles(String pathToSaveBackupFiles) { this.pathToSaveBackupFiles = pathToSaveBackupFiles; }
    public void setCalendarParser(String scrappingURL) { this.scrappingURL = scrappingURL; }
    public String getCustomPathToSavedFiles() { return pathToSaveBackupFiles; }
    public String getCalendarParser() { return scrappingURL; }
    public void setScrapingInterval(long interval) {
        propertyChange.firePropertyChange("scrapingIntervalInMinutes", scrapingIntervalInMinutes, interval);
        scrapingIntervalInMinutes = interval;
    }

    public void writeSimpleProperties() 
    {
        String path = "userFiles/userSettings/settingsTest.file";
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
            streamOut.writeLong(scrapingIntervalInMinutes);
            streamOut.writeLong(notificationTimeBeforeEntryInMinutes);
            streamOut.writeUTF(cssMode);
            streamOut.close();
            writeToFile.close();
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }

    public void readSimpleProperties() 
    {
        try 
        {
            String path = "userFiles/userSettings/settingsTest.file";
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
            scrapingIntervalInMinutes = inputStream.readLong();
            notificationTimeBeforeEntryInMinutes = inputStream.readLong();
            cssMode = inputStream.readUTF();
            inputStream.close();
            loadFile.close();
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }    
}