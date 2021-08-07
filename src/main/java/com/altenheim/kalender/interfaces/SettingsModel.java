package com.altenheim.kalender.interfaces;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import jfxtras.styles.jmetro.Style;
import java.io.File;
import java.io.Serializable;

public interface SettingsModel extends Serializable
{
    /*
    String APICYPHERTEXT = "apCg/0Odtz1r9kYuh011M4sur5xv5UU0hYJQcymI9gpAfMWP1eJWOtgpXu/lawR+";
    String PASSWORDHASH = "54fvHpgroWTcl6h/4SxMEiwchYBcYzqtrXX4eMySjf94gqHjPhjPCPl4d2IH7jg0";
    String defaultCalendarForSearchView = "";
    String hwrWebsiteUrl = "https://moodle.hwr-berlin.de/fb2-stundenplan/download.php?doctype=.ics&url=./fb2-stundenplaene/wi/semester2/kursc";
    long entrySystemMessageIntervalInMinutes = 1;
    long notificationTimeBeforeEntryInMinutes = 15;
    long hwrRequestIntervalInMinutes = 1440;*/
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
