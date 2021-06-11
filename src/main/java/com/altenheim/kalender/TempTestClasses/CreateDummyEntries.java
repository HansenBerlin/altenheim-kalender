package com.altenheim.kalender.TempTestClasses;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.altenheim.kalender.models.ContactModel;
import com.altenheim.kalender.models.SerializableEntry;


public class CreateDummyEntries implements ICreateDummyEntries
{
    private int iDCounter;
    private List<ContactModel> contacts;

    public CreateDummyEntries(List<ContactModel> contacts)
    {
        this.contacts = contacts;
    }

    public void createContactsList(int amount)
    {
        var contacts = new ArrayList<ContactModel>();
        for (int i = 0; i < amount; i++)
        {
            String testFirstName = "ThisIsAName" + i;
            String surName = "ThisIsASurname" + i;
            String phoneNumber = "0153-22446688" + i;
            String mail = "mailadress@testmailadress.com" + i;        
            contacts.add(new ContactModel(testFirstName, surName, mail, iDCounter, phoneNumber, createOpeningHours()));       
            iDCounter++;
        }      

        this.contacts.addAll(contacts);
    }

    
    private Map<DayOfWeek, List<SerializableEntry>> createOpeningHours()
    {
        var openingHours = new HashMap<DayOfWeek, List<SerializableEntry>>();
        var startTime = LocalTime.of(8, 0);
        var endTimeAlt = LocalTime.of(12, 0);
        var startTimeAlt = LocalTime.of(14, 0);
        var endTime = LocalTime.of(20, 0);

        for (var day : DayOfWeek.values()) 
        {
            var entrys = new ArrayList<SerializableEntry>();
            if (day.getValue() %2 == 0)
            {
                var entryOne = new SerializableEntry();
                var entryTwo = new SerializableEntry();
                entryOne.changeStartTime(startTime);
                entryOne.changeEndTime(endTimeAlt);
                entryTwo.changeStartTime(startTimeAlt);
                entryTwo.changeEndTime(endTime);
                entrys.add(entryOne);
                entrys.add(entryTwo);
            }
            else
            {
                var entryOne = new SerializableEntry();
                entryOne.changeStartTime(startTime);
                entryOne.changeEndTime(endTime);               
                entrys.add(entryOne);
            }
            openingHours.put(day, entrys);            
        }
        return openingHours;        
    }    
}