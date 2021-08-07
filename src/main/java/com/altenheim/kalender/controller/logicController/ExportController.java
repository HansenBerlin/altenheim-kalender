package com.altenheim.kalender.controller.logicController;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.GregorianCalendar;
import com.altenheim.kalender.interfaces.IExportController;
import com.altenheim.kalender.interfaces.SettingsModel;
import com.altenheim.kalender.models.SettingsModelImpl;
import com.calendarfx.model.Entry;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.*;
import net.fortuna.ical4j.util.RandomUidGenerator;
import net.fortuna.ical4j.validate.ValidationException;

public class ExportController implements IExportController 
{
    protected SettingsModel settings;

    public ExportController(SettingsModel settings) {
        this.settings = settings;
    }

    public void exportCalendarAsFile(com.calendarfx.model.Calendar fxCalendar, String path)
            throws ValidationException, IOException 
    {
        var entries = fxCalendar.findEntries("");
        if (entries.size() == 0)
            return;
        var fxCalendarName = fxCalendar.getName();
        var fout = new FileOutputStream(new File(path + "/" + fxCalendarName + ".ics"));
        var iCalCalendar = initICalCalendar();
        iCalCalendar.getProperties().add(new XProperty("X-WR-CALNAME", fxCalendarName));
        for (int i = 0; i < entries.size(); i++) 
        {
            var entry = entries.get(i);
            iCalCalendar.getComponents().add(createIcalEntryFromCalFXEntry((Entry<String>) entry));
        }
        var outputter = new CalendarOutputter();
        try 
        {
            outputter.output(iCalCalendar, fout);
        } 
        catch (Exception e) 
        {
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

    public void exportEntryAsFile(Entry<String> entry) throws ValidationException, IOException {
        var iCalCalendar = initICalCalendar();
        iCalCalendar.getComponents().add(createIcalEntryFromCalFXEntry(entry));
        var fout = new FileOutputStream(new File(entry.getTitle() + ".ics"));
        var outputter = new CalendarOutputter();
        outputter.output(iCalCalendar, fout);
        fout.close();
    }
    
}
