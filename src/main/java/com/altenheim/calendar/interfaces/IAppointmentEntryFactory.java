package com.altenheim.calendar.interfaces;

public interface IAppointmentEntryFactory 
{
    public ICalendarEntryModel[] getDummyEntries();
    public ICalendarEntryModel[][][] createRandomDatesForOneYear();
    public ICalendarEntryModel[] createEntrys();
    public ICalendarEntryModel createDefinedEntry(int[] startDate, int[] endDate, 
        int[] startTime, int[] endTime, String entryName, int travelTime);

}
