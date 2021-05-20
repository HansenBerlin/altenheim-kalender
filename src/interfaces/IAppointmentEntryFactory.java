package interfaces;

public interface IAppointmentEntryFactory 
{
    public ICalendarEntryModel[][][] createRandomDatesForOneYear();
    public ICalendarEntryModel[] createEntrys();
    public ICalendarEntryModel createDefinedEntry(int[] startDate, int[] endDate, 
        int[] startTime, int[] endTime, String entryName);
}
