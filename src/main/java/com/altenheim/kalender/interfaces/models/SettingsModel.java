package com.altenheim.kalender.interfaces.models;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import jfxtras.styles.jmetro.Style;
import java.io.File;
import java.io.Serializable;

public interface SettingsModel extends Serializable
{
    SimpleStringProperty[] getSettingsInputFieldsContainer();
    SimpleStringProperty[] getSettingsDropdownTitlesContainer();
    String getSelectedHwrCourseName();
    File getPasswordhashFile();
    Style getCssStyle();
    long getNotificationTimeBeforeEntryInMinutes();
    long getHwrRequestIntervalInMinutes();
    long getEntrySystemMessageIntervalInMinutes();
    void setSelectedHwrCourseName(String value);
    void setNotificationTimeBeforeEntryInMinutes(long value);
    void setDefaultCalendarForSearchView(String value);
    void setEntrySystemMessageIntervalInMinutes(int value);
    void setHwrWebsiteUrl(String value);
    void loadSettings();
    void saveSettings();
}
