package com.altenheim.kalender.TempTestClasses;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import com.altenheim.kalender.controller.viewController.CalendarViewOverride;
import com.altenheim.kalender.interfaces.ICalendarEntriesModel;
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


public class ReadWriteICal 
{
    private ICalendarEntriesModel allEntries;
    private CalendarViewOverride calendarView;

    public ReadWriteICal(ICalendarEntriesModel allEntries, CalendarViewOverride calendarView)
    {
        this.allEntries = allEntries;
        this.calendarView = calendarView;
    }
    
    public void loadCalendars() throws ParseException, IOException, ParserException
    {
        var stream = new FileInputStream("testExport.ics");
        var builder = new CalendarBuilder();
        var fxcalendar = builder.build(stream);
        var components = fxcalendar.getComponents();
        var calNameProp = (Property) fxcalendar.getProperties().getProperty("X-WR-CALNAME");
        var calendar = new com.calendarfx.model.Calendar();
        if(calNameProp != null)
            calendar.setName(calNameProp.getValue());
        System.out.println(calendar.getName());

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
        var myCalendarSource = new CalendarSource("");
        myCalendarSource.getCalendars().addAll(calendar);
        calendarView.getCalendarSources().addAll(myCalendarSource);
    } 

    public void saveCalendars() throws ValidationException, IOException
    {
        var entries = createEntriesListFromCalendar();
        var icsCalendar = new Calendar();
        icsCalendar.getProperties().add(new ProdId("-//Smart Planner//iCal4j 1.0//DE"));
        icsCalendar.getProperties().add(Version.VERSION_2_0);
        icsCalendar.getProperties().add(CalScale.GREGORIAN);
        for (var entry : entries)        
            icsCalendar.getComponents().add(createIcalEntryFromCalFXEntry(entry));        
        var fout = new FileOutputStream("testExport.ics");
        var outputter = new CalendarOutputter();
        outputter.output(icsCalendar, fout);
        System.out.println("File created in root directory.");
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
    

    public ArrayList<Entry<?>> createEntriesListFromCalendar() 
	{			
		var result = allEntries.getAllCalendars();		
		var output = new ArrayList<Entry<?>>();
        var zoneId = ZoneId.systemDefault();

		for (var calendar : result) 		
		{	
            var firstEntry = LocalDate.ofInstant(calendar.getEarliestTimeUsed(), zoneId);
            var lastEntry = LocalDate.ofInstant(calendar.getLatestTimeUsed(), zoneId);
            var entries = calendar.findEntries(firstEntry, lastEntry, zoneId);

			for (var entry : entries.values())
			{		
                for (var singleEntry : entry)
                    output.add(singleEntry);					
			}		
		}			
		return output;
	}
}
