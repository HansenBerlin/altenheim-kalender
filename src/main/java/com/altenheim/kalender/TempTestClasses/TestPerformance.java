package com.altenheim.kalender.TempTestClasses;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;
import com.altenheim.kalender.interfaces.ICalendarEntriesModel;
import com.altenheim.kalender.models.ContactModel;

public class TestPerformance implements Serializable
{
   
    public void saveContactsToFile() throws IOException
    {           
        var dummys = new CreateDummyEntries();
        var list = dummys.createContactsList(10000); 
        var start = System.currentTimeMillis();
        var writeToFile = new FileOutputStream("contacts.txt");
        var output = new ObjectOutputStream(writeToFile);
        output.writeObject(list);
        output.close();
        System.out.printf("Saving %d contacts time passed: %d\n", 10000, (System.currentTimeMillis() - start));
    }

    public void loadContactsTFromFile() throws IOException, ClassNotFoundException
    {
        var start = System.currentTimeMillis();
        var loadFile = new FileInputStream("contacts.txt");
        var inputStream = new ObjectInputStream(loadFile);
        var readObjects = (List<ContactModel>)inputStream.readObject();
        inputStream.close();
        System.out.printf("Loading %d contacts time passed: %d\n", 10000, (System.currentTimeMillis() - start));
    }  

    public void saveCalendarsToFile() throws IOException
    {    
        var dummys = new CreateDummyEntries();               
        var list = dummys.createCalendarList(10000); 
        var start = System.currentTimeMillis();
        var writeToFile = new FileOutputStream("cals.txt");
        var output = new ObjectOutputStream(writeToFile);
        output.writeObject(list);
        output.close();
        System.out.printf("Saving %d calendars time passed: %d\n", 10000, (System.currentTimeMillis() - start));
    }

    public void loadCalendarsFromFile() throws IOException, ClassNotFoundException
    {
        var start = System.currentTimeMillis();
        var loadFile = new FileInputStream("cals.txt");
        var inputStream = new ObjectInputStream(loadFile);
        var readObjects = (ICalendarEntriesModel)inputStream.readObject();
        System.out.println(readObjects.getAllCalendars().size());
        inputStream.close();
        System.out.printf("Loading %d calendars time passed: %d\n", 10000, (System.currentTimeMillis() - start));
    }  
}