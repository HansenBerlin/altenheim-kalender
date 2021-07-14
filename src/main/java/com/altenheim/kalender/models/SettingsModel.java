package com.altenheim.kalender.models;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.Serializable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.LongPropertyBase;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableFloatValue;
import javafx.beans.value.ObservableLongValue;
import javafx.beans.value.ObservableNumberValue;


public class SettingsModel implements Serializable
{
    public final static String APICYPHERTEXT = "apCg/0Odtz1r9kYuh011M4sur5xv5UU0hYJQcymI9gpAfMWP1eJWOtgpXu/lawR+";
    public final static String PASSWORDHASH = "54fvHpgroWTcl6h/4SxMEiwchYBcYzqtrXX4eMySjf94gqHjPhjPCPl4d2IH7jg0";

    private String icsExportedFile = "userFiles/exportedCalendars/TestKalender.ics";
    private String hwrScrapedFile = "userFiles/crawledCalendarFiles/1415872094.ics";
    private String userDirectory = "userfiles/";
    private String decryptedPassword = "";
    private String pathToSaveBackupFiles = null;
    public Long scrapingIntervalInMillis = (long) 2000;
    private String scrappingURL; 
    private SimpleStringProperty street = new SimpleStringProperty();
    private SimpleStringProperty houseNumber = new SimpleStringProperty();
    private SimpleStringProperty zipCode = new SimpleStringProperty();
    private SimpleStringProperty city = new SimpleStringProperty();
    private SimpleStringProperty mail = new SimpleStringProperty();
    private BooleanProperty toolTip = new SimpleBooleanProperty(false);

    private PropertyChangeSupport propertyChange = new PropertyChangeSupport(this);
    public Long scrapingIntervalInMinutes = (long) 60000;
    private String url = "https://moodle.hwr-berlin.de/fb2-stundenplan/download.php?doctype=.ics&url=./fb2-stundenplaene/wi/semester2/kursc";

    private long entrySystemMessageIntervalInMinutes = 1;
    private SimpleStringProperty notificationTimeBeforeEntryInMinutes = new SimpleStringProperty("15");

    private SimpleLongProperty testLongPorperty = new SimpleLongProperty();

    private boolean useAdvancedFeatures = false; // je nachdem ob der pw hash erfolgreich geladen wird an oder aus, wegesuche und ÖZ dann ausgrauen

    public void addPropertyChangeListener(PropertyChangeListener listener)
    {
        propertyChange.addPropertyChangeListener(listener);
    }



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
    public void setScrapingInterval(long interval)
    {
        propertyChange.firePropertyChange("scrapingIntervalInMillis", scrapingIntervalInMinutes, interval);
        scrapingIntervalInMinutes = interval;
    }


    public void setCustomPathToSavedFiles(String pathToSaveBackupFiles) {this.pathToSaveBackupFiles=pathToSaveBackupFiles;}
    public String getCustomPathToSavedFiles() {return pathToSaveBackupFiles;}

    public void setCalendarParser(String scrappingURL) {this.scrappingURL = scrappingURL;}
    public String getCalendarParser() {return scrappingURL;}
    
    public void setStreet (String street) {this.street.set(street);}
    public SimpleStringProperty getStreet() {return street;}
    
    public void setHouseNumber (String houseNumber) {this.houseNumber.set(houseNumber);}
    public SimpleStringProperty getHouseNumber() {return houseNumber;}
    
    public void setZipCode (String zipCode) {this.zipCode.set(zipCode);}
    public SimpleStringProperty getZipCOde() {return zipCode;}

    public void setCity (String city) {this.city.set(city);}
    public SimpleStringProperty getCity() {return city;}
    
    public void setMail(String mail) { this.mail.set(mail);}
    public SimpleStringProperty getMail() {return mail;}

    public void setToolTip(BooleanProperty toolTip) {this.toolTip = toolTip;}
    public BooleanProperty getToolTip() {return toolTip;}

    public long getEntrySystemMessageIntervalInMinutes() {return entrySystemMessageIntervalInMinutes;}

    public void setnotificationTimeBeforeEntryInMinutes(SimpleStringProperty notificationTimeBeforeEntryInMinutes) {
        this.notificationTimeBeforeEntryInMinutes = notificationTimeBeforeEntryInMinutes;}  
    public long getnotificationTimeBeforeEntryInMinutes() {return Long.parseLong(notificationTimeBeforeEntryInMinutes.getValue());}
    public SimpleStringProperty getnotificationTimeBeforeEntryInMinutes2() {return notificationTimeBeforeEntryInMinutes;}

}
