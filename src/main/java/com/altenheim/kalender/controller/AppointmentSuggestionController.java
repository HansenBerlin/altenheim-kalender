package com.altenheim.kalender.controller;

import java.awt.print.Printable;
import java.lang.invoke.VarHandle;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import com.altenheim.kalender.interfaces.*;
import com.calendarfx.model.Calendar;
import com.calendarfx.model.Entry;

public class AppointmentSuggestionController implements IAppointmentSuggestionController {

	private ICalendarEntriesModel administrateEntries;

	public AppointmentSuggestionController(ICalendarEntriesModel administrateEntries) {
		this.administrateEntries = administrateEntries;
	}

	public AppointmentSuggestionController() {
	}

	public List<Entry<String>> getAvailableAppointments(Calendar calendar, ArrayList<LocalTime>[] openingHours,
			LocalDate firstDate, long interval, int spread, int maxOffers, int appointmentDuration, int travelTime) {

		switch (LocalDate.now().getDayOfWeek().toString()) {
		case "MONDAY":
			System.out.println("MONDAY");
			if (openingHours[0] != null && !openingHours[0].isEmpty()) {
				
				var list = Logik(calendar, openingHours[0], appointmentDuration);
				for (Entry<?> listEntry : list) {
					System.out.println(listEntry.toString());
				}
			}

			break;
		case "TUESDAY":
			System.out.println("TUESDAY");
			if (openingHours[1] != null && !openingHours[1].isEmpty()) {
				Logik(calendar, openingHours[1], appointmentDuration);

			}

			break;
		case "WEDNESDAY":
			System.out.println("WEDNESDAY");
			if (openingHours[2] != null && !openingHours[2].isEmpty()) {
				Logik(calendar, openingHours[2], appointmentDuration);

			}

			break;
		case "THURSDAY":
			System.out.println("THURSDAY");
			if (openingHours[3] != null && !openingHours[3].isEmpty()) {
				Logik(calendar, openingHours[3], appointmentDuration);

			}

			break;
		case "FRIDAY":
			System.out.println("FRIDAY");
			if (openingHours[4] != null && !openingHours[4].isEmpty()) {
				Logik(calendar, openingHours[4], appointmentDuration);

			}

			break;
		case "SATURDAY":
			System.out.println("SATURDAY");
			if (openingHours[5] != null && !openingHours[5].isEmpty()) {
				Logik(calendar, openingHours[5], appointmentDuration);

			}

			break;
		case "SUNDAY":
			System.out.println("SUNDAY");
			if (openingHours[6] != null && !openingHours[6].isEmpty()) {

				Logik(calendar, openingHours[6], appointmentDuration);

			}

			break;

		default:
			break;
		}

		return null;
	}

	private ArrayList<Entry<?>> Logik(Calendar calendar, ArrayList<LocalTime> open, int appointmentDuration) {

//		var open = openingHours[3];
//		var dayEntries = new ArrayList<LocalTime>();
		var result = calendar.findEntries(LocalDate.now(), LocalDate.now(), ZoneId.systemDefault());
		var allEntries = result.values();
		LocalTime startTime = open.get(0);
		LocalTime endTime = open.get(1);
		var output = new ArrayList<Entry<?>>();

		if (allEntries.size() > 0) {

			for (var entries : allEntries) {
				for (var entry : entries) {

//					System.out.println(entry.toString());
					if (startTime.isAfter(endTime)) {
						return output;
					}
					if (entry.isFullDay()) {
						return null;

					} else {
						if (entry.getEndTime().isAfter(startTime) && (entry.getStartTime().isBefore(startTime)
								|| entry.getStartTime().equals(startTime))) {
							startTime = entry.getEndTime();
							continue;
						} else if (startTime.plusMinutes(appointmentDuration).isBefore(entry.getStartTime())) {

							if (startTime.plusMinutes(appointmentDuration).isBefore(endTime)) {
								var mma = new Entry<String>();
								mma.changeStartTime(startTime);
								mma.changeEndTime(entry.getStartTime());						
								mma.changeStartDate(entry.getStartDate());
								mma.changeEndDate(entry.getEndDate());
								output.add(mma);
								startTime = entry.getEndTime();
							}

						}

					}
				}
			}

		}


		return output;
	}// ende

	@SuppressWarnings("unchecked")
	private ArrayList<LocalTime> findAllDayEntrieTimes(Entry<?> entry, ArrayList<LocalTime> dayEntries) {

		if (dayEntries.size() <= 0) {
			dayEntries.add(entry.getStartTime());
			dayEntries.add(entry.getEndTime());
		} else {

			ArrayList<LocalTime> dayEntries2 = LogicStartTime(entry, dayEntries);
			dayEntries2 = LogicEndTime(dayEntries, dayEntries2, entry, 1);

			dayEntries = (ArrayList<LocalTime>) dayEntries2.clone();

		}

		return dayEntries;
	}

