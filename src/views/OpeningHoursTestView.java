package views;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;
import org.json.*;

public class OpeningHoursTestView 
{
    private final String FINDPLACEQUERY= "https://maps.googleapis.com/maps/api/place/findplacefromtext/json?input=%s&inputtype=textquery&fields=place_id&key=AIzaSyCjdv5ViLvdzCNTKXB2FZ-fhSkI_0OUZ9w";
    private final String OPENINGHOURSQUERY = "https://maps.googleapis.com/maps/api/place/details/json?place_id=%s&fields=opening_hours&key=AIzaSyCjdv5ViLvdzCNTKXB2FZ-fhSkI_0OUZ9w";

    public OpeningHoursTestView()
    {
    }

    public void userInputSearchQuery() throws IOException, InterruptedException
    {
        var reader = new BufferedReader(new InputStreamReader(System.in)); 
        System.out.println("Bitte genaue Ortsangabe machen (Geschäft, Ort, Straße, Stadt, Land)");
        String userInput = reader.readLine();     
        String input = userInput.replaceAll(" ", "%20");
        String searchQuery = String.format(FINDPLACEQUERY, input);

        var jsonResponse = httpRequest(searchQuery);
        var id = parseJsonForLocationId(jsonResponse);        
        var jsonResponse2 = httpRequest(String.format(OPENINGHOURSQUERY, id));
        parseJsonForOpeningHours(jsonResponse2);
    }

    public String httpRequest(String requestString) throws IOException, InterruptedException
    {
        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder().uri(URI.create(requestString)).build();
        var response = client.send(request, BodyHandlers.ofString());
        if (response.statusCode() == 200)  
            return response.body();   
        else
            return "";       
    }

    public String parseJsonForLocationId(String jsonBody)
    {
        var json = new JSONObject(jsonBody).getJSONArray("candidates");        
        var locationId = new ArrayList<String>();

        for (int i = 0; i < json.length(); i++)
        {
            locationId.add(json.getJSONObject(i).getString("place_id"));    
        }
        return locationId.get(0);
    }

    public void parseJsonForOpeningHours(String jsonBody)
    {
        JSONObject json = new JSONObject(jsonBody);
        var openingHours = json.getJSONObject("result").getJSONObject("opening_hours").getJSONArray("weekday_text");       

        for (int i = 0; i < openingHours.length(); i++)
        {
            var data = openingHours.get(i);
            System.out.println(data);   
        }
    }
}
