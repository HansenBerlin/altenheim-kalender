package com.altenheim.kalender.interfaces;

import java.time.LocalTime;
import com.altenheim.kalender.models.EntrySer;

public interface IAppointmentEntryFactory 
{
    public void createRandomEntrys(String calendarName);
    public void createTestCalendar();
    public EntrySer createUserSettingsEntry(LocalTime startSearchTime, LocalTime endSearchTime);
}
