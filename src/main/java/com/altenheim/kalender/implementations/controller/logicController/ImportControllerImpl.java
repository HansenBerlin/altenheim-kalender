package com.altenheim.kalender.implementations.controller.logicController;

import java.io.FileInputStream;
import java.io.IOException;
import com.altenheim.kalender.interfaces.factorys.EntryFactory;
import com.altenheim.kalender.interfaces.logicController.ImportController;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.property.DtEnd;
import net.fortuna.ical4j.model.property.DtStart;

public class ImportControllerImpl implements ImportController
{
    private EntryFactory entryFactory;
    private Calendar tempCalendar;
    private com.calendarfx.model.Calendar tempFxCalendar;

    public ImportControllerImpl(EntryFactory entryFactory)
    {
        this.entryFactory = entryFactory;
    }

    public boolean canCalendarFileBeImported (String path)
    {
        tempCalendar = null;
        tempFxCalendar = null;
        try
        {
            var stream = new FileInputStream(path);
            var builder = new CalendarBuilder();
            var iCalCalendar = builder.build(stream);
            stream.close();
            tempCalendar = iCalCalendar;
            return true;
        }
        catch (net.fortuna.ical4j.data.ParserException | IOException e)
        {
            System.err.println(e.getMessage());
            return false;
        }
    }

    public boolean canCalendarFileBeParsed()
    {
        try
        {
            var fxCalendar = parseICal(tempCalendar);
            tempFxCalendar = fxCalendar;
            tempCalendar = null;
            return true;
        }
        catch (Exception e)
        {
            System.err.println(e.getMessage());
            tempCalendar = null;
            return false;
        }
    }

    public void importCalendar(String name)
    {
        if (name.contains("HWR-Kalender"))
            tempFxCalendar.setName(name);
        com.calendarfx.model.Calendar localCalendar = tempFxCalendar;
        entryFactory.addCalendarToView(localCalendar, localCalendar.getName());
        tempFxCalendar = null;
    }

    private com.calendarfx.model.Calendar parseICal(Calendar iCalCalendar)
    {
        com.calendarfx.model.Calendar fxCalendar;
        if (iCalCalendar.getProperties().getProperty("X-WR-CALNAME") != null) {
            var iCalCalendarName = ((Property) iCalCalendar.getProperties().getProperty("X-WR-CALNAME")).getValue();
            fxCalendar = new com.calendarfx.model.Calendar(iCalCalendarName);

        } else
            fxCalendar = new com.calendarfx.model.Calendar("Standardkalender");

        var components = iCalCalendar.getComponents("VEVENT");
        for (net.fortuna.ical4j.model.component.CalendarComponent component : components) {
            var start = (DtStart) (component.getProperties().getProperty("DTSTART"));
            var end = (DtEnd) component.getProperties().getProperty("DTEND");
            var startMilli = start.getDate().toInstant().toEpochMilli();
            var endMilli = end.getDate().toInstant().toEpochMilli();
            var entry = entryFactory.createCalendarFXEntryFromMillis(startMilli, endMilli);
            var summary = ((Property) component.getProperties().getProperty("SUMMARY")).getValue();
            entry.setTitle(summary);
            var locationProp = (Property) component.getProperties().getProperty("LOCATION");
            if (locationProp != null)
                entry.setLocation(locationProp.getValue());
            fxCalendar.addEntry(entry);
        }
        return fxCalendar;
    }
}