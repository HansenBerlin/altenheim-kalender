package views;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;



public class OpeningHoursTestView 
{
    private final String FINDPLACEQUERY= "https://maps.googleapis.com/maps/api/place/findplacefromtext/json?input=%s&inputtype=textquery&fields=place_id&key=AIzaSyCjdv5ViLvdzCNTKXB2FZ-fhSkI_0OUZ9w";
    private final String OPENINGHOURSQUERY = "https://maps.googleapis.com/maps/api/place/details/json?place_id=ChIJM2yt-mBOqEcRsCel8UFtMeI&fields=opening_hours&key=AIzaSyCjdv5ViLvdzCNTKXB2FZ-fhSkI_0OUZ9w";

    public OpeningHoursTestView()
    {

    }

    public void userInputSearchQuery() throws IOException, InterruptedException
    {
        var reader = new BufferedReader(new InputStreamReader(System.in)); 
        String userInput = reader.readLine();        
        String input = userInput.replaceAll(" ", "%20");
        String searchQuery = String.format(FINDPLACEQUERY, input);
        System.out.println(searchQuery);
        httpRequest("https://maps.googleapis.com/maps/api/place/findplacefromtext/json?input=casablanca%20berlin%20rigaer%20strae&inputtype=textquery&fields=place_id&key=AIzaSyCjdv5ViLvdzCNTKXB2FZ-fhSkI_0OUZ9w");
    }

    public void httpRequest(String requestString) throws IOException, InterruptedException
    {
        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder().uri(URI.create(requestString)).build();
        var response = client.send(request, BodyHandlers.ofString());
        //if (response.statusCode() == 200)
        

            
    }


        
    
}
