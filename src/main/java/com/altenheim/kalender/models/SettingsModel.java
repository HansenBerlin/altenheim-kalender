package com.altenheim.kalender.models;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.Serializable;


public class SettingsModel implements Serializable
{
    public final static String APICYPHERTEXT = "apCg/0Odtz1r9kYuh011M4sur5xv5UU0hYJQcymI9gpAfMWP1eJWOtgpXu/lawR+";
    public final static String PASSWORDHASH = "54fvHpgroWTcl6h/4SxMEiwchYBcYzqtrXX4eMySjf94gqHjPhjPCPl4d2IH7jg0";

    private String icsExportedFile = "userFiles/exportedCalendars/TestKalender.ics";
    private String hwrScrapedFile = "userFiles/crawledCalendarFiles/1415872094.ics";
    private String userDirectory = "userfiles/";
    private String decryptedPassword = "";

    private PropertyChangeSupport propertyChange = new PropertyChangeSupport(this);
    public Long scrapingIntervalInMinutes = (long) 60000;
    private String url = "https://moodle.hwr-berlin.de/fb2-stundenplan/download.php?doctype=.ics&url=./fb2-stundenplaene/wi/semester2/kursc";

    public long entrySystemMessageIntervalInMillis = (long) 60000;
    public long notificationTimeBeforeEntryInMillis = 900000;

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
    public long getEntrySystemMessageIntervalInMills() { return entrySystemMessageIntervalInMillis;}
    public void setEntrySystemMessageIntervalInMillis(long entrySystemMessageIntervalInMillis) {
        this.entrySystemMessageIntervalInMillis = entrySystemMessageIntervalInMillis;
    }
    public long getnotificationTimeBeforeEntryInMillis() { return notificationTimeBeforeEntryInMillis;}
    public void setnotificationTimeBeforeEntryInMillis(long notificationTimeBeforeEntryInMillis) {
        this.notificationTimeBeforeEntryInMillis = notificationTimeBeforeEntryInMillis;
    }
}
