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

import assigment.dawson.restocoderenation.beans.Review;

/**
 * This class performs adding new review to the Heroku db in the background thread
 * @author pishchalov 1430169
 * @since 20166/12/08
 */

public class AddReviewLoader extends AsyncTask<Void, Void, Void> {

    private static final String URL_STR = "https://shrouded-ravine-87507.herokuapp.com/api/add_review";
    private static final String DEBUGTAG = "ReviewLoader";
    private Review review;
    private String password;
    private int restoId;
    private Activity activity;

    /**
     * Constructor
     * @param review
     * @param password
     * @param restoId
     */
    public AddReviewLoader(Review review, String password, int restoId, Activity ac) {
        super();
        this.review = review;
        this.password= password;
        this.restoId=restoId;
        this.activity = ac;
    }

    /**
     * Does remote db operations using other methods
     * @param params
     * @return
     */
    @Override
    protected Void doInBackground(Void... params) {
        if (checkNetwork()) {
            addReviewToHeroku(review, password, restoId);
        }
        return null;

    }

    /**
     * Creates POST request and sends Jsonized object in it to add new review in Heroku db
     * @param review
     * @param password
     * @param restoId
     */
    private void addReviewToHeroku(Review review, String password, int restoId)
    {
       try {
            URL url = new URL(URL_STR);

           byte[] data = createJsonReview(review, password, restoId).getBytes("UTF-8");

           HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
           con.setDoOutput(true);
           con.setRequestMethod("POST");

           con.setReadTimeout(10000);
           con.setConnectTimeout(15000);

           con.setRequestProperty("Content-Type",
                   "application/json; charset=UTF-8");
           // set length of POST data to send
           con.addRequestProperty("Content-Length", data.toString());

           BufferedOutputStream out = new BufferedOutputStream(con.getOutputStream());

           out.write(data);
           out.flush();
           out.close();

            if(con.getResponseCode() == HttpURLConnection.HTTP_OK)
                Log.d(DEBUGTAG, "Post was a success");
            else
                Log.d(DEBUGTAG, "Post was a failure ERROR CODE: " + con.getResponseCode());

            con.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Performs assembly of JSON object using the data provided to the present class
     * @param review
     * @param password
     * @param restoId
     * @return
     */
    private String createJsonReview(Review review, String password, int restoId)
    {
        JSONObject rev = new JSONObject();

        try {
            rev.put("rating", review.getRating());
            rev.put("comment", review.getComment());
            rev.put("title", review.getTitle());
            rev.put("email", review.getUser());
            rev.put("password", password);
            rev.put("resto_id", restoId);
            Log.d(DEBUGTAG, "JSON object is created here it is: \n" + rev.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return rev.toString();
    }

    /**
     * Checks network status (available or not)
     * @return true if available
     */
    private boolean checkNetwork()
    {
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(activity.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo netMobileInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if(netInfo.isAvailable() && netInfo.isConnected())
            return true;
        else if (netMobileInfo.isAvailable() && netMobileInfo.isConnected())
            return true;
        else
            return false;
    }
}