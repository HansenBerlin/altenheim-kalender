package com.altenheim.kalender.controller.logicController;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import com.altenheim.kalender.interfaces.ICalendarEntriesModel;
import com.altenheim.kalender.interfaces.ISmartSearchController;
import com.calendarfx.model.Entry;

public class SmartSearchController implements ISmartSearchController {
	private ICalendarEntriesModel administrateEntries;

	public SmartSearchController(ICalendarEntriesModel administrateEntries) {
		this.administrateEntries = administrateEntries;
	}

	/*
	 * Die Methode ist zum Aufruf der Suchlogik aus dem SearchViewController
	 * 
	 * Der Methode wird übergeben: input: Der Entry beinhaltet das Startdatum, die
	 * Startuhrzeit und die Enduhrzeit für die Terminsuche duration: Hier wird die
	 * Länge des gesuchten Termins in Minuten übergeben. weekdays: Hier wird ein
	 * boolean Array der Länge 7 übergeben, dabei ist die erste Position Montag und
	 * die letzte Sonntag. (true == Tag darf belegt werden / false == Tag darf nicht
	 * belegt werden) openingHours: Hier wird eine Liste der Länge 7 übergeben, die
	 * die Wochentage repräsentiert. Für jeden Wochentag beinhaltet die Liste eine
	 * weitere Liste, die alle Öffnungzeiten des Tages (in Entrys mit Start- u.
	 * EndTime gespeichert) enthält. timeBefore: Hier wird die Zeit in Minuten
	 * übergeben, die vor dem Termin frei sein muss. timeAfter: Hier wird die Zeit
	 * in Minuten übergeben, die nach dem Termin frei sein muss.
	 * maxNumberOfReturnEntrys: Hier wird die maximale Anzahl an gewünschten
	 * Zeiträumen übergeben. (Bei Intervallverarbeitung wird hier die Anzahl der
	 * Intervalle übergeben) intervalDays: Hier wird die Zeit in Tagen übergeben,
	 * die mindestens zwischen zwei Terminen aufeinanderfolgenden Terminen in der
	 * Intervallverarbeitung liegen soll. (Zur Deaktivierung der Intervallsuche wird
	 * hier 0 übergeben)
	 * 
	 * Die Methode gibt zurück: Liste mit den Zeiträumen für mögliche Einträge
	 */
	public List<Entry<?>> findPossibleTimeSlots(Entry<?> input, int duration, boolean[] weekdays,
			ArrayList<ArrayList<Entry<?>>> openingHours, int timeBefore, int timeAfter, int maxNumberOfReturnEntrys, int intervalDays) {
		var output = new ArrayList<Entry<?>>();
		var startTime = input.getStartTime();
		var endTime = input.getEndTime();	
		var date = input.getStartDate();
		
		var inInterval = true;
		int intervalNumber = 1;
		int i = -1;
		while (output.size() < maxNumberOfReturnEntrys) {
			i++;
			date = date.plusDays(i);

			manipulteDayForInterval(intervalDays, inInterval, input, date, i);
			if (!weekdays[date.getDayOfWeek().getValue() - 1]) {
				inInterval = true;
				continue;
			}

			for (var day : openingHours.get(i % 7)) {
				if (endTime.isBefore(day.getStartTime()) || startTime.isAfter(day.getEndTime()))
					continue;

				var entry = prepareEntry(startTime, endTime, day, date);
				output.addAll(findAvailableTimeSlot(entry, duration, timeBefore, timeAfter));

				if (intervalDays > 0 && intervalNumber >= output.size()) {
					reduceListLenght(output, intervalNumber);
					date = date.plusDays(intervalDays);
					intervalNumber++;
					inInterval = false;
					i = -1;
				}
			}
		}
		if (output.size() >= maxNumberOfReturnEntrys)
			reduceListLenght(output, maxNumberOfReturnEntrys);
		return output;
	}

