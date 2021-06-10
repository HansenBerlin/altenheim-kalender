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
    private static int iDcount = 111111;
    private String firstName;
    private String surName;
    private String mail;
    private String phoneNumber;
    private int mailTemplateId;
    private int iD;
    private Map<DayOfWeek, List<EntrySer<String>>> openingHours;

    public ContactModel(String firstName, String surName, String mail, String phoneNumber)
    {
        this.firstName = firstName;
        this.surName = surName;
        this.mail = mail;
        this.phoneNumber = phoneNumber;
        openingHours = createOpeningHours();
        iD = iDcount;
        iDcount++;
    }

    public ContactModel()
    {
    }

    public Map<DayOfWeek, List<EntrySer<String>>> createOpeningHours()
    {
        var openingHours = new HashMap<DayOfWeek, List<EntrySer<String>>>();
        var startTime = LocalTime.of(8, 0);
        var endTimeAlt = LocalTime.of(12, 0);
        var startTimeAlt = LocalTime.of(14, 0);
        var endTime = LocalTime.of(20, 0);

        for (var day : DayOfWeek.values()) 
        {
            var entrys = new ArrayList<EntrySer<String>>();
            if (day.getValue() %2 == 0)
            {
                var entryOne = new EntrySer<String>();
                var entryTwo = new EntrySer<String>();
                entryOne.changeStartTime(startTime);
                entryOne.changeEndTime(endTimeAlt);
                entryTwo.changeStartTime(startTimeAlt);
                entryTwo.changeEndTime(endTime);
                entrys.add(entryOne);
                entrys.add(entryTwo);
            }
            else
            {
                var entryOne = new EntrySer<String>();
                entryOne.changeStartTime(startTime);
                entryOne.changeEndTime(endTime);               
                entrys.add(entryOne);
            }
            openingHours.put(day, entrys);            
        }
        return openingHours;        
    }    
}
