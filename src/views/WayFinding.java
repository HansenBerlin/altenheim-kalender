package views;

import static org.junit.Assert.assertFalse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;
import org.json.*;

public class WayFinding 
{
    private final String FINDPLACEQUERY= "https://maps.googleapis.com/maps/api/distancematrix/json?origins=%s&destinations=%s&key=AIzaSyCjdv5ViLvdzCNTKXB2FZ-fhSkI_0OUZ9w";
    


    public void userInputSearchQuery() throws IOException, InterruptedException
    {
        var reader = new BufferedReader(new InputStreamReader(System.in)); 
        System.out.println("Bitte genaue Ortsangabe machen (Gesch‰ft, Ort, Straﬂe, Stadt, Land)");
        String userInput = reader.readLine();    
        System.out.println("Bitte genaue Ortsangabe machen (Gesch‰ft, Ort, Straﬂe, Stadt, Land)");
        String userInput2 = reader.readLine(); 
        
        String input = userInput.replaceAll(" ", "%20");
        String input2 = userInput2.replaceAll(" ", "%20");
        String searchQuery = String.format(FINDPLACEQUERY, input, input2);

        
        
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
        var json = new JSONObject(jsonBody);
        var elements = json.getJSONArray("rows").getJSONObject(0).getJSONArray("elements").getJSONObject(0);
        
        int duration = elements.getJSONObject("duration").getInt("value");
        System.out.println(duration+"s");
        
        int distance = elements.getJSONObject("distance").getInt("value");
        System.out.println(distance+"m");
        
        

       return "";
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
