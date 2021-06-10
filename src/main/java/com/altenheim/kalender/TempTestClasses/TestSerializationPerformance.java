package com.altenheim.kalender.TempTestClasses;

import com.altenheim.kalender.controller.logicController.AppointmentEntryFactory;
import com.altenheim.kalender.interfaces.ICalendarEntriesModel;
import com.altenheim.kalender.models.ContactModel;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

public class TestSerializationPerformance implements Serializable
{
   
    public void createAndSaveContactsToFile(int amountContacts) throws IOException
    {           
        var dummys = new CreateDummyEntries();
        var list = dummys.createContactsList(amountContacts);
        checkContactEntries(list);
        var start = System.currentTimeMillis();
        var writeToFile = new FileOutputStream("contacts.txt");
        var output = new ObjectOutputStream(writeToFile);
        output.writeObject(list);
        output.close();
        writeToFile.close();
        System.out.printf("Saving %d contacts time passed: %d\n", amountContacts, (System.currentTimeMillis() - start));
        System.out.println("Contacts File Size in kB: " + new File("contacts.txt").length()/1000);
    }

    public void loadContactsTFromFile() throws IOException, ClassNotFoundException
    {
        var start = System.currentTimeMillis();
        var loadFile = new FileInputStream("contacts.txt");
        var inputStream = new ObjectInputStream(loadFile);
        var readObjects = (List<ContactModel>)inputStream.readObject();
        inputStream.close();
        System.out.println("Loading contacts time passed: " + (System.currentTimeMillis() - start));
        checkContactEntries(readObjects);
        var file = new File("contacts.txt");
        file.delete();
    }  

    public void createAndSaveCalendarsToFile(int amountCalendars) throws IOException
    {    
        var dummys = new CreateDummyEntries();               
        var list = dummys.createCalendarList(amountCalendars);
        checkCalendarEntries(list);
        System.out.printf("Created %d calendar entries.\n", AppointmentEntryFactory.entries);
        var start = System.currentTimeMillis();
        var writeToFile = new FileOutputStream("cals.txt");
        var output = new ObjectOutputStream(writeToFile);
        output.writeObject(list);
        output.close();
        writeToFile.close();
        System.out.printf("Saving %d calendars time passed: %d\n", amountCalendars, (System.currentTimeMillis() - start));
        System.out.println("Calendar File Size in kB: " + new File("cals.txt").length()/1000);

    }

    public void loadCalendarsFromFile() throws IOException, ClassNotFoundException
    {
        var start = System.currentTimeMillis();
        var loadFile = new FileInputStream("cals.txt");
        var inputStream = new ObjectInputStream(loadFile);
        var readObjects = (ICalendarEntriesModel)inputStream.readObject();
        System.out.println("Loading calendars time passed:" + (System.currentTimeMillis() - start));
        checkCalendarEntries(readObjects);
        inputStream.close();
        var file = new File("cals.txt");
        file.delete();
    }

    private void checkCalendarEntries(ICalendarEntriesModel allEntries)
    {
        long entriesCount = 0;
        var result = allEntries.getSpecificCalendarByIndex(0).findEntries(LocalDate.of(2021, 1, 1), 
            LocalDate.of(2021, 12, 31), ZoneId.systemDefault()).values();

        for (var entries : result) 		
		{		
			for (int i = 0; i <= entries.size(); i++) 
			{		
                entriesCount++;		
			}		
		}		
        System.out.println("Checked calendar entries: " + entriesCount);
    }

    private void checkContactEntries(List<ContactModel> allEntries)
    {
        long entriesCount = 0;
        for (var entry : allEntries) 			
            entriesCount++;			
        System.out.println("Checked contact entries: " + entriesCount);
    }
}