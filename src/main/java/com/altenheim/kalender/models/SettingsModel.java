package com.altenheim.kalender.models;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;


public class SettingsModel
{
    private String path = "hwrCalendar.ics";
    private String pathToSaveBackupFiles = null;
    private String url = null;
    private PropertyChangeSupport propertyChange = new PropertyChangeSupport(this);
    public Long scrapingIntervalInMinutes = (long) 2000;     

    public long getScrapingInterval() { return scrapingIntervalInMinutes; }
    public void setScrapingInterval(long interval)
    {
        propertyChange.firePropertyChange("scrapingIntervalInMinutes", scrapingIntervalInMinutes, interval);
        scrapingIntervalInMinutes = interval;
    }    
     
    public void addPropertyChangeListener(PropertyChangeListener listener) 
    {
        propertyChange.addPropertyChangeListener(listener);
    }

    public void setUrl(String url) {this.url = url;}
    public String getUrl() { return url; }

    public void setPath(String path) {this.path = path;}
    public String getPath() { return path; }

    public void setCustomPathToSavedFiles(String pathToSaveBackupFiles) {this.pathToSaveBackupFiles = pathToSaveBackupFiles;}
    public String getCustomPathToSavedFiles() { return pathToSaveBackupFiles; }
}
