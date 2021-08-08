package com.altenheim.kalender.interfaces.logicController;

import net.fortuna.ical4j.validate.ValidationException;
import java.io.IOException;

public interface ExportController
{
    void exportCalendarAsFile(com.calendarfx.model.Calendar fxCalendar, String path) throws ValidationException, IOException;
}