	@SuppressWarnings("unchecked")
	private ArrayList<LocalTime> LogicStartTime(Entry<?> entry, ArrayList<LocalTime> dayEntries) {

		ArrayList<LocalTime> dayEntries2 = new ArrayList<LocalTime>();

		if (entry.getStartTime().isBefore(dayEntries.get(0))) {
			dayEntries2.add(entry.getStartTime());

			if (entry.getEndTime().isBefore(dayEntries.get(0))) {

			}

			for (int i = 1; i < dayEntries.size(); i++) {
				dayEntries2.add(dayEntries.get(i));
			}

		} else {
			// Logik für Startdatum nicht vor erstem Datum in dayEntries2

			for (int i = 0; i < dayEntries.size(); i++) {
				if (entry.getStartTime().isBefore(dayEntries.get(i))) {

					if (i % 2 == 0) {
						dayEntries2.add(entry.getStartTime());

					} else {
						dayEntries2.add(dayEntries.get(i));
					}
				} else {
					dayEntries2.add(dayEntries.get(i));
				}
			}

		}

		return dayEntries2;
	}

	// Fertig
	private ArrayList<LocalTime> LogicEndTime(ArrayList<LocalTime> dayEntries, ArrayList<LocalTime> dayEntries2,
			Entry<?> entry, int searchFromNumber) {
		// logik für EndDatum

		if (entry.getEndTime().isAfter(dayEntries.get(dayEntries.size() - 1))
				|| entry.getEndTime().compareTo(dayEntries.get(dayEntries.size() - 1)) == 0) {

			dayEntries2.set(dayEntries.size() - 1, entry.getEndTime());
		} else {
			for (int i = searchFromNumber; i < dayEntries.size() - 1; i++) {
				if (entry.getEndTime().isBefore(dayEntries.get(i)) || entry.getEndTime().equals(dayEntries.get(i))) {

					if (i % 2 == 0) {

						System.out.println(dayEntries2.get(i - 1));
						dayEntries2.set(i - 1, entry.getEndTime());
						System.out.println(dayEntries2.get(i - 1));
					}
					dayEntries2.add(dayEntries.get(i));
					for (int j = i + 1; j < dayEntries.size() - 1; j++) {
						dayEntries2.add(dayEntries.get(j));
					}

				} else {

					dayEntries2.add(dayEntries.get(i));

				}
			}
		}
//		printList(dayEntries2);
		return dayEntries2;

	}

	@SuppressWarnings("unused")
	private void adjustOpeningHours(Entry<?> entry, ArrayList<LocalTime> open) {
		// EndUhrzeit liegt nach Startzeit
		if (open.get(0).isBefore(entry.getEndTime())) {

			// Startuhrzeit liegt nach der Startzeit
			if (open.get(open.size() - 1).isAfter(entry.getStartTime())) {

				// itteration von Anfang an, solang, bis die Startzeit vor einer der
				// Öffnungzeiten liegt
				for (int beginn = 0; beginn < open.size(); beginn++) {
					if (open.get(beginn).isAfter(entry.getStartTime())) {
						// überprüfung, ob Endzeit nach der letzten Öffnungszeit liegt
						if (!open.get(open.size() - 1).isBefore(entry.getEndTime())) {
							// itteration bis die EndZeit vor einer Öffnungszeit liegt
							for (int end = beginn; end < open.size() - beginn; end++) {
								if (open.get(end).isAfter(entry.getEndTime())) {

									var open2 = open;

									open2.clear();
									// prüfung, ob StartZeit vor Öffnung liegt
									if (beginn % 2 == 0) {
										// alle die vor der beginnzeit liegt hinzufügen
										for (int j = 0; j <= beginn - 1; j++) {
											open2.add(open.get(j));
										}
										if (end % 2 == 0) {
											for (int j = end; j <= open.size(); j++) {
												open2.add(open.get(j));
											}
										} else {
											open2.add(entry.getEndTime());
											for (int j = end + 1; j <= open.size(); j++) {
												open2.add(open.get(j));
											}
										}

									} else {
										for (int j = 0; j <= beginn - 1; j++) {
											open2.add(open.get(j));
										}
										open2.add(entry.getStartTime());

										if (end % 2 == 0) {
											for (int j = end; j <= open.size(); j++) {
												open2.add(open.get(j));
											}
										} else {
											open2.add(entry.getEndTime());
											for (int j = end + 1; j <= open.size(); j++) {
												open2.add(open.get(j));
											}
										}
									}

									open = open2;

								}
							}
						} else {

							var open2 = open;

							open2.clear();
							// prüfung, ob StartZeit vor Öffnung liegt
							if (beginn % 2 == 0) {
								// alle die vor der beginnzeit liegt hinzufügen
								for (int j = 0; j <= beginn - 1; j++) {
									open2.add(open.get(j));
								}

							} else {
								for (int j = 0; j <= beginn - 1; j++) {
									open2.add(open.get(j));
								}
								open2.add(entry.getStartTime());
							}

							open = open2;

						}

					}
				}
			}
		}
	}

