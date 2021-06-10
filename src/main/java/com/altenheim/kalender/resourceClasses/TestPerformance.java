package com.altenheim.kalender.resourceClasses;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.altenheim.kalender.controller.logicController.AppointmentEntryFactory;
import com.altenheim.kalender.interfaces.ICalendarEntriesModel;
import com.altenheim.kalender.models.CalendarEntriesModel;
import com.altenheim.kalender.models.ContactModel;
import com.calendarfx.model.Calendar;

public class TestPerformance implements Serializable
{
    public TestPerformance()
    {

    }

    public List<ContactModel> createContactsList(int amount)
    {
        String testFirstName = "ThisIsAName";
        String surName = "ThisIsASurname";
        String phoneNumber = "0153-22446688";
        String mail = "mailadress@testmailadress.com";        
        var contacts = new ArrayList<ContactModel>();

        for (int i = 0; i < amount; i++) 
        {
            contacts.add(new ContactModel(testFirstName, surName, mail, phoneNumber));            
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


    public void saveContactsToFile() throws IOException
    {            
        var list = createContactsList(10000); 
        var writeToFile = new FileOutputStream("contacts.txt");
        var convert = new ObjectOutputStream(writeToFile);
        convert.writeObject(list);
        convert.close();
    }

    public void loadContactsTFromFile() throws IOException, ClassNotFoundException
    {
        var loadFile = new FileInputStream("contacts.txt");
        var inputStream = new ObjectInputStream(loadFile);
        var readObjects = (List<ContactModel>)inputStream.readObject();
        inputStream.close();
    }  

    public void saveCalendarsToFile() throws IOException
    {                   
        var list = createCalendarList(10000); 
        var writeToFile = new FileOutputStream("cals.txt");
        var convert = new ObjectOutputStream(writeToFile);
        convert.writeObject(list);
        convert.close();
    }

    public void loadCalendarsFromFile() throws IOException, ClassNotFoundException
    {
        var loadFile = new FileInputStream("cals.txt");
        var inputStream = new ObjectInputStream(loadFile);
        var readObjects = (ICalendarEntriesModel)inputStream.readObject();
        System.out.println(readObjects.getAllCalendars().size());

        inputStream.close();
    }  
}