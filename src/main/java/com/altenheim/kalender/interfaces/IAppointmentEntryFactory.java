package com.altenheim.kalender.interfaces;

import java.util.HashMap;
import java.util.List;
import com.calendarfx.model.Entry;

public interface IAppointmentEntryFactory extends IContactFactory
{
    public void createRandomCalendarList(String calendarName);
    public HashMap<String, List<Entry<?>>> createEntryListForEachCalendar();
}
