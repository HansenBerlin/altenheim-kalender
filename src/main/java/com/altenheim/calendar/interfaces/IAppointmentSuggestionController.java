package com.altenheim.calendar.interfaces;

import java.util.List;
import com.calendarfx.model.Entry;

public interface IAppointmentSuggestionController 
{
    public List<Entry<String>> getAvailableAppointments(int firstDate, int interval, int spread, int maxOffers, int appointmentDuration, int travelTime, int institutionOpen, int institutionClose);
  
}
