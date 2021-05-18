package models;

import java.util.Calendar;

import interfaces.IAppointmentEntryFactory;
import interfaces.ICalendarEntriesModel;
import interfaces.ICalendarEntryModel;

public class CalendarEntriesModel implements ICalendarEntriesModel
{
    private ICalendarEntryModel[] savedRandomDates;
    private ICalendarEntryModel[] savedSundays;

    public CalendarEntriesModel(IAppointmentEntryFactory entryFactory)
    {
        savedRandomDates = entryFactory.createFixedDates();
        savedSundays = entryFactory.createFixedDates();
    }

    public ICalendarEntryModel[] getAllRandomDates()
    {
        return savedRandomDates;
    }

    public ICalendarEntryModel[] getAllFixedDates()
    {
        return savedSundays;
    }

    public ICalendarEntryModel getSpecificRandomDate(int day)
    {
        return savedRandomDates[day];
    }

    public ICalendarEntryModel getSpecificFixedDate(int day)
    {
        return savedSundays[day];
    }

    public void saveDate(boolean saveToSavedRandomDates, ICalendarEntryModel newEntry)
    {
        if (saveToSavedRandomDates)        
            savedRandomDates[newEntry.getDate(true).get(Calendar.DAY_OF_YEAR)] = newEntry;        
        else
            savedSundays[newEntry.getDate(true).get(Calendar.DAY_OF_YEAR)] = newEntry;  
    }
}
