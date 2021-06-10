package com.altenheim.kalender.TempTestClasses;

import java.util.ArrayList;
import java.util.List;
import com.altenheim.kalender.controller.logicController.AppointmentEntryFactory;
import com.altenheim.kalender.interfaces.ICalendarEntriesModel;
import com.altenheim.kalender.models.ContactModel;

public class CreateDummyEntries 
{
    public List<ContactModel> createContactsList(int amount)
    {
        String testFirstName = "ThisIsAName";
        String surName = "ThisIsASurname";
        String phoneNumber = "0153-22446688";
        String mail = "mailadress@testmailadress.com";        
        var contacts = new ArrayList<ContactModel>();

        for (int i = 0; i < amount; i++) 
        {
            contacts.add(new ContactModel(testFirstName + i, surName + i, mail, phoneNumber + i));            
        }

        return contacts;
    }


    public ICalendarEntriesModel createCalendarList(int amount)
    {
        var calCreator = new AppointmentEntryFactory();

        for (int i = 0; i < amount; i++) 
        {
            calCreator.createRandomEntrys("Test " + i);
        }
        System.out.println(AppointmentEntryFactory.entries);

        return calCreator.getEntriesModel();
    }    
}
