package assigment.dawson.restocoderenation.loaders;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import assigment.dawson.restocoderenation.CustomAdapters.ReviewAdapter;
import assigment.dawson.restocoderenation.beans.Review;

/**
 * This calss returns reviews for a restorant using Heroku db
 * @author Renuchan
 * @since 12/7/2016.
 */
public class ReviewLoader extends AsyncTask<Integer, Void, ArrayList<Review>> {

    private int container;
    private ListView lv;
    private Activity act;

    private String TAG = "ReviewLoader";

    public ReviewLoader(ListView lv, int revBoxId, Activity act) {
        super();
        this.container = revBoxId;
        this.lv = lv;
        this.act = act;
    }

    /**
     * Calls method that retrives reviews from Heroku in background thread
     * @param params
     * @return
     */
    @Override
    protected ArrayList<Review> doInBackground(Integer... params) {
        ApiLoader loader = new ApiLoader(act);

        return loader.getReviewForResto(params[0].intValue());

    }

    /**
     * After reviews are retrived from the db updates UI
     * @param reviews
     */
    @Override
    protected void onPostExecute(ArrayList<Review> reviews) {

        Log.d("ReviewLoader", reviews.toString());

        Log.d(TAG, "HERE ARE THE REVIEWS" + reviews.toString());

        if(reviews != null && reviews.size() != 0)
            lv.setAdapter(new ReviewAdapter(act,container,reviews));
    }
}
