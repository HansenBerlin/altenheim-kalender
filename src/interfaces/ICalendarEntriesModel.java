package interfaces;

import models.CalendarEntryModel;

public interface ICalendarEntriesModel 
{
    public CalendarEntryModel[] getAllRandomDates();
    public CalendarEntryModel[] getAllFixedDates();
    public CalendarEntryModel getSpecificRandomDate(int day);
    public CalendarEntryModel getSpecificFixedDate(int day);
}
