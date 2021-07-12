package com.altenheim.kalender.controller.logicController;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.altenheim.kalender.interfaces.IGoogleAPIController;
import com.altenheim.kalender.models.SerializableEntry;
import com.altenheim.kalender.models.SettingsModel;
import com.calendarfx.model.Entry;

import org.json.*;


public class GoogleAPIController implements IGoogleAPIController
{
    private static final String FINDPLACEQUERY= "https://maps.googleapis.com/maps/api/place/findplacefromtext/json?input=%s&inputtype=textquery&fields=place_id&key=%s";
    private static final String OPENINGHOURSQUERY = "https://maps.googleapis.com/maps/api/place/details/json?place_id=%s&fields=opening_hours&key=%s";
    private static final String FINDDESTINATIONSQUERY= "https://maps.googleapis.com/maps/api/distancematrix/json?origins=%s&destinations=%s&key=%s";

    private SettingsModel settings;

    public GoogleAPIController(SettingsModel settings)
    {
        this.settings = settings;
    }
    
    public String getOpeningHours(String locationSearchUserInput)
    {
        var security = new SecureAesController();
        var apiKey = security.decrypt(settings.getDecryptedPasswordHash(), "e]<J3Grct{~'HJv-", SettingsModel.APICYPHERTEXT);
        String input = locationSearchUserInput.replaceAll(" ", "%20");

        try 
        {
            var jsonResponse = makeHttpRequest(String.format(FINDPLACEQUERY, input, apiKey));
            var id = parseJsonForLocationId(jsonResponse);
            var jsonResponseDetail = makeHttpRequest(String.format(OPENINGHOURSQUERY, id, apiKey));
            System.out.println(parseJsonForOpeningHours2(jsonResponseDetail));
            return parseJsonForOpeningHours(jsonResponseDetail);          
        } 
        catch (IOException | InterruptedException e) 
        {
            e.printStackTrace();
            return "Ungültige\nOrtseingabe";
        }
        finally
        {
            apiKey = null;
            security = null;
        }
    }


    public int[] searchForDestinationDistance(String startAt, String destination)
    {
        var security = new SecureAesController();
        var apiKey = security.decrypt(settings.getDecryptedPasswordHash(), "e]<J3Grct{~'HJv-", SettingsModel.APICYPHERTEXT);

        int[] returnValues = new int[2];
        var start = startAt.replaceAll(" ", "%20");
        var end = destination.replaceAll(" ", "%20");
        try
        {            
            var jsonBody = makeHttpRequest(String.format(FINDDESTINATIONSQUERY, start, end, apiKey));
            var json = new JSONObject(jsonBody);
            var elements = json.getJSONArray("rows").getJSONObject(0).getJSONArray("elements").getJSONObject(0);        
            returnValues[0] = elements.getJSONObject("duration").getInt("value");        
            returnValues[1] = elements.getJSONObject("distance").getInt("value");            
        } 
        catch (IOException | InterruptedException e) 
        {
            e.printStackTrace();
            returnValues[0] = -1;
            returnValues[1] = -1;
        }
        finally
        {
            apiKey = null;
            security = null;
        }

        return returnValues;
    } 


    private String makeHttpRequest(String requestString) throws IOException, InterruptedException
    {
        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder().uri(URI.create(requestString)).build();
        var response = client.send(request, BodyHandlers.ofString());
        if (response.statusCode() == 200)  
            return response.body();   
        else
            return "";       
    }


    private String parseJsonForLocationId(String jsonBody)
    {
        if (jsonBody.isEmpty())
            return jsonBody;
        var json = new JSONObject(jsonBody).getJSONArray("candidates");        
        var locationId = new ArrayList<String>();

        for (int i = 0; i < json.length(); i++)
        {
            locationId.add(json.getJSONObject(i).getString("place_id"));    
        }
        return locationId.get(0);
    }
    

    private String parseJsonForOpeningHours(String jsonBody)
    {
        if (jsonBody.isEmpty())
            return jsonBody;
        String openingHours = "";
        var json = new JSONObject(jsonBody);
        var openingHoursJson = json.getJSONObject("result").getJSONObject("opening_hours");
        var weekdays = openingHoursJson.getJSONArray("weekday_text");       

        for (int i = 0; i < weekdays.length(); i++)
        {
            openingHours = openingHours + "\n" + weekdays.get(i).toString();             
        }
        return openingHours;
    }

    private HashMap<DayOfWeek, List<SerializableEntry>> parseJsonForOpeningHours2(String jsonBody)
    {
        if (jsonBody.isEmpty())
            return null;

        var openingHours = new HashMap<DayOfWeek, List<SerializableEntry>>();
        
        var json = new JSONObject(jsonBody);
        var openingHoursJson = json.getJSONObject("result").getJSONObject("opening_hours");
        var periods = openingHoursJson.getJSONArray("periods");
        

        for (var day : DayOfWeek.values()) 
        {
            var entryList = new ArrayList<SerializableEntry>();
            openingHours.put(day, entryList);
        }

        for (int i = 0; i < periods.length(); i++)
        {
            var close = ((JSONObject) periods.get(i)).getJSONObject("close");
            var closeDay = close.getInt("day");
            var closeTime = close.getString("time");
            if (closeTime.equals("0000")) {
                closeDay--; 
                closeTime = "2359";
                if (closeDay == -1) {
                    closeDay = 6;
                }
            }
            if (closeDay==0) 
                closeDay = 7;
            var open = ((JSONObject) periods.get(i)).getJSONObject("open");
            var openDay = open.getInt("day");
            var openTime = open.getString("time");
            if (openDay==0) 
                openDay = 7;

            if (openDay!=closeDay) {
                var dayList = openingHours.get(DayOfWeek.of(openDay));
                var entry = new SerializableEntry();
                entry.changeStartTime(LocalTime.of(Integer.valueOf(openTime.substring(0, 2)), Integer.valueOf(openTime.substring(2, 4))));
                entry.changeEndTime(LocalTime.of(23, 59, 59));
                dayList.add(entry);
                openingHours.replace(DayOfWeek.of(openDay), dayList);

                var day2List = openingHours.get(DayOfWeek.of(closeDay));
                var entry2 = new SerializableEntry();
                entry2.changeStartTime(LocalTime.of(0, 0));
                entry2.changeEndTime(LocalTime.of(Integer.valueOf(closeTime.substring(0, 2)), Integer.valueOf(closeTime.substring(2, 4))));
                day2List.add(entry2);
                openingHours.replace(DayOfWeek.of(closeDay), day2List);

            } else {
                var dayList = openingHours.get(DayOfWeek.of(openDay));
                var entry = new SerializableEntry();
                entry.changeStartTime(LocalTime.of(Integer.valueOf(openTime.substring(0, 2)), Integer.valueOf(openTime.substring(2, 4))));
                entry.changeEndTime(LocalTime.of(Integer.valueOf(closeTime.substring(0, 2)), Integer.valueOf(closeTime.substring(2, 4))));
                dayList.add(entry);
                openingHours.replace(DayOfWeek.of(openDay), dayList);
            }             
        }
        return openingHours;
    }
}
