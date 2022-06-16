package me.kooper.fbla.models;

import lombok.Getter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

@Getter
public class Place {

    // data that will be retrieved and stored from the JSON Object
    private final String NAME, STATE, FORMATTED, PLACEID;
    private final Object DATA;
    private final ArrayList<String> CATEGORIES = new ArrayList<>();

    // constructor that is given a json object from Place Connection and turns it into a class to be utilized easily
    public Place(Object o) {
        // store object in case needed for db
        DATA = o;

        // convert object to JSON
        JSONObject loc = new JSONObject(o.toString());

        // get properties
        JSONObject properties = new JSONObject(loc.getJSONObject("properties").toString());

        // get formatted address
        FORMATTED = properties.optString("formatted");

        // get name
        NAME = properties.optString("name");

        // get state
        STATE = properties.optString("state");

        // get placeID
        PLACEID = properties.optString("place_id");

        // get categories
        JSONArray cat = new JSONArray(properties.getJSONArray("categories"));
        for (Object c : cat) {
            CATEGORIES.add(c.toString());
        }
    }

}
