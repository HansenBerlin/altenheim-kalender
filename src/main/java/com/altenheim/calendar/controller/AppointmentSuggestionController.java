package com.altenheim.calendar.controller;

import java.util.ArrayList;
import java.util.List;
import com.altenheim.calendar.interfaces.*;
import com.altenheim.calendar.models.CalendarEntryModel;

public class AppointmentSuggestionController implements IAppointmentSuggestionController {
	
	private ICalendarEntriesModel administrateEntries;
	
	public AppointmentSuggestionController(ICalendarEntriesModel administrateEntries) {
		this.administrateEntries = administrateEntries;
	}

	@Override
	public List<CalendarEntryModel> getAvailableAppointments(int firstDate, int interval, int spread, int maxOffers,
			int appointmentDuration, int travelTime, int institutionOpen, int institutionClose) {
		// TODO Auto-generated method stub
		return null;
	}
}
	/*

	public List<CalendarEntryModel> getAvailableAppointments(int firstDate, int interval, int spread, int maxOffers,
			int appointmentDuration, int travelTime, int institutionOpen, int institutionClose) {
		List<CalendarEntryModel> possibleCalendarEntrys = new ArrayList<CalendarEntryModel>();
		var planetAppointmentDay = firstDate + interval;
		var freeTime = 0;
		var freeTime1 = 0;
		int hoursBeforOpen = ((int) Math.ceil(travelTime / 60)) + 1;
		appointmentDuration +=  15 - appointmentDuration%15 ;

		for (int day = 0; day < spread; day++) {
			freeTime = 0;
			freeTime1 = 0;

			for (int hour = institutionOpen - hoursBeforOpen; hour < institutionClose; hour++) 
			{
				for (int min = 0; min < 60; min++) 
				{
					if (hour < institutionOpen && min < 60 - ((int) Math.ceil(travelTime % 60))) 
					{
						min = 60 - (int) (travelTime % 60);
					}

					if (allEntries.getSpecificDate(planetAppointmentDay + day, hour, min) == null
							&& freeTime <= appointmentDuration + travelTime) 
					{
						freeTime++;
						if (freeTime > appointmentDuration + travelTime) 
						{
							maxOffers--;
							if (maxOffers >= 0)
								prepareAppointment(appointmentDuration, travelTime, planetAppointmentDay + day, hour,
										min);

							freeTime = 0;
						}
					} else 
					{
						freeTime = 0;
					}
					if (allEntries.getSpecificDate(planetAppointmentDay - day, hour, min) == null
							&& freeTime1 <= appointmentDuration + travelTime && day != 0) {
						freeTime1++;
						if (freeTime1 > appointmentDuration + travelTime) 
						{
							maxOffers--;
							if (maxOffers >= 0)
								possibleCalendarEntrys.add(prepareAppointment(appointmentDuration, travelTime,
										planetAppointmentDay - day, hour, min));
							freeTime1 = 0;
						}
					} else {
						freeTime1 = 0;
					}
				}
			}
		}	
		return possibleCalendarEntrys;
	}

	private CalendarEntryModel prepareAppointment(int appointmentDuration, int travelTime, int day, int hour, int min) {
		int minAppointment = 0;
		int minAppointmentTravel = 0;
		
		if ((min - (int) (appointmentDuration % 60)) < 0) 
		{
			if (min - (int) ((appointmentDuration + travelTime) % 60) < 0) 
			{
				minAppointment = min + 60 - (int) (appointmentDuration % 60);
				minAppointmentTravel = min + 60 - (int) ((appointmentDuration + travelTime) % 60);
			} else 
			{
				minAppointment = min + 60 - (int) (appointmentDuration % 60);
				minAppointmentTravel = min - (int) ((appointmentDuration + travelTime) % 60);
			}

		} else 
		{
			if (min - (int) ((appointmentDuration + travelTime) % 60) < 0) 
			{
				minAppointment = min - (int) (appointmentDuration % 60);
				minAppointmentTravel = min + 60 - (int) ((appointmentDuration + travelTime) % 60);

			} else 
			{
				minAppointment = min - (int) (appointmentDuration % 60);
				minAppointmentTravel = min - (int) ((appointmentDuration + travelTime) % 60);
			}
		}
		
		GregorianCalendar gregorianCalendar = new GregorianCalendar();
		gregorianCalendar.set(Calendar.DAY_OF_YEAR, day);
		var month = gregorianCalendar.get(Calendar.MONTH);
		var dayOfMonth = gregorianCalendar.get(Calendar.DAY_OF_MONTH);
		var hourStart = hour - (int) Math.ceil((appointmentDuration + travelTime) / 60);

		return (CalendarEntryModel) administrateEntries.createDefinedEntry(new int[] { 2021, month+1, dayOfMonth },
				new int[] { 2021, month+1, dayOfMonth }, new int[] { hourStart, minAppointmentTravel },
				new int[] { hour, min }, "Vorschlag", travelTime);

//				System.out.println(
//				String.format("Der Terminvorschlag ist am %s von %s bis %s und die Anreise beginnt um %s", day,
//						String.format("%s:%s", hour - (int) Math.ceil(appointmentDuration / 60), minAppointment),
//						String.format("%s:%s", hour, min),
//						String.format("%s:%s", hour - (int) Math.ceil((appointmentDuration + travelTime) / 60), minAppointmentTravel)));

	}
}

*/