package assigment.dawson.restocoderenation.loaders;

import android.app.Activity;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;

import assigment.dawson.restocoderenation.RestoListFragment;
import assigment.dawson.restocoderenation.beans.Restaurant;
import assigment.dawson.restocoderenation.database.RestoDBHelper;
import assigment.dawson.restocoderenation.locationServices.GPSTracker;

import static assigment.dawson.restocoderenation.loaders.LoadOptions.RESTO_NEAR;

/**
 * This class serves as a switch to call thew proper DB methods from UI
 * Created by Renuchan on 11/25/2016.
 */

public class RestoLoader extends AsyncTask<Void, Void, ArrayList<Restaurant>> {

    private Activity activity;

    private LoadOptions option;
    private RestoDBHelper db;
    private RestoListFragment fragment;

    private String dataStr[];
    private String nameCriteria;
    private String genreCriteria;
    private String cityCriteria;
    private int dataInt;
    private Location location;

    private String TAG = "RestoLoader";

    /**
     * Constructor
     * @param option
     * @param context
     * @param db
     * @param fragment
     */
    public RestoLoader(LoadOptions option, Activity context, RestoDBHelper db, RestoListFragment fragment) {
        super();
        this.activity = context;
        this.option = option;
        this.db = db;
        this.fragment = fragment;
    }

    //Setters for fields
    public void setDataStr(String[] dataStr) {
        this.dataStr = dataStr;
    }

    public void setDataInt(int dataInt) {
        this.dataInt = dataInt;
    }


    /**
     * Finds location before using it to retrive the list of restaurants
     */
    @Override
    protected void onPreExecute() {
        if (option == RESTO_NEAR) {
            GPSTracker gpstracker = new GPSTracker(activity);
            location = gpstracker.getLocation();
        }

    }


    /**
     * Performs operations in background thread (calling DAO methods)
     * @param params
     * @return
     */
    @Override
    protected ArrayList<Restaurant> doInBackground(Void... params) {
        if (option==null)
            return fragment.getRestos();

        ArrayList<Restaurant> restos = null;

        switch(option)
        {
            case SEARCH:
                restos = db.findRestosByName(dataStr);
                break;
            case FAV:
                restos = db.getFavouriteRestos(dataInt);
                break;
            case RESTO_NEAR: // load api restos

                restos = getNearResto();
                break;
            case RESTO_SOMEWHERE: // load api restos form place picker

                ZomatoRestoList loader = new ZomatoRestoList(activity);

                restos = loader.getRestos(Double.parseDouble(dataStr[0]), Double.parseDouble(dataStr[1]));

                break;
            default:
                restos = new ArrayList<Restaurant>(); // return empty list
                break;

        }
        return restos;
    }

    /**
     * After completion of the db queries returns the list of restaurants
     * @param restaurants
     */
    @Override
    protected void onPostExecute(ArrayList<Restaurant> restaurants)
    {
        fragment.updateList(restaurants);
    }


    /**
     * Returns list of nearby restos from Zomato db using lat and long as input
     * @return arraylist of restaurant objects
     */
    private ArrayList<Restaurant> getNearResto()
    {
        ZomatoRestoList loader = new ZomatoRestoList(activity);
        ApiLoader loader2 = new ApiLoader(activity);

        double lat = 0.0;
        double longCoor = 0.0;


        if (location!=null) {

            lat = location.getLatitude();
            longCoor = location.getLongitude();
        }
        else {
            lat = 45.489209;
            longCoor = -73.586870;
        }
        ArrayList<Restaurant> zama = loader.getRestos(longCoor,lat);
        ArrayList<Restaurant> hero = loader2.getNearRestoFromHeroku(longCoor, lat);

        Log.d("RestoLoader", hero.toString());
        Log.d("RestoLoader", zama.toString());

        return mergeHeroAndZama(hero, zama);

    }


    /**
     * Phase2 merges the results from search in Heroku and Zomato
     * @param hero
     * @param zama
     * @return
     */
    private ArrayList<Restaurant> mergeHeroAndZama(ArrayList<Restaurant> hero,
                                                   ArrayList<Restaurant> zama)
    {
        if(hero != null) {
            for (Restaurant rest : hero)
                zama.add(rest);
        }
        return zama;
    }
}
