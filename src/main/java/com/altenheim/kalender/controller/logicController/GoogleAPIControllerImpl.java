package com.altenheim.kalender.controller.logicController;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.List;
import com.altenheim.kalender.interfaces.GoogleAPIController;
import com.altenheim.kalender.interfaces.logicController.EncryptionController;
import com.altenheim.kalender.models.SettingsModelImpl;
import com.calendarfx.model.Entry;

import org.json.*;

public class GoogleAPIControllerImpl implements GoogleAPIController {
    private static final String FINDPLACEQUERY = "https://maps.googleapis.com/maps/api/place/findplacefromtext/json?input=%s&inputtype=textquery&fields=place_id&key=%s";
    private static final String OPENINGHOURSQUERY = "https://maps.googleapis.com/maps/api/place/details/json?place_id=%s&fields=opening_hours&key=%s";
    private static final String FINDDESTINATIONSQUERY = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=%s&destinations=%s&key=%s";
    private static final String SALT = "e]<J3Grct{~'HJv-";

    private JsonParserImpl jsonParserImpl;
    private EncryptionController encryptionController;

    public GoogleAPIControllerImpl(JsonParserImpl jsonParserImpl, EncryptionController encryptionController)
    {
        this.jsonParserImpl = jsonParserImpl;
        this.encryptionController = encryptionController;
    }

    public HashMap<DayOfWeek, List<Entry<String>>> getOpeningHours(String locationSearchUserInput) {
        var security = new EncryptionControllerImpl();
        var apiKey = security.decrypt(SettingsModelImpl.decryptedPassword, SALT, SettingsModelImpl.APICYPHERTEXT);
        String input = locationSearchUserInput.replace(" ", "%20");

        try {
            var jsonResponse = makeHttpRequest(String.format(FINDPLACEQUERY, input, apiKey));
            var id = jsonParserImpl.parseJsonForLocationId(jsonResponse);
            var jsonResponseDetail = makeHttpRequest(String.format(OPENINGHOURSQUERY, id, apiKey));
            return jsonParserImpl.parseJsonForOpeningHours(jsonResponseDetail);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            // ungültige Ortsangabe
            return null;
        }
    }

    public int[] searchForDestinationDistance(String startAt, String destination) {

        var apiKey = encryptionController.decrypt(SettingsModelImpl.decryptedPassword, SALT, SettingsModelImpl.APICYPHERTEXT);

        int[] returnValues = new int[2];
        var start = startAt.replace(" ", "%20");
        var end = destination.replace(" ", "%20");
        try {
            var jsonBody = makeHttpRequest(String.format(FINDDESTINATIONSQUERY, start, end, apiKey));
            var json = new JSONObject(jsonBody);
            var elements = json.getJSONArray("rows").getJSONObject(0).getJSONArray("elements").getJSONObject(0);
            returnValues[0] = elements.getJSONObject("duration").getInt("value");
            returnValues[1] = elements.getJSONObject("distance").getInt("value");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            returnValues[0] = -1;
            returnValues[1] = -1;
        }
        return returnValues;
    }

    public int[] searchForDestinationDistance(String origin, String destination, String travelMode) {
        var apiKey = encryptionController.decrypt(SettingsModelImpl.decryptedPassword, SALT, SettingsModelImpl.APICYPHERTEXT);

        int[] returnValues = new int[2];
        var start = origin.replace(" ", "%20");
        var end = destination.replace(" ", "%20");

        String furtherAttributes = "&mode=";
        switch (travelMode) {
            case "Auto":
                furtherAttributes += "driving";
                break;
            case "Öffis":
                furtherAttributes += "transit";
                break;
            case "Fahrrad":
                furtherAttributes += "bicycling";
                break;
            case "Fußgänger":
                furtherAttributes += "walking";
                break;
            default:
                furtherAttributes = "";
                break;
        }

        try {
            var jsonBody = makeHttpRequest(
                    String.format(FINDDESTINATIONSQUERY, start, end, apiKey) + furtherAttributes);
            var json = new JSONObject(jsonBody);
            var elements = json.getJSONArray("rows").getJSONObject(0).getJSONArray("elements").getJSONObject(0);
            returnValues[0] = elements.getJSONObject("duration").getInt("value");
            returnValues[1] = elements.getJSONObject("distance").getInt("value");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            returnValues[0] = -1;
            returnValues[1] = -1;
        }
        return returnValues;
    }

    private String makeHttpRequest(String requestString) throws IOException, InterruptedException {
        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder().uri(URI.create(requestString)).build();
        var response = client.send(request, BodyHandlers.ofString());
        if (response.statusCode() == 200)
            return response.body();
        else
            return "";
    }

}
