package com.altenheim.kalender.interfaces.models;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import jfxtras.styles.jmetro.Style;
import java.io.File;
import java.io.Serializable;

public interface SettingsModel extends Serializable
{
    SimpleStringProperty street = new SimpleStringProperty();
    SimpleStringProperty houseNumber = new SimpleStringProperty();
    SimpleStringProperty zipCode = new SimpleStringProperty();
    SimpleStringProperty city = new SimpleStringProperty();
    SimpleStringProperty mail = new SimpleStringProperty();
    SimpleStringProperty specialField = new SimpleStringProperty("Auswahl FB");
    SimpleStringProperty course = new SimpleStringProperty("Kurs");
    SimpleStringProperty semester = new SimpleStringProperty("Sem.");
    SimpleBooleanProperty toolTip = new SimpleBooleanProperty(false);
    SimpleStringProperty[] getSettingsInputFieldsContainer();
    SimpleBooleanProperty getToolTipEnabled();
    long getNotificationTimeBeforeEntryInMinutes();

    //SimpleStringProperty[] getSettingsDropdownTitleCOntainer();
    void setNotificationTimeBeforeEntryInMinutes(long value);
    void setDefaultCalendarForSearchView(String value);
    void setEntrySystemMessageIntervalInMinutes(int value);
    long getEntrySystemMessageIntervalInMinutes();
    void setHwrWebsiteUrl(String value);
    long getHwrRequestIntervalInMinutes();
    String getPathToUserDirectory();
    File getPasswordhashFile();
    Style getCssStyle();
    void loadSettings();
    void saveSettings();
}
