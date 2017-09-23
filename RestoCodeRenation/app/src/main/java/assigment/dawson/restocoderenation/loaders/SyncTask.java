package assigment.dawson.restocoderenation.loaders;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import assigment.dawson.restocoderenation.beans.Restaurant;

/**
 * This class will sync the local database to the heroku database
 * @Author Renuchan
 * @since 12/8/2016.
 */
public class SyncTask extends AsyncTask<Void,Void,Void> {

    private Activity act;

    private String user;
    private String password;
    private ArrayList<Restaurant> favs;


    public SyncTask(Activity act, String user, String password, ArrayList<Restaurant> favs)
    {
        this.act = act;
        this.user = user;
        this.password = password;
        this.favs = favs;
    }

    /**
     * Will write the current user favourite restos to the heroku database
     *
     * @param voids
     * @return
     */
    @Override
    protected Void doInBackground(Void... voids) {

        AddRestoPost sender = new AddRestoPost(act, user, password, null);

        for(int i = 0; i < favs.size(); i++)
        {
             Restaurant curFav = favs.get(i);
             sender.setResto(curFav);
             sender.addRestoToHeroku();
        }
        return null;
    }
}
