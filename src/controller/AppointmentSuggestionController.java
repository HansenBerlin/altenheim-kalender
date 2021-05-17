package controller;

import java.util.Calendar;

import interfaces.IAppointmentSuggestionController;
import interfaces.ICalendarEntriesModel;
import models.CalendarEntryModel;

public class AppointmentSuggestionController implements IAppointmentSuggestionController
{
    private ICalendarEntriesModel allEntries;

    public AppointmentSuggestionController(ICalendarEntriesModel allEntries)
    {
        this.allEntries = allEntries;
        
        // übergebens Interface beinhaltet Arrays für random generierte Termine
        // und für jeden 7ten Tag ('Sonntag') Kannste dir mit den ensprechenden Methoden
        // komplett rausholen oder nur spezifische Tage
    }

    public int testFunction()
    {
        int days = 0;
        for (CalendarEntryModel entry : allEntries.getAllRandomDates()) 
        {
            if (entry != null)
                days += entry.getDate(true).get(Calendar.DAY_OF_WEEK);            
        }
        return days;
    }
    
}
