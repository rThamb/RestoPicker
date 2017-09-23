package assigment.dawson.restocoderenation.loaders;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;

import assigment.dawson.restocoderenation.R;
import assigment.dawson.restocoderenation.beans.Restaurant;

/**
 * The resent class is reponsible for interaction with Zomato database
 * @author Renuchan
 * @since 12/2/2016.
 */

public class ZomatoRestoList {

    private Activity activity;
    private NetworkInfo netInfo;
    private NetworkInfo netMobileInfo;
    private ConnectivityManager cm;
    private String zamaTag;

    private final String DEBUGTAG = "ZomatoRestoList";

    /**
     * Constructor with one parameter
     * @param act
     */
    public ZomatoRestoList(Activity act)
    {
        activity = act;
        cm = (ConnectivityManager) act.getSystemService(act.CONNECTIVITY_SERVICE);
        netInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        netMobileInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        zamaTag = act.getString(R.string.zamaTag);
    }


    /**
     * This method returns the list of nearby restaurants by querying the db
     * @param longitude
     * @param latitude
     * @return
     */
    public ArrayList<Restaurant> getRestos(double longitude, double latitude)
    {
        if(checkNetwork())
        {

            HttpURLConnection con = makeRequest(longitude,latitude);

            if(con != null)
            {
                return checkReponse(con);
            }

            Log.d(DEBUGTAG, "Network is available");
        }
        else
            Log.d(DEBUGTAG, "Network is not available");

        return null;
    }


    /**
     * Check if the network is available
     * @return boolean
     */
    private boolean checkNetwork()
    {
        if(netInfo.isAvailable() && netInfo.isConnected())
            return true;
        else if (netMobileInfo.isAvailable() && netMobileInfo.isConnected())
            return true;
        else
            return false;

    }

    /**
     * Creates GET request querying Zomato db
     * @param longitude
     * @param latitude
     * @return
     */
    private HttpURLConnection makeRequest(double longitude, double latitude)
    {
        String latValue = String.valueOf(latitude);
        String longValue = String.valueOf(longitude);

        try {

            String requestUrl = "https://developers.zomato.com/api/v2.1/geocode?lat=" +
                    URLEncoder.encode(latValue,"UTF-8") + "&lon=" + URLEncoder.encode(longValue,"UTF-8");

            URL url = new URL(requestUrl);
            //set up Connection
            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();

            //setup header packet
            httpCon.setRequestMethod("GET");
            httpCon.setRequestProperty("user-key", "079150e10bcd31cdd8d30928531c4cc2");

            httpCon.connect();

            return httpCon;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Reads response from Zomato database if any and converts in the arraylist of Restaurants
     * @param con
     * @return ArrayList of Restaurant objects
     */
    private ArrayList<Restaurant> checkReponse(HttpURLConnection con)
    {
        String jsonResponse;
        try {
            if (con.getResponseCode() == 200) {

                Log.d(DEBUGTAG, "Response was good");

                jsonResponse = readInputStream(con.getInputStream());

                return parseJSON(jsonResponse);

            } else
                Log.d(DEBUGTAG, "Response was a fail");
        }catch(IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Converts inputStriim to string
     * @param in
     * @return
     */
    private String readInputStream(InputStream in)
    {
        StringBuilder sb = new StringBuilder("");

        try {

            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String text = br.readLine();

            while (text != null)
            {
                sb.append(text);
                text = br.readLine();
            }

        }catch(IOException e)
        {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * Parses string json encoded objects into arraylist of Restaurant objects
     * @param json
     * @return
     */
    private ArrayList<Restaurant> parseJSON(String json)
    {

        Log.d(DEBUGTAG, "parsing");
        ArrayList<Restaurant> list = new ArrayList<Restaurant>();
        try {
            JSONObject jObj = new JSONObject(json);

            JSONArray nearRestos = jObj.getJSONArray("nearby_restaurants");

            //get the restos and add them
            for(int i = 0; i < nearRestos.length(); i++) {
                list.add(readOneRestoInfo(nearRestos.getJSONObject(i)));
            }

            Log.d(DEBUGTAG, list.toString());
            return list;

        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Reads one JSON encoded at a time and converts it into Restaurant object
     * @param resto
     * @return Restaurant object
     */
    private Restaurant readOneRestoInfo(JSONObject resto)
    {
        Restaurant rest = new Restaurant();

        try {
            JSONObject restoInfo = resto.getJSONObject("restaurant");
            rest.setName(restoInfo.getString("name"));
            rest.setPriceRange(restoInfo.getInt("price_range"));
            rest.setGenre(restoInfo.getString("cuisines"));


            //not all restos have phone numbers
            try {
                rest.setTelephone(restoInfo.getString("phone_numbers"));
            }
            catch (Exception e)
            {
                rest.setTelephone("");
            }

            JSONObject rating = restoInfo.getJSONObject("user_rating");
            double ratingNum = rating.getDouble("aggregate_rating");
            rest.setRating((int)ratingNum);

            JSONObject location = restoInfo.getJSONObject("location");

            String[] fullAddress = location.getString("address").split(",");
            rest.setAddress(fullAddress[0]);
            rest.setPostalCode(location.getString("zipcode"));
            rest.setCity(location.getString("city"));
            rest.setLatitude(location.getDouble("latitude"));
            rest.setLongitude(location.getDouble("longitude"));

            rest.setNotes(zamaTag);
            rest.setDateCreated(new Date().toString());

            Log.d(DEBUGTAG, "added a restaurant" + rest.toString());

            return rest;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return rest;
    }
}