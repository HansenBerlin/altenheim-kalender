package com.altenheim.kalender.models;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;


public class SettingsModel
{
    private String pathToSaveBackupFiles = null;
    private PropertyChangeSupport propertyChange = new PropertyChangeSupport(this);
    public Long scrapingIntervalInMinutes = (long) 2000; 
    private String scrappingURL = "HWR.hallo.com"; 
    private String street = "Straße";
    private String houseNumber = "Hausnr.";
    private String zipCode = "Plz";
    private String city = "Stadt";
    private String mail = "E-Mail Adresse";  


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

    public void setCustomPathToSavedFiles() { }
    public String getCustomPathToSavedFiles() { return pathToSaveBackupFiles; }

    
    public void setCalendarParser(String scrappingURL) { this.scrappingURL = scrappingURL; }
    public String getCalendarParser() { return scrappingURL; }
    
    public void setStreet (String street) {this.street = street;    }
    public String getStreet() {return street;}
    
    public void setHouseNumber (String houseNumber) {this.houseNumber=houseNumber;}
    public String getHouseNumber () {return houseNumber;}
    
    public void setZipCode (String zipCode) {this.zipCode=zipCode;}
    public String getZipCOde () {return zipCode;}

    public void setCity (String city) {this.city=city;}
    public String getCity (){return city;}
    
    public void setMail(String mail) { this.mail = mail; }
    public String getMail() { return mail; }

}



