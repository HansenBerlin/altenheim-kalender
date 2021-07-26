package com.altenheim.kalender.interfaces;

import net.fortuna.ical4j.validate.ValidationException;

import java.io.IOException;

public interface IExportController {
    void exportCalendarAsFile(com.calendarfx.model.Calendar fxCalendar, String path)
            throws ValidationException, IOException;
}
