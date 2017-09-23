package assigment.dawson.restocoderenation;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import assigment.dawson.restocoderenation.beans.Restaurant;
import assigment.dawson.restocoderenation.database.RestoDBHelper;
import assigment.dawson.restocoderenation.loaders.LoadOptions;
import assigment.dawson.restocoderenation.loaders.RestoLoader;

/**
 * Main Activity is run first. Its layout contains all major functional fragments
 * @author pishchalov 1430169
 * @since 24/11/2016
 */
public class MainActivity extends MenuActivity {

    private final String TAG = this.getClass().toString();
    private RestoDBHelper db;
    private SerializableFragment currentFragment;
    private final String USERS_PREFERENCES = "user";
    int PLACE_PICKER_REQUEST =1;

    /**
     * onCreate appart from standard operations retrives saved current fragment instance and
     * geets the user name from shared preferences if any
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //If we have the bundle we retive saved fragment for display
        if(savedInstanceState!=null&&savedInstanceState.getSerializable("currentFragment")!=null) {
            this.currentFragment = (SerializableFragment) savedInstanceState.getSerializable("currentFragment");
        }
        //otherwise we assign the default value to the currentFrsgment
        else {
            this.currentFragment = new mainMenuFragment();
        }
        setContentView(R.layout.activity_main);
        db = new RestoDBHelper(this);
        db.getWritableDatabase();

        SharedPreferences prefs = getSharedPreferences(USERS_PREFERENCES, MODE_PRIVATE);
        String fullname = prefs.getString("firstName", "") + " " + prefs.getString("lastName", "");
        Log.i("TAG", "Fullname =" + fullname);

        if(fullname==null||fullname.trim().equals("")) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }
    }

    /**
     * onResume manages the currently active fragment in order to display it properly
     * on screen rotation
     */
    @Override
    protected void onResume(){
        super.onResume();
        //if portrait
        Log.d(TAG, currentFragment.getClass().toString());

        if (currentFragment.getClass()!=mainMenuFragment.class) {
            showFragment(currentFragment);
            if (currentFragment.getClass() == RestoListFragment.class) {
                loadList((RestoListFragment) currentFragment, null, new String[] {"Fast Food"}, 1);
            }
        }
        else if (this.findViewById(R.id.functionMenuFragment)!=null) {
            showFragment(currentFragment);
        }
    }

    /**
     * method handles add to favorites button click
     * @param v
     */
    public void favRestos(View v)
    {
        currentFragment = new RestoListFragment();
        showFragment(currentFragment);
        loadList((RestoListFragment)currentFragment, LoadOptions.FAV, new String[] {""}, getSharedPreferences(USERS_PREFERENCES, MODE_PRIVATE).getInt("userId", 0));

    }

    /**
     * method handles find restos button click
     * @param v
     */
    public void findRestos(View v)
    {
        currentFragment = new RestoFindFragment();
        showFragment(currentFragment);
    }


    /**
     * method handles search restos button click
     * @param nameCriteria
     * @param genreCriteria
     * @param cityCriteria
     */
    public void searchRestos(String nameCriteria, String genreCriteria, String cityCriteria)
    {
        currentFragment = new RestoListFragment();
        showFragment(currentFragment);
        Log.i(TAG, nameCriteria + "   " + genreCriteria + "  " + cityCriteria);
        loadList((RestoListFragment)currentFragment, LoadOptions.SEARCH, new String[] {"%" +nameCriteria+"%", "%"+genreCriteria +"%", "%"+cityCriteria+"%"}, 1);
    }

    /**
     * method handles near restos button click
     * @param v
     */
    public void nearRestos(View v)
    {
        currentFragment = new RestoListFragment();
        showFragment(currentFragment);
        loadList((RestoListFragment)currentFragment, LoadOptions.RESTO_NEAR, new String[] {"Fast Food"}, 1);
    }

    /**
     * method handles tip calculator button click and launches relevant activity
     * @param v
     */
    public void tipCal(View v)
    {
        currentFragment = new TipFragment();
        showFragment(currentFragment);
    }

    /**
     * Handles add resto button allowing user to add new restro to db
     * @param v
     */
    public void addResto(View v)
    {

        currentFragment =  new RestoAddFragment();
        showFragment(currentFragment);
    }

    /**
     * method handles find resto somewhere
     * @param v
     */
    public void findSomewhere(View v)
    {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles add review button click
     * @param resto
     */
    public void writeReview(Restaurant resto)
    {

        currentFragment =  new AddReviewFragment();
        showFragment(currentFragment);
        ((AddReviewFragment)currentFragment).setResto(resto);
    }


    /**
     * shows current fragment in UI
     * @param frag
     */
    private void showFragment(Fragment frag) {

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (this.findViewById(R.id.listFragmentBox)!=null) {
            ft.replace( R.id.listFragmentBox, frag);
        }
        else {
            ft.replace( R.id.functionMenuFragment, frag);
        }
        ft.commit();
    }

    /**
     * Display the list of restos according to the option selected by user
     * @param list
     * @param option
     * @param valueStr
     * @param valueInt
     */
    private void loadList(RestoListFragment list, LoadOptions option, String[] valueStr, int valueInt)
    {
        //perform the background task
        RestoLoader loader = new RestoLoader(option, this, db, list);
        loader.setDataStr(valueStr);
        loader.setDataInt(valueInt);
        loader.execute();
    }

    /**
     * Tackles redult from Google place picker API
     * @param requestCode
     * @param resultCode
     * @param data
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place =   PlacePicker.getPlace(this, data);
                Double latitude = place.getLatLng().latitude;
                Double longitude = place.getLatLng().longitude;
                Log.d(TAG, latitude+"  " + longitude);
                currentFragment = new RestoListFragment();
                showFragment(currentFragment);
                loadList((RestoListFragment)currentFragment, LoadOptions.RESTO_SOMEWHERE, new String[] {"" + longitude, ""+latitude}, 1);
            }
        }
    }


    /**
     * Saves current fragment to the bundle
     * @param savedInstanceState
     */
    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {

        savedInstanceState.putSerializable("currentFragment", currentFragment);
        super.onSaveInstanceState(savedInstanceState);
    }

    /**
     * Overrides the back button functionality
     */
    @Override
    public void onBackPressed(){
        if (currentFragment.getClass()!=mainMenuFragment.class &&this.findViewById(R.id.functionMenuFragment)!=null){
            currentFragment = new mainMenuFragment();
            showFragment(currentFragment);
        }
        else
            finish();
    }

    /**
     * settter to change current fragment from outside
     * @param currentFragment
     */
    public void setCurrentFragment(SerializableFragment currentFragment) {
        this.currentFragment = currentFragment;
    }
}