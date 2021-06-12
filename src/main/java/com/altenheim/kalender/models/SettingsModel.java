package com.altenheim.kalender.models;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import com.altenheim.kalender.controller.logicController.IOController;

public class SettingsModel extends IOController
{
    private PropertyChangeSupport propertyChange = new PropertyChangeSupport(this);
    public Long scrapingIntervalInMinutes = (long) 20000;    

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
}
