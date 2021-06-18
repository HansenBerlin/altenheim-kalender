package com.altenheim.kalender.models;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;


public class SettingsModel implements Serializable
{
    private String icsExportedFile = "userFiles/exportedCalendars/TestKalender.ics";
    private String hwrScrapedFile = "userFiles/crawledCalendarFiles/1415872094.ics";

    private PropertyChangeSupport propertyChange = new PropertyChangeSupport(this);
    public Long scrapingIntervalInMinutes = (long) 60000;
    private String url = "https://moodle.hwr-berlin.de/fb2-stundenplan/download.php?doctype=.ics&url=./fb2-stundenplaene/wi/semester2/kursc";

    public final static String APICYPHERTEXT = "vzAX1n7COi82yQPOqQa1gA==";
    public final static String PASSWORDHASH = "zmug2qLvMu11eKqbckKNVs+FjCcmDSK8p3feHqObn/2cwNbtPNG3y8VR5z16Po/zPFPLgjPBYB6muGS8GcBftNqU14PiRl0ho1pq2CovNXYD2TxrMAI3ZANtL375wQbJxAvQZLBXwo6jQ6AxFGe6gA==";

  


    public void addPropertyChangeListener(PropertyChangeListener listener)
    {
        propertyChange.addPropertyChangeListener(listener);
    }

    public void setPathToIcsExportedFile(String path) { icsExportedFile = path; }
    public String getPathToIcsExportedFile() { return icsExportedFile; }
    public String getPathToHwrScrapedFIle() { return hwrScrapedFile; }
    public String getUrl() { return url; }
    public long getScrapingInterval() { return scrapingIntervalInMillis; }
    public void setScrapingInterval(long interval)
    {
        propertyChange.firePropertyChange("scrapingIntervalInMillis", scrapingIntervalInMillis, interval);
        scrapingIntervalInMillis = interval;
    }

}
