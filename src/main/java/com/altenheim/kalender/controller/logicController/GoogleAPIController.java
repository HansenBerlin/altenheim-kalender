package com.altenheim.kalender.controller.logicController;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;

import com.altenheim.kalender.interfaces.IGoogleAPIController;

import org.json.*;


public class GoogleAPIController implements IGoogleAPIController
{
    private static String FINDPLACEQUERY= "https://maps.googleapis.com/maps/api/place/findplacefromtext/json?input=%s&inputtype=textquery&fields=place_id&key=%s";
    private static String OPENINGHOURSQUERY = "https://maps.googleapis.com/maps/api/place/details/json?place_id=%s&fields=opening_hours&key=%s";
    private static String FINDDESTINATIONSQUERY= "https://maps.googleapis.com/maps/api/distancematrix/json?origins=%s&destinations=%s&key=%s";

    
    public String showOpeningHours(String locationSearchUserInput)
    {          
        String input = locationSearchUserInput.replaceAll(" ", "%20");
        String searchQuery = String.format(FINDPLACEQUERY, input);        

        try 
        {
            var jsonResponse = httpRequest(searchQuery);
            var id = parseJsonForLocationId(jsonResponse);        
            var jsonResponseDetail = httpRequest(String.format(OPENINGHOURSQUERY, id)); 
            return parseJsonForOpeningHours(jsonResponseDetail);           
        } 
        catch (IOException | InterruptedException e) 
        {
            e.printStackTrace();
            return "Ungültige\nOrtseingabe";
        }       
    }


    public int[] searchForDestinationDistance(String startAt, String destination)
    {
        int[] returnValues = new int[2];
        var start = startAt.replaceAll(" ", "%20");
        var end = destination.replaceAll(" ", "%20");
        String searchQuery = String.format(FINDDESTINATIONSQUERY, start, end);
        try 
        {            
            var jsonBody = httpRequest(searchQuery);
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

        return returnValues;
    } 


    private String httpRequest(String requestString) throws IOException, InterruptedException
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
}
