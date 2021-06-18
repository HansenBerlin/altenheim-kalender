package com.altenheim.kalender.controller.logicController;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.List;

import com.altenheim.kalender.interfaces.ICalendarEntriesModel;
import com.altenheim.kalender.interfaces.IEntryFactory;
import com.altenheim.kalender.interfaces.IExportController;
import com.altenheim.kalender.models.ContactModel;
import com.altenheim.kalender.models.MailTemplateModel;
import com.altenheim.kalender.models.SettingsModel;
import com.calendarfx.model.Entry;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Version;
import net.fortuna.ical4j.model.property.XProperty;
import net.fortuna.ical4j.util.RandomUidGenerator;
import net.fortuna.ical4j.validate.ValidationException;

public class ExportController extends IOController implements IExportController
{

    public ExportController(IEntryFactory administrateEntries, List<ContactModel> allContacts, SettingsModel settings,
            List<MailTemplateModel> mailTemplates, ICalendarEntriesModel allEntries) 
    {
        super(administrateEntries, allContacts, settings, mailTemplates, allEntries);
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
