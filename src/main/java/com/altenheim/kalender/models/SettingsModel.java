package com.altenheim.kalender.models;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;


public class SettingsModel implements Serializable
{
    public final static String APICYPHERTEXT = "apCg/0Odtz1r9kYuh011M4sur5xv5UU0hYJQcymI9gpAfMWP1eJWOtgpXu/lawR+";
    public final static String PASSWORDHASH = "54fvHpgroWTcl6h/4SxMEiwchYBcYzqtrXX4eMySjf94gqHjPhjPCPl4d2IH7jg0";

    private String icsExportedFile = "userFiles/exportedCalendars/TestKalender.ics";
    private String hwrScrapedFile = "userFiles/crawledCalendarFiles/1415872094.ics";
    private String userDirectory = "userfiles/";

    private PropertyChangeSupport propertyChange = new PropertyChangeSupport(this);
    public Long scrapingIntervalInMinutes = (long) 60000;
    private String url = "https://moodle.hwr-berlin.de/fb2-stundenplan/download.php?doctype=.ics&url=./fb2-stundenplaene/wi/semester2/kursc";

    public void addPropertyChangeListener(PropertyChangeListener listener)
    {
        propertyChange.addPropertyChangeListener(listener);
    }

    public void setPathToIcsExportedFile(String path) { icsExportedFile = path; }
    public String getPathToIcsExportedFile() { return icsExportedFile; }
    public String getPathToHwrScrapedFIle() { return hwrScrapedFile; }
    public String getPathToUserDirectory() { return userDirectory; }

    public String getUrl() { return url; }
    public long getScrapingInterval() { return scrapingIntervalInMinutes; }
    public void setScrapingInterval(long interval)
    {
        propertyChange.firePropertyChange("scrapingIntervalInMillis", scrapingIntervalInMinutes, interval);
        scrapingIntervalInMinutes = interval;
    }

}
