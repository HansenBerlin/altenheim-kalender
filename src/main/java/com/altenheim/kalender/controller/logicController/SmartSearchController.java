package com.altenheim.kalender.controller.logicController;

import java.time.DayOfWeek;
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
import java.util.HashMap;
import java.util.LinkedList;

import com.altenheim.kalender.models.SerializableEntry;

public class SmartSearchController implements ISmartSearchController {
	private ICalendarEntriesModel administrateEntries;

	public SmartSearchController(ICalendarEntriesModel administrateEntries) {
		this.administrateEntries = administrateEntries;
	}

	/*Die Methode ist zum Aufruf der Suchlogik aus dem SearchViewController

	Der Methode wird übergeben:
		input: Der Entry beinhaltet das Startdatum, die Startuhrzeit und die Enduhrzeit für die Terminsuche
		duration: Hier wird die Länge des gesuchten Termins in Minuten übergeben.
		weekdays: Hier wird ein boolean Array der Länge 7 übergeben, dabei ist die erste Position Montag und die letzte Sonntag.
				  (true == Tag darf belegt werden / false == Tag darf nicht belegt werden)
		openingHours: Hier wird eine Liste der Länge 7 übergeben, die die Wochentage repräsentiert.
					  Für jeden Wochentag beinhaltet die Liste eine weitere Liste, die alle Öffnungzeiten des Tages (in Entrys mit Start- u. EndTime gespeichert) enthält.
		timeBefore: Hier wird die Zeit in Minuten übergeben, die vor dem Termin frei sein muss.
		timeAfter: Hier wird die Zeit in Minuten übergeben, die nach dem Termin frei sein muss.
		maxNumberOfReturnEntrys: Hier wird die maximale Anzahl an gewünschten Zeiträumen übergeben. 
								 (Bei Intervallverarbeitung wird hier die Anzahl der Intervalle übergeben)
		intervalDays: Hier wird die Zeit in Tagen übergeben, die mindestens zwischen zwei Terminen aufeinanderfolgenden Terminen in der Intervallverarbeitung liegen soll.
					  (Zur Deaktivierung der Intervallsuche wird hier 0 übergeben)

	Die Methode gibt zurück:
		Liste mit den Zeiträumen für mögliche Einträge
	*/
	public ArrayList<SerializableEntry> findPossibleTimeSlots(SerializableEntry input, int duration, boolean[] allowedWeekdays, 
		HashMap<DayOfWeek, List<SerializableEntry>> openingHours, int timeBefore, int timeAfter, int intervalDays){
		
		var output = new ArrayList<SerializableEntry>(); 
		var startTime = input.getStartTime();
		var endTime = input.getEndTime();
		var date = input.getStartDate();

		int i = -1;
		while (date.isBefore(input.getEndDate()) && output.size() < 1000) {
			i++;	
			
			int index = date.getDayOfWeek().getValue()-1;
			if(allowedWeekdays[index] == false)	
			{
				date = date.plusDays(1);
				continue;			
			}		
				
			if (openingHours.get(DayOfWeek.of((i%7)+1)) == null)
			{
				var entry = createEntry(date, startTime, endTime);
				output.addAll(findAvailableTimeSlot(entry, duration, timeBefore, timeAfter));
				date = date.plusDays(intervalDays);							
			}
			else
			{
				for (var day : openingHours.get(DayOfWeek.of((i%7)+1)))
				{				
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

	public ArrayList<SerializableEntry> findAvailableTimeSlot(SerializableEntry input, int duration, int before,
			int after) {
		var result = new LinkedList<List<Entry<?>>>();
		for (var calendar : administrateEntries.getAllCalendarsSelectedByUser()) {
			result.addAll(
					calendar.findEntries(input.getStartDate(), input.getEndDate(), ZoneId.systemDefault()).values());
		}
		var output = new ArrayList<SerializableEntry>();

		long start = input.getStartMillis() + before * 60000;
		long end = input.getEndMillis() - after * 60000;
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
					output.add(createEntryFromMillis(start, end));
				if (checkForDuplicates(output))
					output.remove(output.size() - 1);
			}

		if (result.isEmpty())
			output.add(input);

		return output;
	}

	private SerializableEntry createEntry(LocalDate startAndEnd, LocalTime start, LocalTime end) {
		var entry = new SerializableEntry();
		entry.changeStartTime(start);
		entry.changeEndTime(end);
		entry.changeStartDate(startAndEnd);
		entry.changeEndDate(startAndEnd);
		return entry;
	}

	private SerializableEntry createEntryFromMillis(long start, long end) {
		var entry = new SerializableEntry();
		var dateStart = LocalDateTime.ofInstant(Instant.ofEpochMilli(start), ZoneId.systemDefault());
		var dateEnd = LocalDateTime.ofInstant(Instant.ofEpochMilli(end), ZoneId.systemDefault());
		entry.changeStartTime(dateStart.toLocalTime());
		entry.changeStartDate(dateStart.toLocalDate());
		entry.changeEndTime(dateEnd.toLocalTime());
		entry.changeEndDate(dateEnd.toLocalDate());
		return entry;
	}

	private void reduceListLenght(ArrayList<SerializableEntry> list, int maxNumberOfEntrys) {
		while (list.size() > maxNumberOfEntrys) {
			list.remove(list.size() - 1);
		}
	}

	private boolean checkForDuplicates(ArrayList<SerializableEntry> currentEntries) {
		if (currentEntries.size() < 2)
			return false;
		return (currentEntries.get(currentEntries.size() - 2).getStartMillis() == currentEntries
				.get(currentEntries.size() - 1).getStartMillis());
	}
	
}
