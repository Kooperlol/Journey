package me.kooper.fbla.models;

import lombok.Getter;
import org.json.JSONArray;
import org.json.JSONObject;

@Getter
public class Location {

    // data that will be retrieved and stored from the JSON Object
    private final double LON, LAT;
    private final String STATE;

    // constructor that is given the json object from LocationConnection and turns it into a class to be utilized easily
    public Location(JSONObject data) {
        // features array
        JSONArray features = data.getJSONArray("features");

        // get first location
        JSONObject first = features.getJSONObject(0);

        // set lon & lat from geo
        JSONArray coordinates = first.getJSONObject("geometry").getJSONArray("coordinates");
        LON = coordinates.optDouble(0);
        LAT = coordinates.optDouble(1);

        // get properties
        JSONObject properties = first.getJSONObject("properties");

        // get state from properties
        STATE = properties.optString("state");
    }

}
