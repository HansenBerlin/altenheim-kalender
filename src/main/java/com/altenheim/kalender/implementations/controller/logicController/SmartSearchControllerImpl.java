package com.altenheim.kalender.implementations.controller.logicController;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import com.altenheim.kalender.interfaces.factorys.EntryFactory;
import com.altenheim.kalender.interfaces.viewController.CalendarEntriesController;
import com.altenheim.kalender.interfaces.logicController.SmartSearchController;
import com.calendarfx.model.Entry;
import java.util.HashMap;
import java.util.LinkedList;


public record SmartSearchControllerImpl(
		CalendarEntriesController administrateEntries,
		EntryFactory entryFactory) implements SmartSearchController
{

	public ArrayList<Entry<String>> findPossibleTimeSlots(Entry<String> input, int duration, boolean[] allowedWeekdays,
														  HashMap<DayOfWeek, List<Entry<String>>> openingHours, int timeBefore, int timeAfter, int intervalDays) {

		var output = new ArrayList<Entry<String>>();
		var startTime = input.getStartTime();
		var endTime = input.getEndTime();
		var date = input.getStartDate();

		int i = -1;
		while (date.isBefore(input.getEndDate()) && output.size() < 1000) {
			i++;

			int index = date.getDayOfWeek().getValue() - 1;
			if (!allowedWeekdays[index]) {
				date = date.plusDays(1);
				continue;
			}

			if (openingHours.get(DayOfWeek.of((i % 7) + 1)) == null) {
				var entry = createEntry(date, startTime, endTime);
				output.addAll(findAvailableTimeSlot(entry, duration, timeBefore, timeAfter));
				date = date.plusDays(intervalDays);
			} else {
				for (var day : openingHours.get(DayOfWeek.of((i % 7) + 1))) {
					var entry = createEntry(date, startTime, endTime);
					if (endTime.isBefore(day.getStartTime()) || startTime.isAfter(day.getEndTime()))
						continue;
					if (startTime.isBefore(day.getStartTime()))
						entry.changeStartTime(day.getStartTime());
					if (endTime.isAfter(day.getEndTime()))
						entry.changeEndTime(day.getEndTime());
					output.addAll(findAvailableTimeSlot(entry, duration, timeBefore, timeAfter));
					date = date.plusDays(intervalDays);
				}
			}
		}
		return output;
	}

	private ArrayList<Entry<String>> findAvailableTimeSlot(Entry<String> input, int duration, int before, int after) {
		var result = new LinkedList<List<Entry<?>>>();
		for (var calendar : administrateEntries.getAllCalendarsSelectedByUser()) {
			result.addAll(calendar.findEntries(input.getStartDate(), input.getEndDate(), ZoneId.systemDefault()).values());
		}
		var output = new ArrayList<Entry<String>>();

		long start = input.getStartMillis() + before * 60000L;
		long end = input.getEndMillis() - after * 60000L;
		long userStart = start;
		long userEnd = end;

		for (var entries : result)
			for (int i = 0; i <= entries.size(); i++) {
				if (i >= 0 && i < entries.size())
					end = entries.get(i).getStartMillis();
				if (i > 0)
					start = entries.get(i - 1).getEndMillis();
				if (i == entries.size())
					end = userEnd;
				if (end < start)
					continue;
				if ((end - start) / 60000 >= duration
						&& !((end - userStart) / 60000 <= duration || (userEnd - start) / 60000 < duration))
					output.add(entryFactory.createCalendarFXEntryFromMillis(start, end));
				if (checkForDuplicates(output))
					output.remove(output.size() - 1);
			}

		if (result.isEmpty())
			output.add(input);

		return output;
	}

	private Entry<String> createEntry(LocalDate startAndEnd, LocalTime start, LocalTime end) {
		var entry = new Entry<String>();
		entry.changeStartTime(start);
		entry.changeEndTime(end);
		entry.changeStartDate(startAndEnd);
		entry.changeEndDate(startAndEnd);
		return entry;
	}

	private boolean checkForDuplicates(ArrayList<Entry<String>> currentEntries) {
		if (currentEntries.size() < 2)
			return false;
		return (currentEntries.get(currentEntries.size() - 2).getStartMillis() == currentEntries
				.get(currentEntries.size() - 1).getStartMillis());
	}
}