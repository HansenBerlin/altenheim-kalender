package com.altenheim.kalender.controller.logicController;

import java.io.File;

import com.altenheim.kalender.interfaces.ICalendarEntriesModel;
import com.altenheim.kalender.interfaces.IEntryFactory;
import com.altenheim.kalender.interfaces.IIOController;
import com.altenheim.kalender.models.CalendarEntriesModel;
import com.altenheim.kalender.models.ContactModel;
import com.altenheim.kalender.models.MailTemplateModel;
import com.altenheim.kalender.models.SettingsModel;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import net.fortuna.ical4j.data.*;
import net.fortuna.ical4j.model.property.*;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.util.RandomUidGenerator;
import net.fortuna.ical4j.validate.ValidationException;

public class IOController implements IIOController
{
    private ICalendarEntriesModel allEntries;
    private List<ContactModel> allContacts;
    private SettingsModel settings;
    private List<MailTemplateModel> mailTemplates;

    public IOController(IEntryFactory administrateEntries, List<ContactModel> allContacts, 
        SettingsModel settings, List<MailTemplateModel> mailTemplates, ICalendarEntriesModel allEntries)
    {
        this.allEntries = allEntries;
        this.allContacts = allContacts;
        this.settings = settings;
        this.mailTemplates = mailTemplates;
    }
    

    public void writeCalendarFiles() throws ValidationException, IOException
    {
        var allCalendars = allEntries.getAllCalendars();
        for (var calendarSet : allCalendars) 
        {
            var icsCalendar = new Calendar();
            icsCalendar.getProperties().add(new ProdId("-//Smart Planner//iCal4j 1.0//DE"));
            icsCalendar.getProperties().add(Version.VERSION_2_0);
            icsCalendar.getProperties().add(CalScale.GREGORIAN);
            //icsCalendar.getProperties().add(new Name(calendarSet.getKey()));
            /*for (var entry : calendarSet.clear();)        
                icsCalendar.getComponents().add(createIcalEntryFromCalFXEntry(entry));        
            var fout = new FileOutputStream("icsFiles/" + calendarSet.getKey() + ".ics");
            var outputter = new CalendarOutputter();
            outputter.output(icsCalendar, fout);
            fout.close();     */     
        }        
    }


    public void loadCalendarsFromFile() throws IOException, ParserException
    {
        var folder = new File("icsFiles");
        var files = folder.listFiles();
        var calendarSource = new CalendarSource("Saved Calendars");
        var calendars = new ArrayList<com.calendarfx.model.Calendar>();

        for (var file : files) 
        {
            if (file.getName().contains(".ics"))
            {
                var stream = new FileInputStream(file);
                var builder = new CalendarBuilder();
                var fxcalendar = builder.build(stream);
                var components = fxcalendar.getComponents();
                var calNameProp = (Property) fxcalendar.getProperties().getProperty("X-WR-CALNAME");
                var calendar = new com.calendarfx.model.Calendar();
                if(calNameProp != null)
                    calendar.setName(calNameProp.getValue());
                    
                for (int i = 1; i < components.size(); i++)         
                {
                    var start = (DtStart)((Property) components.get(i).getProperties().getProperty("DTSTART"));
                    var end = (DtEnd)((Property) components.get(i).getProperties().getProperty("DTEND"));
                    var startMilli = start.getDate().toInstant().toEpochMilli();
                    var endMilli = end.getDate().toInstant().toEpochMilli();            
                    var entry = createCalendarFXEntryFromMillis(startMilli, endMilli);
                    var summary = ((Property) components.get(i).getProperties().getProperty("SUMMARY")).getValue();
                    entry.setTitle(summary);
                    var locationProp = (Property) components.get(i).getProperties().getProperty("LOCATION");
                    if(locationProp != null)
                        entry.setLocation(locationProp.getValue());
                    calendar.addEntry(entry);
                }
                stream.close();
                calendars.add(calendar);
            }            
        }

        calendarSource.getCalendars().addAll(calendars);
        //calendarView.getCalendarSources().addAll(calendarSource);        
    }


    public void saveContactsToFile() throws IOException
    {         
        var path = settings.getCustomPathToSavedFiles();
        if (path == null)
            path = "contactFiles/contacts.file";  
        var writeToFile = new FileOutputStream(path);
        var convert = new ObjectOutputStream(writeToFile);
        convert.writeObject(allContacts);
        convert.close();
    }


    public void loadContactsFromFile() throws IOException, ClassNotFoundException
    {
        var path = settings.getCustomPathToSavedFiles();
        if (path == null)
            path = "contactFiles/contacts.file"; 
        var loadFile = new FileInputStream(path);
        var inputStream = new ObjectInputStream(loadFile);
        var loadedContacts = (List<ContactModel>)inputStream.readObject();
        allContacts.addAll(loadedContacts);
        inputStream.close();
    }  
    

    public void writeSettings(SettingsModel settings)
    {
    }


    public SettingsModel restoreSettings()
    {
        return null;
    }


    public void writeMailTemplates(MailTemplateModel templates)
    {
    }


    public MailTemplateModel restoreMailTemplates()
    {
        return null;
    }  


    private Entry<String> createCalendarFXEntryFromMillis(long start, long end)
	{
		var entry = new Entry<String>();
		var dateStart = LocalDateTime.ofInstant(Instant.ofEpochMilli(start), ZoneId.systemDefault());
		var dateEnd = LocalDateTime.ofInstant(Instant.ofEpochMilli(end), ZoneId.systemDefault());		
		entry.changeStartTime(dateStart.toLocalTime());
		entry.changeStartDate(dateStart.toLocalDate());
		entry.changeEndTime(dateEnd.toLocalTime());
		entry.changeEndDate(dateEnd.toLocalDate());
		return entry;
	} 

    
    private VEvent createIcalEntryFromCalFXEntry(Entry<?> entry)
    {
        var startTime = GregorianCalendar.from(entry.getStartAsZonedDateTime()).getTime();
        var endTime = GregorianCalendar.from(entry.getEndAsZonedDateTime()).getTime();
        var start = new DateTime(startTime);
        var end = new DateTime(endTime);
        var title = entry.getTitle();
        var event = new VEvent(start, end, title);
        var iD = new RandomUidGenerator();
        var uid = iD.generateUid();
        event.getProperties().add(uid); 
        return event;           
    }
}