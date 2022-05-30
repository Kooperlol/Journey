package me.kooper.fbla.api.location;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.Getter;
import me.kooper.fbla.util.LogUtil;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;

import java.util.Objects;
import java.util.logging.Level;

@Getter
public class LocationConnection {

    // data retrieved from api stored as JSON Object
    private JSONObject data;

    /* Constructor that gets basic attributes as JSON object from a city provided as a string
     Uses Geoapify (Geocoding API) - https://www.geoapify.com/geocoding-api */
    public LocationConnection(String city) {
        // initialize environmental file to retrieve API key during request
        Dotenv dotenv = Dotenv.load();

        // send request to API
        LogUtil.getLogger().log(Level.INFO, "Sending request to get information about " + city + ": ");
        long duration = System.currentTimeMillis();
        OkHttpClient client = new OkHttpClient();
        try {
            HttpUrl url = Objects.requireNonNull(HttpUrl.parse("https://api.geoapify.com/v1/geocode/search?")).newBuilder()
                    .addQueryParameter("city", city)
                    .addQueryParameter("country", "United States of America")
                    .addQueryParameter("state", "Wisconsin")
                    .addQueryParameter("apiKey", dotenv.get("GEOAPIFY_API_KEY"))
                    .build();
            Request request = new Request.Builder().url(url).build();
            Response response = client.newCall(request).execute();

            LogUtil.getLogger().log(Level.INFO, "Request took: " + (System.currentTimeMillis() - duration) + " ms.");

            // response code 200 means that the response is successful otherwise throw an error
            if (response.code() == 200) {
                data = new JSONObject(Objects.requireNonNull(response.body()).string());
                LogUtil.getLogger().log(Level.INFO, "Successfully got data from place and stored it.");
                Objects.requireNonNull(response.body()).close();
            } else {
                LogUtil.getLogger().log(Level.SEVERE, "Request error " + response.code());
            }
        } catch (Exception e) {
            LogUtil.getLogger().log(Level.SEVERE, "Error: ", e);
        }
    }

}