	private void printList(ArrayList<LocalTime> list) {
		for (LocalTime localTime : list) {
			System.out.println(localTime);
		}
		System.out.println();
		System.out.println("------------------------------------------");
		System.out.println();
	}

//	var freeTime = 0;
//	var freeTime1 = 0;
//	int hoursBeforOpen = ((int) Math.ceil(travelTime / 60)) + 1;
//	appointmentDuration += 15 - appointmentDuration % 15;

	// for (int day = 0; day < spread; day++) {
	// freeTime = 0;
	// freeTime1 = 0;
//
	// for (int hour = institutionOpen - hoursBeforOpen; hour < institutionClose;
	// hour++)
	// {
	// for (int min = 0; min < 60; min++)
	// {
	// if (hour < institutionOpen && min < 60 - ((int) Math.ceil(travelTime % 60)))
	// {
	// min = 60 - (int) (travelTime % 60);
	// }
//
	// if (allEntries.getSpecificDate(planetAppointmentDay + day, hour, min) == null
	// && freeTime <= appointmentDuration + travelTime)
	// {
	// freeTime++;
	// if (freeTime > appointmentDuration + travelTime)
	// {
	// maxOffers--;
	// if (maxOffers >= 0)
	// prepareAppointment(appointmentDuration, travelTime, planetAppointmentDay +
	// day, hour,
	// min);
//
	// freeTime = 0;
	// }
	// } else
	// {
	// freeTime = 0;
	// }
	// if (allEntries.getSpecificDate(planetAppointmentDay - day, hour, min) == null
	// && freeTime1 <= appointmentDuration + travelTime && day != 0) {
	// freeTime1++;
	// if (freeTime1 > appointmentDuration + travelTime)
	// {
	// maxOffers--;
	// if (maxOffers >= 0)
	// possibleCalendarEntrys.add(prepareAppointment(appointmentDuration,
	// travelTime,
	// planetAppointmentDay - day, hour, min));
	// freeTime1 = 0;
	// }
	// } else {
	// freeTime1 = 0;
	// }
	// }
	// }
	// }

//	private CalendarEntryModel prepareAppointment(int appointmentDuration, int travelTime, int day, int hour, int min) {
	// int minAppointment = 0;
	// int minAppointmentTravel = 0;
	//
	// if ((min - (int) (appointmentDuration % 60)) < 0)
	// {
	// if (min - (int) ((appointmentDuration + travelTime) % 60) < 0)
	// {
	// minAppointment = min + 60 - (int) (appointmentDuration % 60);
	// minAppointmentTravel = min + 60 - (int) ((appointmentDuration + travelTime) %
	// 60);
	// } else
	// {
	// minAppointment = min + 60 - (int) (appointmentDuration % 60);
	// minAppointmentTravel = min - (int) ((appointmentDuration + travelTime) % 60);
	// }
//
	// } else
	// {
	// if (min - (int) ((appointmentDuration + travelTime) % 60) < 0)
	// {
	// minAppointment = min - (int) (appointmentDuration % 60);
	// minAppointmentTravel = min + 60 - (int) ((appointmentDuration + travelTime) %
	// 60);
//
	// } else
	// {
	// minAppointment = min - (int) (appointmentDuration % 60);
	// minAppointmentTravel = min - (int) ((appointmentDuration + travelTime) % 60);
	// }
	// }
	//
	// GregorianCalendar gregorianCalendar = new GregorianCalendar();
	// gregorianCalendar.set(Calendar.DAY_OF_YEAR, day);
	// var month = gregorianCalendar.get(Calendar.MONTH);
	// var dayOfMonth = gregorianCalendar.get(Calendar.DAY_OF_MONTH);
	// var hourStart = hour - (int) Math.ceil((appointmentDuration + travelTime) /
	// 60);
//
	// return (CalendarEntryModel) administrateEntries.createDefinedEntry(new int[]
	// { 2021, month+1, dayOfMonth },
	// new int[] { 2021, month+1, dayOfMonth }, new int[] { hourStart,
	// minAppointmentTravel },
	// new int[] { hour, min }, "Vorschlag", travelTime);

//		return null;

//	}

}
