package me.kooper.fbla.models;

import lombok.Getter;
import org.json.JSONArray;
import org.json.JSONObject;

@Getter
public class Location {

    // data that will be retrieved and stored from the JSON Object
    private final double lon, lat;
    private final String state;

    // constructor that is given the json object from LocationConnection and turns it into a class to be utilized easily
    public Location(JSONObject data) {
        // features array
        JSONArray features = new JSONArray(data.getJSONArray("features").toString());

        // get first location
        JSONObject first = new JSONObject(features.get(0).toString());

        // geometry properties
        JSONObject geo = new JSONObject(first.getJSONObject("geometry").toString());

        // set lon & lat from geo
        JSONArray coordinates = new JSONArray(geo.getJSONArray("coordinates").toString());
        lon = coordinates.optDouble(0);
        lat = coordinates.optDouble(1);

        // get properties
        JSONObject properties = new JSONObject(first.getJSONObject("properties").toString());

        // get state from properties
        state = properties.optString("state");
    }

}
