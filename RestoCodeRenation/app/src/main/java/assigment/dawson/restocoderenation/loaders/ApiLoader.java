package assigment.dawson.restocoderenation.loaders;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;

import assigment.dawson.restocoderenation.R;
import assigment.dawson.restocoderenation.beans.Restaurant;
import assigment.dawson.restocoderenation.beans.Review;

/**
 * The present class is used to perform db GET operations with Heroku
 * @author Renuchan
 * @since 12/7/2016.
 */

public class ApiLoader {

    private Activity activity;
    private NetworkInfo netInfo;
    private NetworkInfo netMobileInfo;
    private ConnectivityManager cm;
    private String heroTag;

    private final String DEBUGTAG = "ApiLoader";

    /**
     * Constructor uese Activity as a context
     * @param act
     */
    public ApiLoader(Activity act)
    {
        activity = act;
        cm = (ConnectivityManager) act.getSystemService(act.CONNECTIVITY_SERVICE);
        netInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        netMobileInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        heroTag = act.getString(R.string.heroTag);
    }

    /**
     * THe resent method checks whether network connection is available
     * @return true if available
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

    //--------------- Methods used to get Restos

    /**
     * Sends GET request and receives JSON encoded object(s) as a response
     * @param longitude
     * @param latitude
     * @return
     */
    public ArrayList<Restaurant> getNearRestoFromHeroku(double longitude, double latitude)
    {
        ArrayList<Restaurant> list = new ArrayList<Restaurant>();

        String coors = latitude + "," + longitude;

        try {
            String url = "https://shrouded-ravine-87507.herokuapp.com/api/restaurants/" + URLEncoder.encode(coors, "UTF-8");


            if (checkNetwork()) {
                HttpsURLConnection con = makeGetRequest(url);
                if (con != null) {

                    Log.d(DEBUGTAG, "Check is good");
                    try {
                        if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                            String json = getJson(con.getInputStream());
                            Log.d(DEBUGTAG, "Response is " + json);
                            return getRestos(json, list);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Creates the array of REstaurant objects using json encoded objects
     * @param json
     * @param list
     * @return
     */
    private ArrayList<Restaurant> getRestos(String json, ArrayList<Restaurant> list)
    {
        try {

            JSONArray restos = new JSONArray(json);

            for(int i = 0; i < restos.length(); i++) {

                JSONObject restoInfo = restos.getJSONObject(i);

                String name = restoInfo.getString("name");
                int pR = restoInfo.getInt("priceRange");
                String genre = restoInfo.getString("genre");

                String address = restoInfo.getString("address");
                String zip = restoInfo.getString("postalCode");
                String city = restoInfo.getString("city");
                double longitude = restoInfo.getDouble("longitude");
                double latitude = restoInfo.getDouble("latitude");

                String date = "";
                int herokuId = restoInfo.getInt("resto_id");

                list.add(makeRestaurant(name, pR, genre, "", 0,
                        address, zip, city, longitude, latitude, "", date, herokuId));
            }

        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * Creates Restaurant object using data received from JSON response
     * @param name
     * @param pR
     * @param genre
     * @param telephone
     * @param rating
     * @param address
     * @param zip
     * @param city
     * @param longCoor
     * @param latCoor
     * @param notes
     * @param date
     * @param herokuId
     * @return
     */
    private Restaurant makeRestaurant(String name, int pR, String genre, String telephone, int rating,
                    String address, String zip, String city, double longCoor, double latCoor,
                                      String notes, String date, int herokuId)

    {
        Restaurant rest = new Restaurant();

        rest.setName(name);
        rest.setPriceRange(pR);
        rest.setGenre(genre);
        rest.setTelephone(telephone);

        rest.setRating(rating);

        rest.setAddress(address);
        rest.setPostalCode(zip);
        rest.setCity(city);
        rest.setLatitude(latCoor);
        rest.setLongitude(longCoor);
        rest.setNotes(notes + "\n" + heroTag);
        rest.setDateCreated(date);
        rest.setHerokuID(herokuId);
        return rest;
    }



    //

    /**
     * Returns reviews for particular restaurant basing on restoId
     * will only get 10 reviews or less
     * @param herokuId
     * @return
     */
    public ArrayList<Review>getReviewForResto(int herokuId)
    {

        String url = "https://shrouded-ravine-87507.herokuapp.com/api/reviews/" + herokuId;
        ArrayList<Review> reviews = new ArrayList<Review>();
        if(checkNetwork())
        {
            Log.d(DEBUGTAG, "you have network");
            HttpsURLConnection con = makeGetRequest(url);

            if(con != null) {
                try {
                    if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        String json = getJson(con.getInputStream());

                        ArrayList<Review> revs = getReview(json);
                        Log.d(DEBUGTAG, revs.toString());
                        return revs;

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return reviews;

    }

    /**
     * Creates Review object using JSON response from Heroku db
     * @param json
     * @return
     */
    private ArrayList<Review> getReview(String json)
    {
        ArrayList<Review> revs = new ArrayList<Review>();

        try {
            JSONArray rev = new JSONArray(json);

            for(int i = 0; i < rev.length(); i++) {

                JSONObject curRev = rev.getJSONObject(i);

                Review review = new Review();

                String user = curRev.getString("title");
                String comment = curRev.getString("comment");
                String date = "";

                review.setUser(user);
                review.setComment(comment);

                revs.add(review);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return revs;
    }


    /**
     * Creates GET request querying Heroku
     * @param urlStr
     * @return
     */
    private HttpsURLConnection makeGetRequest(String urlStr)
    {
        try {

            URL url = new URL(urlStr);
            //set up Connection
            HttpsURLConnection httpCon = (HttpsURLConnection) url.openConnection();

            //setup header packet
            httpCon.setRequestMethod("GET");
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
     * Reads JSON encoded response from the input stream
     * @param in
     * @return
     */
    private String getJson(InputStream in)
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


}
