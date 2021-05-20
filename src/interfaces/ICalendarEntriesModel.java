package interfaces;

public interface ICalendarEntriesModel 
{
    public ICalendarEntryModel[][][] getYear();
    public ICalendarEntryModel getSpecificDate(int day, int hour, int minute);
    public void saveDate(ICalendarEntryModel newEntry);
    public void printCalendarDates(int numberOfEntriesToPrint);
    public void initializeYear();
}
