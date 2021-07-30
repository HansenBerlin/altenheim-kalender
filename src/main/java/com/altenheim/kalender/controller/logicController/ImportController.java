package com.altenheim.kalender.controller.logicController;

import java.io.FileInputStream;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import com.altenheim.kalender.interfaces.*;
import com.altenheim.kalender.models.SettingsModel;
import com.calendarfx.model.Entry;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.property.DtEnd;
import net.fortuna.ical4j.model.property.DtStart;

public class ImportController implements IImportController {
    protected SettingsModel settings;

    public ImportController(SettingsModel settings) {
        this.settings = settings;
    }

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
            return null;
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
        for (int i = 0; i < components.size(); i++) {
            var start = (DtStart) (components.get(i).getProperties().getProperty("DTSTART"));
            var end = (DtEnd) ((Property) components.get(i).getProperties().getProperty("DTEND"));
            var startMilli = start.getDate().toInstant().toEpochMilli();
            var endMilli = end.getDate().toInstant().toEpochMilli();
            var entry = createCalendarFXEntryFromMillis(startMilli, endMilli);
            var summary = ((Property) components.get(i).getProperties().getProperty("SUMMARY")).getValue();
            entry.setTitle(summary);
            var locationProp = (Property) components.get(i).getProperties().getProperty("LOCATION");
            if (locationProp != null)
                entry.setLocation(locationProp.getValue());
            fxCalendar.addEntry(entry);
        }
        return fxCalendar;
    }

    private Entry<String> createCalendarFXEntryFromMillis(long start, long end) {
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