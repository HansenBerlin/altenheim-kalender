package interfaces;

public interface ICalendarEntriesModel 
{
    public ICalendarEntryModel[] getAllRandomDates();
    public ICalendarEntryModel[] getAllFixedDates();
    public ICalendarEntryModel getSpecificRandomDate(int day);
    public ICalendarEntryModel getSpecificFixedDate(int day);
    public void saveDate(boolean saveToSavedRandomDates, ICalendarEntryModel newEntry);

}
