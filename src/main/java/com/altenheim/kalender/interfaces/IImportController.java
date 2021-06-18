package com.altenheim.kalender.interfaces;

import net.fortuna.ical4j.data.ParserException;

import java.io.IOException;
import java.text.ParseException;

public interface IImportController
{
    public com.calendarfx.model.Calendar importFile(String path);


}
