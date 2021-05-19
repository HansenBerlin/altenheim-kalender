package interfaces;

public interface IAppointmentEntryFactory 
{
    public ICalendarEntryModel[] createRandomDates();
    public ICalendarEntryModel[] createFixedDates();    
    public ICalendarEntryModel createDefinedEntry(int[] startDate, int[] endDate, 
        int[] startTime, int[] endTime, String entryName);
}
