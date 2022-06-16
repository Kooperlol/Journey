package me.kooper.fbla.api;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.Getter;
import me.kooper.fbla.models.Place;
import me.kooper.fbla.util.LogUtil;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

@Getter
public class PlaceAPI {

    // stores a places around an area in the PlaceModel class as an ArrayList
    private final ArrayList<Place> PLACES = new ArrayList<>();

    /* Constructor that calls api and gets all places around the specified location with the specified attributes
    Uses Geoapify (Places API) - https://www.geoapify.com/places-api */
    public PlaceAPI(String categories, String conditions, double lon, double lat, double radius, String limit) {
        // initialize environmental file to retrieve API key during request
        Dotenv dotenv = Dotenv.load();

        // build request to API from specified parameters
        LogUtil.LOGGER.info("Sending request to get places around " + lon + " " + lat + " in a radius of " + radius + " km that fits " + categories + " and " + conditions + " and returns a limit of " + limit + " places:");
        long duration = System.currentTimeMillis();
        OkHttpClient client = new OkHttpClient();
        try {
            HttpUrl url = Objects.requireNonNull(HttpUrl.parse("https://api.geoapify.com/v2/places")).newBuilder()
                    .addQueryParameter("categories", categories)
                    .addQueryParameter("conditions", conditions)
                    .addQueryParameter("limit", limit)
                    .addQueryParameter("filter", "circle:" + lon + "," + lat + "," + radius)
                    .addQueryParameter("bias", "proximity:" + lon + "," + lat)
                    .addQueryParameter("apiKey", dotenv.get("GEOAPIFY_API_KEY"))
                    .build();
            Request request = new Request.Builder().url(url).build();
            Response response = client.newCall(request).execute();

            LogUtil.LOGGER.info( "Request took: " + (System.currentTimeMillis() - duration) + " ms.");

            // response code 200 means that the response is successful otherwise throw an error
            if (response.code() == 200) {
                // get response as json object
                JSONObject json = new JSONObject(Objects.requireNonNull(response.body()).string());

                // iterate through each location in response
                for (Object object : json.getJSONArray("features")) {
                    // create a pojo from the json to easily access information
                    Place p = new Place(object);

                    /* assert the location is within Wisconsin and if the place has a name
                    then if both are true add to the places list */
                    if (p.getSTATE().equals("Wisconsin") && (!p.getNAME().equals(""))) {
                        PLACES.add(p);
                    }
                }
                Objects.requireNonNull(response.body()).close();
                LogUtil.LOGGER.info( "Successfully found and stored " + this.getPLACES().size() + " locations.");
            } else {
                LogUtil.LOGGER.severe( "Request error " + response.code());
            }
        } catch (Exception e) {
            LogUtil.LOGGER.severe( "Error: " + e);
        }
    }

}