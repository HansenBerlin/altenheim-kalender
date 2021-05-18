package models;

import interfaces.IAppointmentEntryFactory;
import interfaces.ICalendarEntriesModel;

public class CalendarEntriesModel implements ICalendarEntriesModel
{
    private CalendarEntryModel[] savedRandomDates;
    private CalendarEntryModel[] savedSundays;

    public CalendarEntriesModel(IAppointmentEntryFactory entryFactory)
    {
        savedRandomDates = entryFactory.createFixedDates();
        savedSundays = entryFactory.createFixedDates();
    }

    public CalendarEntryModel[] getAllRandomDates()
    {
        return savedRandomDates;
    }

    public CalendarEntryModel[] getAllFixedDates()
    {
        return savedSundays;
    }

    public CalendarEntryModel getSpecificRandomDate(int day)
    {
        return savedRandomDates[day];
    }

    public CalendarEntryModel getSpecificFixedDate(int day)
    {
        return savedSundays[day];
    }
}
