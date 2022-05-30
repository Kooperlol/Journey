package me.kooper.fbla.api.place;

import lombok.Getter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

@Getter
public class PlaceModel {

    // data that will be retrieved and stored from the JSON Object
    private final String name, state, formatted, placeID;
    private final Object data;
    private final ArrayList<String> categories = new ArrayList<>();

    // constructor that is given a json object from Place Connection and turns it into a class to be utilized easily
    public PlaceModel(Object o) {
        // store object in case needed for db
        data = o;

        // convert object to JSON
        JSONObject loc = new JSONObject(o.toString());

        // get properties
        JSONObject properties = new JSONObject(loc.getJSONObject("properties").toString());

        // get formatted address
        formatted = properties.optString("formatted");

        // get name
        name = properties.optString("name");

        // get state
        state = properties.optString("state");

        // get placeID
        placeID = properties.optString("place_id");

        // get categories
        JSONArray cat = new JSONArray(properties.getJSONArray("categories"));
        for (Object c : cat) {
            categories.add(c.toString());
        }
    }

}
