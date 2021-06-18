package com.altenheim.kalender.interfaces;

import net.fortuna.ical4j.validate.ValidationException;

import java.io.IOException;

public interface IExportController
{
    public void exportFile(com.calendarfx.model.Calendar fxCalendar) throws ValidationException, IOException;
}