	private LocalDate manipulteDayForInterval(int intervalDays, boolean inInterval, Entry<?> input, LocalDate date,	int i) {
		if (intervalDays == 0 && inInterval)
			date = input.getStartDate().plusDays(i);
		return date;
	}

	private Entry<?> prepareEntry(LocalTime startTime, LocalTime endTime, Entry<?> day, LocalDate date) {
		var entry = createEntry(date, startTime, endTime);
		if (startTime.isBefore(day.getStartTime()))
			entry.changeStartTime(day.getStartTime());
		if (endTime.isAfter(day.getEndTime()))
			entry.changeEndTime(day.getEndTime());
		return entry;
	}

	public List<Entry<?>> findAvailableTimeSlot(Entry<?> input, int duration, int before, int after) {
		var result = administrateEntries.getSpecificCalendarByIndex(0).findEntries(input.getStartDate(), input.getEndDate(), ZoneId.systemDefault()).values();
		List<Entry<?>> output = new ArrayList<Entry<?>>();
		long start = input.getStartMillis() + before * 60000;
		long end = input.getEndMillis() - after * 60000;
		long userStart = start;
		long userEnd = end;

		for (var entries : result)
			for (int i = 0; i <= entries.size(); i++) {
				end = manipulateEndForFindAvailableTimeSlotFunction(i, entries, end, userEnd);
				start = manipulateStartForFindAvailableTimeSlotFunction(i, start, entries);

				if (end < start)
					continue;
				output = createNewEntriesInRequirementsForFindAvailableTimeSlotFunction(end, start, duration, userStart, userEnd, output);
			}
		if (result.isEmpty())
			output.add(input);

		return output;
	}

	private List<Entry<?>> createNewEntriesInRequirementsForFindAvailableTimeSlotFunction(long end, long start,
			int duration, long userStart, long userEnd, List<Entry<?>> output) {
		if ((end - start) / 60000 >= duration && !((end - userStart) / 60000 <= duration || (userEnd - start) / 60000 < duration))
			output.add(createEntryFromMillis(start, end));
		if (checkForDuplicates(output))
			output.remove(output.size() - 1);
		return output;
	}

	private long manipulateStartForFindAvailableTimeSlotFunction(int i, long start, List<Entry<?>> entries) {
		if (i > 0)
			start = entries.get(i - 1).getEndMillis();
		return start;
	}

	private long manipulateEndForFindAvailableTimeSlotFunction(int i, List<Entry<?>> entries, long end, long userEnd) {
		if (i >= 0 && i < entries.size())
			end = entries.get(i).getStartMillis();
		if (i == entries.size())
			end = userEnd;
		return end;
	}

	private Entry<?> createEntry(LocalDate startAndEnd, LocalTime start, LocalTime end) {
		var entry = new Entry<String>();
		entry.changeStartTime(start);
		entry.changeEndTime(end);
		entry.changeStartDate(startAndEnd);
		entry.changeEndDate(startAndEnd);
		return entry;
	}

	private Entry<?> createEntryFromMillis(long start, long end) {
		var entry = new Entry<String>();
		var dateStart = LocalDateTime.ofInstant(Instant.ofEpochMilli(start), ZoneId.systemDefault());
		var dateEnd = LocalDateTime.ofInstant(Instant.ofEpochMilli(end), ZoneId.systemDefault());
		entry.changeStartTime(dateStart.toLocalTime());
		entry.changeStartDate(dateStart.toLocalDate());
		entry.changeEndTime(dateEnd.toLocalTime());
		entry.changeEndDate(dateEnd.toLocalDate());
		return entry;
	}

	private void reduceListLenght(List<Entry<?>> list, int maxNumberOfEntrys) {
		while (list.size() > maxNumberOfEntrys) {
			list.remove(list.size() - 1);
		}
	}

	private boolean checkForDuplicates(List<Entry<?>> currentEntries) {
		if (currentEntries.size() < 2)
			return false;
		return (currentEntries.get(currentEntries.size() - 2).getStartMillis() == currentEntries
				.get(currentEntries.size() - 1).getStartMillis());
	}
}
