package com.altenheim.kalender.controller.logicController;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.GregorianCalendar;
import com.altenheim.kalender.interfaces.ICalendarEntriesModel;
import com.altenheim.kalender.interfaces.IImportController;
import com.altenheim.kalender.interfaces.IWebsiteScraperController;
import com.calendarfx.model.Entry;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.ComponentList;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.component.CalendarComponent;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.DtEnd;
import net.fortuna.ical4j.model.property.DtStart;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Version;
import net.fortuna.ical4j.model.property.XProperty;
import net.fortuna.ical4j.util.RandomUidGenerator;
import net.fortuna.ical4j.validate.ValidationException;

public class ImportController extends IOController implements IImportController
{
    private ICalendarEntriesModel allEntries;
    private IWebsiteScraperController scraper;

    public ImportController(ICalendarEntriesModel allEntries, IWebsiteScraperController scraper)
    {
        this.allEntries = allEntries;
        this.scraper = scraper;
    }
    public com.calendarfx.model.Calendar importFile(String path) throws ParseException, IOException, ParserException
    {
        ComponentList<CalendarComponent> components = null;
        com.calendarfx.model.Calendar cal;
        try 
        {
            var stream = new FileInputStream(path);
            var builder = new CalendarBuilder();
            var calendar = builder.build(stream);
            components = calendar.getComponents();
            if(calendar.getProperties().getProperty("X-WR-CALNAME") != null)
            {
                var calName = ((Property)calendar.getProperties().getProperty("X-WR-CALNAME")).getValue();
                cal = new com.calendarfx.model.Calendar(calName);
            }else
            {
                cal = new com.calendarfx.model.Calendar();
            }      
        } catch (IOException | ParserException e) 
        {
            e.printStackTrace();
            return null;
        }

        for (int i = 0; i < components.size(); i++)         
        {
            if(i < 10)
            {
                if(!components.get(i).getClass().equals(VEvent.class))
                    continue;
            }
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
            cal.addEntry(entry);
        }
        System.out.println(cal.findEntries("").get(0).getInterval().toString());
        return cal;
    } 

    public void exportFile(com.calendarfx.model.Calendar cal) throws ValidationException, IOException
    {
        var entries = cal.findEntries("");
        var calName = cal.getName();
        var icsCalendar = new Calendar();
        icsCalendar.getProperties().add(new ProdId("-//Smart Planner//iCal4j 1.0//DE"));
        icsCalendar.getProperties().add(Version.VERSION_2_0);
        icsCalendar.getProperties().add(CalScale.GREGORIAN);
        icsCalendar.getProperties().add(new XProperty("X-WR-CALNAME", calName));
        
        for(int i = 0; i < entries.size(); i++)
        {
            var entry = entries.get(i);
            icsCalendar.getComponents().add(createIcalEntryFromCalFXEntry(entry));
        }
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

    private Entry<String> createCalFXEntryDummy(int startTime, int EndTime, int startDay, int endDay)
    {
        var entryUser = new Entry<String>("User Preference");
        var startDate = LocalDate.of(2021, 1, startDay);  
        var endDate = LocalDate.of(2021, 1, endDay);  
        entryUser.changeStartDate(startDate);
        entryUser.changeEndDate(endDate);
        entryUser.changeStartTime(LocalTime.of(startTime, 00, 00));
        entryUser.changeEndTime(LocalTime.of(EndTime, 00, 00));
        return entryUser;
    }
}
