package com.altenheim.kalender.interfaces;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.altenheim.kalender.models.SerializableEntry;
import com.calendarfx.model.Calendar;

public interface IEntryFactory {
    void createRandomCalendarList();

    HashMap<String, List<SerializableEntry>> createEntryListForEachCalendar();

    SerializableEntry createUserEntry(LocalDate dateStart, LocalDate dateEnd, LocalTime timeStart, LocalTime timeEnd);

    ArrayList<ArrayList<SerializableEntry>> createOpeningHoursWithLunchBreak();

    void addHWRCalendarToView(Calendar calendar);

    void addCalendarToView(Calendar calendar, String source);

    void removeCalendarFromView(String source, String calName);
}
