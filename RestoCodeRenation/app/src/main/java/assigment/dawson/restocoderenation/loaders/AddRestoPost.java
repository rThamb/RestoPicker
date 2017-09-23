package assigment.dawson.restocoderenation.loaders;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import assigment.dawson.restocoderenation.beans.Restaurant;

/**
 * The present class adds
 * @Author Renuchan
 * @since 12/8/2016.
 */
public class AddRestoPost extends AsyncTask<Void, Void, Void> {

    private Activity act;
    private String user;
    private String password;
    private Restaurant resto;

    private NetworkInfo netInfo;
    private NetworkInfo netMobileInfo;
    private ConnectivityManager cm;

    private final String DEBUGTAG = "AddRestoPost";

    /**
     * Adds new resto to the Heroku db using post method
     * @param act
     * @param user
     * @param password
     * @param resto
     */
    public AddRestoPost(Activity act, String user, String password, Restaurant resto) {
        super();
        this.act = act;
        this.user = user;
        this.password = password;
        this.resto = resto;

        cm = (ConnectivityManager) act.getSystemService(act.CONNECTIVITY_SERVICE);
        netInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        netMobileInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
    }


    /**
     * Does db management in background thread by calling other methods
     * @param params
     * @return
     */
    @Override
    protected Void doInBackground(Void... params) {

        if(checkNetwork())
            addRestoToHeroku();
        return null;
    }

    /**
     * Checks if network connection is available
     * @return true if connection available
     */
    private boolean checkNetwork() {
        if (netInfo.isAvailable() && netInfo.isConnected())
            return true;
        else if (netMobileInfo.isAvailable() && netMobileInfo.isConnected())
            return true;
        else
            return false;

    }

    public void setResto(Restaurant resto) {
        this.resto = resto;
    }

    /**
     * Creates POST request and sends Jsonized object in it to add new review in Heroku db
     *
     */
    public void addRestoToHeroku() {
        String urlStr = "https://shrouded-ravine-87507.herokuapp.com/api/add_restaurant";

        if (checkNetwork()) {

            try {
                URL url = new URL(urlStr);

                byte[] data = createJsonResto(resto, user, password).getBytes("UTF-8");

                //Log.d(DEBUGTAG, data);

                HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
                con.setDoOutput(true);
                con.setRequestMethod("POST");

                con.setReadTimeout(10000);
                con.setConnectTimeout(15000);

                con.setRequestProperty("Content-Type",
                        "application/json; charset=UTF-8");
                // set length of POST data to send
                con.addRequestProperty("Content-Length", data.toString());

                //send the POST out
                BufferedOutputStream out = new BufferedOutputStream(con.getOutputStream());

                out.write(data);
                out.flush();
                out.close();

                if (con.getResponseCode() == HttpURLConnection.HTTP_OK)
                    Log.d(DEBUGTAG, "Post was a success");
                else
                    Log.d(DEBUGTAG, "Post was a failure ERROR CODE: " + con.getResponseCode());

                con.disconnect();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Creates JSON encoded object that is then sent using POST request
     * @param resto
     * @param email
     * @param password
     * @return
     */
    private String createJsonResto(Restaurant resto, String email, String password) {
        JSONObject rest = new JSONObject();

        try {
            rest.put("name", resto.getName());
            rest.put("notes", resto.getNotes());
            rest.put("priceRange", resto.getPriceRange());
            rest.put("genre", resto.getGenre());
            rest.put("image", "");
            rest.put("address", resto.getAddress());
            rest.put("city", resto.getCity());
            rest.put("postalCode", resto.getPostalCode());

            rest.put("email", email);
            rest.put("password", password);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return rest.toString();
    }

}