package com.altenheim.kalender.interfaces.logicController;

public interface ImportController
{
    void importCalendar(String name);
    boolean canCalendarFileBeParsed();
    boolean canCalendarFileBeImported (String path);
}
