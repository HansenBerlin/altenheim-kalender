package com.altenheim.calendar.interfaces;

import java.util.List;
import com.calendarfx.model.Calendar;

public interface IAppointmentSuggestionController 
{
    public List<Calendar> getAvailableAppointments(int firstDate, int interval, int spread, int maxOffers, int appointmentDuration, int travelTime, int institutionOpen, int institutionClose);
  
}
