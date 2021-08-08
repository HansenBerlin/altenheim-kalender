package com.altenheim.kalender.implementations.controller.logicController;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.altenheim.kalender.interfaces.logicController.JsonParser;
import com.calendarfx.model.Entry;
import org.json.JSONObject;

public class JsonParserImpl implements JsonParser 
{
    public String parseJsonForLocationId(String jsonBody) 
    {
        if (jsonBody.isEmpty())
            return jsonBody;
        var json = new JSONObject(jsonBody).getJSONArray("candidates");
        var locationId = new ArrayList<String>();

        for (int i = 0; i < json.length(); i++)
            locationId.add(json.getJSONObject(i).getString("place_id"));
        
        return locationId.get(0);
    }

    public HashMap<DayOfWeek, List<Entry<String>>> parseJsonForOpeningHours(String jsonBody) 
    {
        if (jsonBody.isEmpty())
            return null;

        var openingHours = new HashMap<DayOfWeek, List<Entry<String>>>();

        var json = new JSONObject(jsonBody);
        var openingHoursJson = json.getJSONObject("result").getJSONObject("opening_hours");
        var periods = openingHoursJson.getJSONArray("periods");

        for (var day : DayOfWeek.values()) 
        {
            var entryList = new ArrayList<Entry<String>>();
            openingHours.put(day, entryList);
        }

        for (int i = 0; i < periods.length(); i++) 
        {
            var close = ((JSONObject) periods.get(i)).getJSONObject("close");
            var closeDay = close.getInt("day");
            var closeTime = close.getString("time");
            if (closeTime.equals("0000")) 
            {
                closeDay--;
                closeTime = "2359";
                if (closeDay == -1) 
                    closeDay = 6;
            }
            if (closeDay == 0)
                closeDay = 7;
            var open = ((JSONObject) periods.get(i)).getJSONObject("open");
            var openDay = open.getInt("day");
            var openTime = open.getString("time");
            if (openDay == 0)
                openDay = 7;

            if (openDay != closeDay) 
            {
                var dayList = openingHours.get(DayOfWeek.of(openDay));
                var entry = new Entry<String>();
                entry.changeStartTime(LocalTime.of(Integer.parseInt(openTime.substring(0, 2)),
                        Integer.parseInt(openTime.substring(2, 4))));
                entry.changeEndTime(LocalTime.of(23, 59, 59));
                dayList.add(entry);
                openingHours.replace(DayOfWeek.of(openDay), dayList);

                var day2List = openingHours.get(DayOfWeek.of(closeDay));
                var entry2 = new Entry<String>();
                entry2.changeStartTime(LocalTime.of(0, 0));
                entry2.changeEndTime(LocalTime.of(Integer.parseInt(closeTime.substring(0, 2)),
                        Integer.parseInt(closeTime.substring(2, 4))));
                day2List.add(entry2);
                openingHours.replace(DayOfWeek.of(closeDay), day2List);

            } 
            else 
            {
                var dayList = openingHours.get(DayOfWeek.of(openDay));
                var entry = new Entry<String>();
                entry.changeStartTime(LocalTime.of(Integer.parseInt(openTime.substring(0, 2)),
                        Integer.parseInt(openTime.substring(2, 4))));
                entry.changeEndTime(LocalTime.of(Integer.parseInt(closeTime.substring(0, 2)),
                        Integer.parseInt(closeTime.substring(2, 4))));
                dayList.add(entry);
                openingHours.replace(DayOfWeek.of(openDay), dayList);
            }
        }
        return openingHours;
    }
    
}
