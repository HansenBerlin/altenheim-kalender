package com.altenheim.kalender.models;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.Serializable;
import com.altenheim.kalender.CustomSerializable.SerSimpleBoolean;
import com.altenheim.kalender.CustomSerializable.SerSimpleString;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

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
    private SerSimpleString street = new SerSimpleString();
    private SerSimpleString houseNumber = new SerSimpleString();
    private SerSimpleString zipCode = new SerSimpleString();
    private SerSimpleString city = new SerSimpleString();
    private SerSimpleString mail = new SerSimpleString();
    private SerSimpleString specialField = new SerSimpleString("Auswahl FB");
    private SerSimpleString course = new SerSimpleString("Kurs");  
    private SerSimpleString semester = new SerSimpleString("Sem.");
    private SerSimpleBoolean toolTip = new SerSimpleBoolean(false);
    private PropertyChangeSupport propertyChange = new PropertyChangeSupport(this);
    public Long scrapingIntervalInMinutes = (long) 60000;
    private String url = "https://moodle.hwr-berlin.de/fb2-stundenplan/download.php?doctype=.ics&url=./fb2-stundenplaene/wi/semester2/kursc";
    private long entrySystemMessageIntervalInMinutes = 1;
    private long notificationTimeBeforeEntryInMinutes = 15;

    private boolean useAdvancedFeatures = false; // je nachdem ob der pw hash erfolgreich geladen wird an oder aus, wegesuche und ÖZ dann ausgrauen

    public String testtest = "";

    private StringProperty testtestddd = new SimpleStringProperty();

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
    public void setStreet (String street) {this.street.set(street);}
    public void setHouseNumber (String houseNumber) {this.houseNumber.set(houseNumber);}
    public void setZipCode (String zipCode) {this.zipCode.set(zipCode);}
    public void setCity (String city) {this.city.set(city);}
    public void setMail(String mail) { this.mail.set(mail);}
    public void setSpecialField (String specialField) {this.specialField.set(specialField);}
    public void setCourse (String course) {this.course.set(course);}
    public void setSemester (String semester) {this.semester.set(semester);}
    public void setToolTip(boolean value) {this.toolTip.set(value);}
    public String getCustomPathToSavedFiles() {return pathToSaveBackupFiles;}
    public String getCalendarParser() {return scrappingURL;}    
    public SerSimpleString getStreet() {return street;}    
    public SerSimpleString getHouseNumber() {return houseNumber;}    
    public SerSimpleString getZipCOde() {return zipCode;}
    public SerSimpleString getCity() {return city;}    
    public SerSimpleString getMail() {return mail;}  
    public SerSimpleString getSpecialField() {return specialField;}
    public SerSimpleString getCourse() {return course;}
    public SerSimpleString getSemester() {return semester;}
    public SerSimpleBoolean getToolTip() {return toolTip;}
    public long getEntrySystemMessageIntervalInMinutes() { return entrySystemMessageIntervalInMinutes;}
    public long getnotificationTimeBeforeEntryInMinutes() { return notificationTimeBeforeEntryInMinutes;}
    public void setnotificationTimeBeforeEntryInMinutes(long notificationTimeBeforeEntryInMinutes) { this.notificationTimeBeforeEntryInMinutes = notificationTimeBeforeEntryInMinutes; }
    public void setScrapingInterval(long interval)
    {
        propertyChange.firePropertyChange("scrapingIntervalInMillis", scrapingIntervalInMinutes, interval);
        scrapingIntervalInMinutes = interval;
    }
    public void addPropertyChangeListener(PropertyChangeListener listener)
    {
        propertyChange.addPropertyChangeListener(listener);
    }
}