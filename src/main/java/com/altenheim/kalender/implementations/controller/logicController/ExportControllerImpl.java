package com.altenheim.kalender.implementations.controller.logicController;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.GregorianCalendar;
import com.altenheim.kalender.interfaces.logicController.ExportController;
import com.altenheim.kalender.interfaces.models.SettingsModel;
import com.calendarfx.model.Entry;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.*;
import net.fortuna.ical4j.util.RandomUidGenerator;
import net.fortuna.ical4j.validate.ValidationException;

public record ExportControllerImpl(SettingsModel settings) implements ExportController
{
    public void exportCalendarAsFile(com.calendarfx.model.Calendar fxCalendar, String path)
            throws ValidationException, IOException {
        var entries = fxCalendar.findEntries("");
        if (entries.size() == 0)
            return;
        var fxCalendarName = fxCalendar.getName();
        var fout = new FileOutputStream(new File(path + "/" + fxCalendarName + ".ics"));
        var iCalCalendar = initICalCalendar();
        iCalCalendar.getProperties().add(new XProperty("X-WR-CALNAME", fxCalendarName));
        for (int i = 0; i < entries.size(); i++) {
            var entry = entries.get(i);
            iCalCalendar.getComponents().add(createIcalEntryFromCalFXEntry((Entry<String>) entry));
        }
        var outputter = new CalendarOutputter();
        try {
            outputter.output(iCalCalendar, fout);
        } catch (Exception e) {
            e.printStackTrace();
        }

        fout.close();
    }

    private Calendar initICalCalendar() {
        var iCalCalendar = new Calendar();
        iCalCalendar.getProperties().add(new ProdId("-//Smart Planner//iCal4j 1.0//DE"));
        iCalCalendar.getProperties().add(Version.VERSION_2_0);
        iCalCalendar.getProperties().add(CalScale.GREGORIAN);
        return iCalCalendar;
    }

    private VEvent createIcalEntryFromCalFXEntry(Entry<String> entry) {
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
