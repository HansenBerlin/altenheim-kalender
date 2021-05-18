package controller;

import java.util.Calendar;
import interfaces.IAppointmentEntryFactory;
import interfaces.IAppointmentSuggestionController;
import interfaces.ICalendarEntriesModel;
import interfaces.ICalendarEntryModel;

public class AppointmentSuggestionController implements IAppointmentSuggestionController
{
    private ICalendarEntriesModel allEntries;
    private IAppointmentEntryFactory administrateEntries;

    public AppointmentSuggestionController(ICalendarEntriesModel allEntries, IAppointmentEntryFactory administrateEntries)
    {
        this.allEntries = allEntries;
        this.administrateEntries = administrateEntries;
        
        // übergebens Interface beinhaltet Arrays für random generierte Termine
        // und für jeden 7ten Tag ('Sonntag') Kannste dir mit den ensprechenden Methoden
        // komplett rausholen oder nur spezifische Tage
    }

    public int testFunction()
    {
        int days = 0;
        for (ICalendarEntryModel entry : allEntries.getAllRandomDates()) 
        {
            if (entry != null)            
                days += entry.getStartDate().get(Calendar.DAY_OF_YEAR);  
        }
        return days;
    }

    public void testFunctionTwo()
    {
        var newEntry = administrateEntries.createDefinedEntry(new int[]{2021, 10, 1}, 
            new int[]{2021, 10, 1}, new int[]{10, 15}, new int[]{12, 30}, "Custom Entry");  
        allEntries.saveDate(true, newEntry);      
    }
    
}
