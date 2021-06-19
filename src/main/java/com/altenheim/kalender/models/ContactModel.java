package com.altenheim.kalender.models;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;

public class ContactModel implements Serializable
{
    private String firstName;
    private String surName;
    private String streetAndNumber;
    private String city;
    private int postalCode;
    private String mail;
    private String phoneNumber;
    private int mailTemplateId;
    private int iD;
    private Map<DayOfWeek, List<SerializableEntry>> openingHours;

    public ContactModel(String firstName, String surName, String mail, int iD, String streetAndNumber, 
        String city, int postalCode, String phoneNumber, int mailTemplateId, Map<DayOfWeek, List<SerializableEntry>> openingHours)
    {
        this.firstName = firstName;
        this.surName = surName;
        this.streetAndNumber = streetAndNumber;
        this.city = city;
        this.postalCode = postalCode;
        this.mail = mail;
        this.phoneNumber = phoneNumber;
        this.mailTemplateId = mailTemplateId;
        this.openingHours = openingHours;
        this.iD = iD;
    }
}
