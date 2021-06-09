package com.altenheim.kalender.controller.logicController;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import com.calendarfx.model.Entry;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.property.DtEnd;
import net.fortuna.ical4j.model.property.DtStart;


public class ImportExportTest 
{
    public void testExport() throws ParseException, IOException, ParserException
    {
        var stream = new FileInputStream("C://Users//Hannes//Documents//test.ics");
        var builder = new CalendarBuilder();
        var calendar = builder.build(stream);
        var components = calendar.getComponents();
        for (int i = 1; i < components.size(); i++)         
        {
            var start = (DtStart)components.get(i).getProperties().get(7);
            var end = (DtEnd)components.get(i).getProperties().get(8);
            var startMilli = start.getDate().toInstant().toEpochMilli();
            var endMilli = end.getDate().toInstant().toEpochMilli();            
            var entry = createEntryFromMillis(startMilli, endMilli);
            System.out.println(entry.getStartTime() + " " + entry.getEndTime());
        }        
    }  

    private Entry<String> createEntryFromMillis(long start, long end)
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
}