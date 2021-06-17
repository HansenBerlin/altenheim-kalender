package com.altenheim.kalender.controller.logicController;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.GregorianCalendar;
import com.calendarfx.model.Entry;
import net.fortuna.ical4j.data.*;
import net.fortuna.ical4j.model.property.*;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.ComponentList;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.component.CalendarComponent;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.util.RandomUidGenerator;
import net.fortuna.ical4j.validate.ValidationException;


public class ImportExportController 
{
    public com.calendarfx.model.Calendar importFile(String path) throws ParseException, IOException, ParserException
    {
        Calendar iCalCalendar;
        try 
        {
            var stream = new FileInputStream(path);
            var builder = new CalendarBuilder();
            iCalCalendar = builder.build(stream);

        } catch (IOException | ParserException e) 
        {
            e.printStackTrace();
            return null;
        }

        var fxCalendar = parseICal(iCalCalendar);
        
        return fxCalendar;
    } 

    public void exportFile(com.calendarfx.model.Calendar fxCalendar) throws ValidationException, IOException
    {
        var entries = fxCalendar.findEntries("");
        var fxCalendarName = fxCalendar.getName();
        var fout = new FileOutputStream(validate(new File(fxCalendarName + ".ics")));
        var iCalCalendar = initICalCalendar();
        iCalCalendar.getProperties().add(new XProperty("X-WR-CALNAME", fxCalendarName));
        for(int i = 0; i < entries.size(); i++)
        {
            var entry = entries.get(i);
            iCalCalendar.getComponents().add(createIcalEntryFromCalFXEntry(entry));
        }
        var outputter = new CalendarOutputter();
        outputter.output(iCalCalendar, fout);
        fout.close();
    }

    public void exportFile(com.calendarfx.model.Calendar[] fxCalendars) throws ValidationException, IOException
    {
        for(int i = 0; i < fxCalendars.length; i++)
        {
            var entries = fxCalendars[i].findEntries("");
            var fxCalendarName = fxCalendars[i].getName();
            var fout = new FileOutputStream(validate(new File(fxCalendarName + ".ics")));
            var iCalCalendar = initICalCalendar();
            iCalCalendar.getProperties().add(new XProperty("X-WR-CALNAME", fxCalendarName));
            for(int j = 0; j < entries.size(); j++)
            {
                var entry = entries.get(j);
                iCalCalendar.getComponents().add(createIcalEntryFromCalFXEntry(entry));
            }
            var outputter = new CalendarOutputter();
            outputter.output(iCalCalendar, fout);
            fout.close();
        }
    
    }

    public void exportFile(Entry<?> ent) throws ValidationException, IOException
    {
        var iCalCalendar = initICalCalendar();
        iCalCalendar.getComponents().add(createIcalEntryFromCalFXEntry(ent));
        var fout = new FileOutputStream(validate(new File(ent.getTitle() + ".ics")));
        var outputter = new CalendarOutputter();
        outputter.output(iCalCalendar, fout);
        fout.close();
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

    private com.calendarfx.model.Calendar parseICal(Calendar iCalCalendar)
    {
        com.calendarfx.model.Calendar fxCalendar;
        if(iCalCalendar.getProperties().getProperty("X-WR-CALNAME") != null)
        {
            var iCalCalendarName = ((Property)iCalCalendar.getProperties().getProperty("X-WR-CALNAME")).getValue();
            fxCalendar = new com.calendarfx.model.Calendar(iCalCalendarName);

        }else
            fxCalendar = new com.calendarfx.model.Calendar("Standart-Kalender");
        
        var components = iCalCalendar.getComponents("VEVENT");
        for (int i = 0; i < components.size(); i++)         
        {
            var start = (DtStart)(components.get(i).getProperties().getProperty("DTSTART"));
            var end = (DtEnd)((Property) components.get(i).getProperties().getProperty("DTEND"));
            var startMilli = start.getDate().toInstant().toEpochMilli();
            var endMilli = end.getDate().toInstant().toEpochMilli();            
            var entry = createCalendarFXEntryFromMillis(startMilli, endMilli);
            var summary = ((Property) components.get(i).getProperties().getProperty("SUMMARY")).getValue();
            entry.setTitle(summary);
            var locationProp = (Property) components.get(i).getProperties().getProperty("LOCATION");
            if(locationProp != null)
                entry.setLocation(locationProp.getValue());
            fxCalendar.addEntry(entry);
        }
        return fxCalendar;

    }

    private File validate(File file)
    {
        if(file.exists())
        {
            var fileName = file.getName().substring(0, file.getName().length()-4);
            file = new File(fileName + " (new)" + ".ics");
        }
        return file;
    }

    private Calendar initICalCalendar()
    {
        var iCalCalendar = new Calendar();
        iCalCalendar.getProperties().add(new ProdId("-//Smart Planner//iCal4j 1.0//DE"));
        iCalCalendar.getProperties().add(Version.VERSION_2_0);
        iCalCalendar.getProperties().add(CalScale.GREGORIAN);
        return iCalCalendar;
    }
}