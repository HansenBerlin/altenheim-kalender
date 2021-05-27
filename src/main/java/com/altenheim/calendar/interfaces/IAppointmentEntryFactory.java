package com.altenheim.calendar.interfaces;

import java.util.List;

public interface IAppointmentEntryFactory 
{
    public List<ICalendarEntryModel> getDummyEntries();
    public List<ICalendarEntryModel> createEntrys();
}
