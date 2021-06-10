package com.altenheim.kalender.TempTestClasses;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.altenheim.kalender.controller.logicController.AppointmentEntryFactory;
import com.altenheim.kalender.interfaces.ICalendarEntriesModel;
import com.altenheim.kalender.models.CalendarEntriesModel;
import com.altenheim.kalender.models.ContactModel;
import com.altenheim.kalender.models.EntrySer;


public class CreateDummyEntries 
{
    private int iDCounter;

    public List<ContactModel> createContactsList(int amount)
    {
        String testFirstName = "ThisIsAName";
        String surName = "ThisIsASurname";
        String phoneNumber = "0153-22446688";
        String mail = "mailadress@testmailadress.com";        
        var contacts = new ArrayList<ContactModel>();

        for (int i = 0; i < amount; i++)
        {
            contacts.add(new ContactModel(testFirstName + i, surName + i, mail, iDCounter, phoneNumber + i, createOpeningHours()));       
            iDCounter++;
        }      

        return contacts;
    }


    public ICalendarEntriesModel createCalendarList(int amount)
    {
        var calCreator = new AppointmentEntryFactory();
        for (int i = 0; i < amount; i++)        
            calCreator.createRandomEntrys("Test " + i);
        
        return calCreator.getEntriesModel();
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