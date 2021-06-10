package com.altenheim.kalender.models;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactModel implements Serializable
{
    private String firstName;
    private String surName;
    private String mail;
    private String phoneNumber;
    private int mailTemplateId;
    private int iD;
    private Map<DayOfWeek, List<EntrySer<String>>> openingHours;

    public ContactModel(String firstName, String surName, String mail, int iD, 
        String phoneNumber, Map<DayOfWeek, List<EntrySer<String>>> openingHours)
    {
        this.firstName = firstName;
        this.surName = surName;
        this.mail = mail;
        this.phoneNumber = phoneNumber;
        this.openingHours = openingHours;
        this.iD = iD;
    }

    public ContactModel()
    {
    }

    
}