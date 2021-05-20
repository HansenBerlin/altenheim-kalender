package interfaces;

import java.util.List;
import models.CalendarEntryModel;

public interface IAppointmentSuggestionController 
{
    public List<CalendarEntryModel> getAvailableAppointments(int firstDate, int interval, int spread, int maxOffers, int appointmentDuration, int travelTime, int institutionOpen, int institutionClose);
  
}
