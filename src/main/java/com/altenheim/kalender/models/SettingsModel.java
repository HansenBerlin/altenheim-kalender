package com.altenheim.kalender.models;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javafx.beans.property.SimpleStringProperty;

public class SettingsModel
{
    private String pathToSaveBackupFiles = null;
    private PropertyChangeSupport propertyChange = new PropertyChangeSupport(this);
    public Long scrapingIntervalInMillis = (long) 2000;
    private String scrappingURL; 
    private SimpleStringProperty street = new SimpleStringProperty();
    private SimpleStringProperty houseNumber = new SimpleStringProperty();
    private SimpleStringProperty zipCode = new SimpleStringProperty();
    private SimpleStringProperty city = new SimpleStringProperty();
    private SimpleStringProperty mail = new SimpleStringProperty();  

    public long getScrapingInterval() {return scrapingIntervalInMillis;}
    public void setScrapingInterval(long interval)
    {
        propertyChange.firePropertyChange("scrapingIntervalInMinutes", scrapingIntervalInMillis, interval);
        scrapingIntervalInMillis = interval;
    }    
     
    public void addPropertyChangeListener(PropertyChangeListener listener) 
    {
        propertyChange.addPropertyChangeListener(listener);
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
}
