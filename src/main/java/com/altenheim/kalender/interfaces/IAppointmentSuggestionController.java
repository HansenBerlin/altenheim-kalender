package com.altenheim.kalender.interfaces;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.Entry;

public interface IAppointmentSuggestionController 
{
    public List<Entry<String>> getAvailableAppointments(Calendar calendar, ArrayList<LocalTime>[] oeffnungszeiten, LocalDate firstDate, long interval, int spread, int maxOffers,
			int appointmentDuration, int travelTime);
  
}
