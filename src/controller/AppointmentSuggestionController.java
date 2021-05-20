package controller;

import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;

import interfaces.IAppointmentEntryFactory;
import interfaces.IAppointmentSuggestionController;
import interfaces.ICalendarEntriesModel;
import interfaces.ICalendarEntryModel;

public class AppointmentSuggestionController implements IAppointmentSuggestionController {
	private ICalendarEntriesModel allEntries;
	private IAppointmentEntryFactory administrateEntries;

	public AppointmentSuggestionController(ICalendarEntriesModel allEntries,
			IAppointmentEntryFactory administrateEntries) {
		this.allEntries = allEntries;
		this.administrateEntries = administrateEntries;

		// übergebens Interface beinhaltet Arrays für random generierte Termine
		// und für jeden 7ten Tag ('Sonntag') Kannste dir mit den ensprechenden Methoden
		// komplett rausholen oder nur spezifische Tage
	}

	public int testFunction(int firstDate, int interval, int spread, int maxOffers, int appointmentDuration, int travelTime, int institutionOpen, int institutionClose) {


		var planetAppointmentDay = firstDate + interval;
		var freeTime = 0;
		var freeTime1 = 0;
		int hoursBeforOpen = ((int) Math.ceil(travelTime / 60)) + 1;
		appointmentDuration = ((appointmentDuration % 15 == 0) ? appointmentDuration
				: ((appointmentDuration / 15 + 1) * 15));

		for (int day = 0; day < spread; day++) {
			freeTime = 0;
			freeTime1 = 0;

			for (int hour = institutionOpen - hoursBeforOpen; hour < institutionClose; hour++) {
				for (int min = 0; min < 60; min++) {
					if (hour < institutionOpen && min < 60 - ((int) Math.ceil(travelTime % 60))) {
						min = 60 - (int) (travelTime % 60);
					}

					if (allEntries.getSpecificDate(planetAppointmentDay + day, hour, min) == null
							&& freeTime <= appointmentDuration + travelTime) {
						freeTime++;
						if (freeTime > appointmentDuration + travelTime) {
							maxOffers--;
							if (maxOffers >= 0)
								printAppointment(appointmentDuration, travelTime, planetAppointmentDay + day, hour,
										min);

							freeTime = 0;
						}
					} else {
						freeTime = 0;
					}
					if (allEntries.getSpecificDate(planetAppointmentDay - day, hour, min) == null
							&& freeTime1 <= appointmentDuration + travelTime && day != 0) {
						freeTime1++;
						if (freeTime1 > appointmentDuration + travelTime) {
							maxOffers--;
							if (maxOffers >= 0)
								printAppointment(appointmentDuration, travelTime, planetAppointmentDay - day, hour,
										min);

							freeTime1 = 0;
						}
					} else {
						freeTime1 = 0;
					}

				}

			}
		}

//    		
		return 0;

	}

	@Override
	public void testFunctionTwo() {
		// TODO Auto-generated method stub

	}

	private void printAppointment(int appointmentDuration, int travelTime, int day, int hour, int min) {

		if ((min - (int) (appointmentDuration % 60)) < 0) {
			if (min - (int) ((appointmentDuration + travelTime) % 60) < 0) {
				System.out.println(
						String.format("Der Terminvorschlag ist am %s von %s bis %s und die Anreise beginnt um %s", day,
								String.format("%s:%s", hour - (int) Math.ceil(appointmentDuration / 60),
										min + 60 - (int) (appointmentDuration % 60)),
								String.format("%s:%s", hour, min),
								String.format("%s:%s", hour - (int) Math.ceil((appointmentDuration + travelTime) / 60),
										min + 60 - (int) ((appointmentDuration + travelTime) % 60))));

			} else {
				System.out.println(
						String.format("Der Terminvorschlag ist am %s von %s bis %s und die Anreise beginnt um %s", day,
								String.format("%s:%s", hour - (int) Math.ceil(appointmentDuration / 60),
										min + 60 - (int) (appointmentDuration % 60)),
								String.format("%s:%s", hour, min),
								String.format("%s:%s", hour - (int) Math.ceil((appointmentDuration + travelTime) / 60),
										min - (int) ((appointmentDuration + travelTime) % 60))));
			}

		} else {
			if (min - (int) ((appointmentDuration + travelTime) % 60) < 0) {
				System.out.println(
						String.format("Der Terminvorschlag ist am %s von %s bis %s und die Anreise beginnt um %s", day,
								String.format("%s:%s", hour - (int) Math.ceil(appointmentDuration / 60),
										min - (int) (appointmentDuration % 60)),
								String.format("%s:%s", hour, min),
								String.format("%s:%s", hour - (int) Math.ceil((appointmentDuration + travelTime) / 60),
										min + 60 - (int) ((appointmentDuration + travelTime) % 60))));

			} else {
				System.out.println(
						String.format("Der Terminvorschlag ist am %s von %s bis %s und die Anreise beginnt um %s", day,
								String.format("%s:%s", hour - (int) Math.ceil(appointmentDuration / 60),
										min - (int) (appointmentDuration % 60)),
								String.format("%s:%s", hour, min),
								String.format("%s:%s", hour - (int) Math.ceil((appointmentDuration + travelTime) / 60),
										min - (int) ((appointmentDuration + travelTime) % 60))));
			}

		}

	}

}
