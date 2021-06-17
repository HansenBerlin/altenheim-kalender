package com.altenheim.kalender.models;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;


public class SettingsModel implements Serializable
{
    public final static String APICYPHERTEXT = "vzAX1n7COi82yQPOqQa1gA==";
    public final static String PASSWORDHASH = "zmug2qLvMu11eKqbckKNVs+FjCcmDSK8p3feHqObn/2cwNbtPNG3y8VR5z16Po/zPFPLgjPBYB6muGS8GcBftNqU14PiRl0ho1pq2CovNXYD2TxrMAI3ZANtL375wQbJxAvQZLBXwo6jQ6AxFGe6gA==";

    private String pathToSaveBackupFiles = null;
    private PropertyChangeSupport propertyChange = new PropertyChangeSupport(this);
    private Long scrapingIntervalInMillis = (long) 2000;


    public void addPropertyChangeListener(PropertyChangeListener listener)
    {
        propertyChange.addPropertyChangeListener(listener);
    }

    public long getScrapingInterval() { return scrapingIntervalInMillis; }
    public void setScrapingInterval(long interval)
    {
        propertyChange.firePropertyChange("scrapingIntervalInMillis", scrapingIntervalInMillis, interval);
        scrapingIntervalInMillis = interval;
    }

    public void setCustomPathToSavedFiles() { }
    public String getCustomPathToSavedFiles() { return pathToSaveBackupFiles; }
}
