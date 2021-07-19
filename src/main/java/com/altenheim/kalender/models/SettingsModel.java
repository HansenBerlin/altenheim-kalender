package com.altenheim.kalender.models;

import java.beans.PropertyChangeListener;
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

    private String icsExportedFile = "userFiles/exportedCalendars/TestKalender.ics";
    private String hwrScrapedFile = "userFiles/crawledCalendarFiles/1415872094.ics";
    private String userDirectory = "userfiles/";
    private String decryptedPassword = "";
    private String pathToSaveBackupFiles = null;

    private String scrappingURL = "";

    public SimpleStringProperty street = new SimpleStringProperty();
    public SimpleStringProperty houseNumber = new SimpleStringProperty();
    public SimpleStringProperty zipCode = new SimpleStringProperty();
    public SimpleStringProperty city = new SimpleStringProperty();
    public SimpleStringProperty mail = new SimpleStringProperty();
    public SimpleStringProperty specialField = new SimpleStringProperty("Auswahl FB");
    public SimpleStringProperty course = new SimpleStringProperty("Kurs");  
    public SimpleStringProperty semester = new SimpleStringProperty("Sem.");
    public SimpleBooleanProperty toolTip = new SimpleBooleanProperty(false);
    public PropertyChangeSupport propertyChange = new PropertyChangeSupport(this);    
    public String url = "https://moodle.hwr-berlin.de/fb2-stundenplan/download.php?doctype=.ics&url=./fb2-stundenplaene/wi/semester2/kursc";
    public long entrySystemMessageIntervalInMinutes = 1;
    public long notificationTimeBeforeEntryInMinutes = 15;
    private Long scrapingIntervalInMinutes = (long) 60000;
    private boolean useAdvancedFeatures = false; // je nachdem ob der pw hash erfolgreich geladen wird an oder aus, wegesuche und ÖZ dann ausgrauen
    public String cssMode = "Light" ;

    private SimpleStringProperty[] settingsInputFieldsContainer = { street, houseNumber, zipCode, city, mail };
    private SimpleStringProperty[] settingsDropdownTitlesContainer = { specialField, course, semester };
    public SimpleStringProperty[] getSettingsInputFieldsContainer() { return settingsInputFieldsContainer; }
    public SimpleStringProperty[] getSettingsDropdownTitleCOntainer() { return settingsDropdownTitlesContainer; }

    public void setPathToIcsExportedFile(String path) { icsExportedFile = path; }
    public String getPathToIcsExportedFile() { return icsExportedFile; }
    public String getPathToHwrScrapedFile() { return hwrScrapedFile; }
    public String getPathToUserDirectory() { return userDirectory; }
    public File getPasswordhashFile() { return new File(userDirectory + "savedHash"); }
    public String getDecryptedPasswordHash() { return decryptedPassword; }
    public void setDecryptedPasswordHash(String decryptedHash) { decryptedPassword = decryptedHash; }
    public void setAdvancedFeaturesFlag(boolean useAdvancedFeatures) { this.useAdvancedFeatures = useAdvancedFeatures; }
    public String getUrl() { return url; }
    public long getScrapingInterval() { return scrapingIntervalInMinutes; }    
    public void setCustomPathToSavedFiles(String pathToSaveBackupFiles) {this.pathToSaveBackupFiles=pathToSaveBackupFiles;}
    public void setCalendarParser(String scrappingURL) {this.scrappingURL = scrappingURL;}    
    public String getCustomPathToSavedFiles() {return pathToSaveBackupFiles;}
    public String getCalendarParser() {return scrappingURL;}    
    
    public void setScrapingInterval(long interval)
    {
        propertyChange.firePropertyChange("scrapingIntervalInMinutes", scrapingIntervalInMinutes, interval);
        scrapingIntervalInMinutes = interval;
    }
    public void addPropertyChangeListener(PropertyChangeListener listener)
    {
        propertyChange.addPropertyChangeListener(listener);
    }

    public void writeSimpleProperties()
    {        
        String path = "userFiles/settingsTest.file";        
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
            streamOut.close();
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
            String path = "userFiles/settingsTest.file";
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
            inputStream.close();
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    } 
}
