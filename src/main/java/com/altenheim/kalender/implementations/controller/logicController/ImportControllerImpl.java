package com.altenheim.kalender.implementations.controller.logicController;

import java.io.FileInputStream;
import com.altenheim.kalender.interfaces.factorys.EntryFactory;
import com.altenheim.kalender.interfaces.logicController.ImportController;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.property.DtEnd;
import net.fortuna.ical4j.model.property.DtStart;

public record ImportControllerImpl(EntryFactory entryFactory) implements ImportController
{
    public com.calendarfx.model.Calendar importFile(String path) {
        Calendar iCalCalendar;
        try {
            var stream = new FileInputStream(path);
            var builder = new CalendarBuilder();
            if (stream.read() == -1) {
                stream.close();
                return null;
            } else {
                stream.close();
                stream = new FileInputStream(path);
                iCalCalendar = builder.build(stream);
                stream.close();
                return parseICal(iCalCalendar);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new com.calendarfx.model.Calendar();
        }
    }

    private com.calendarfx.model.Calendar parseICal(Calendar iCalCalendar) {
        com.calendarfx.model.Calendar fxCalendar;
        if (iCalCalendar.getProperties().getProperty("X-WR-CALNAME") != null) {
            var iCalCalendarName = ((Property) iCalCalendar.getProperties().getProperty("X-WR-CALNAME")).getValue();
            fxCalendar = new com.calendarfx.model.Calendar(iCalCalendarName);

        } else
            fxCalendar = new com.calendarfx.model.Calendar("Standard-Kalender");

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