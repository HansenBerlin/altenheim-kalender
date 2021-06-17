package com.altenheim.kalender.models;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;


public class SettingsModel
{
    private String pathToSaveBackupFiles = null;
    private String urlvariables = null;
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

    public void seturlvariables(String url) {this.urlvariables = url;}
    public String geturlvariables() { return urlvariables; }

    public void setCustomPathToSavedFiles() { }
    public String getCustomPathToSavedFiles() { return pathToSaveBackupFiles; }
}
