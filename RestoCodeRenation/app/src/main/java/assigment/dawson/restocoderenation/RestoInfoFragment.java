package assigment.dawson.restocoderenation;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.nfc.Tag;
import android.opengl.Visibility;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import assigment.dawson.restocoderenation.beans.Restaurant;
import assigment.dawson.restocoderenation.database.RestoDBHelper;
import assigment.dawson.restocoderenation.loaders.ReviewLoader;

/**
 * Present class provides UI functionality for display of the details for selected resto
 * @author Renuchan
 * @since 12/3/2016.
 */

@SuppressLint("ParcelCreator")
public class RestoInfoFragment extends SerializableFragment {

    private Restaurant resto;
    private String TAG = "RestoInfoFragment";
    private View view;
    private RestoDBHelper db;


    private Restaurant prevResto;

    @Override
    public void onCreate(Bundle saveBundle)
    {
        super.onCreate(saveBundle);
        if (saveBundle != null) {
            resto = (Restaurant)saveBundle.getSerializable("curRestos");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_resto_info, container, false);
        attachcontrolBtn(view);
        attachInfoToView(view);
        this.view = view;
        enableButton(false);
        db = new RestoDBHelper(getActivity());

        return view;
    }

    /**
     * The method dynamically adds content to the fileds
     * and setups the user interface
     * @param view
     */
    private void attachInfoToView(View view)
    {
        ((EditText)view.findViewById(R.id.rest_name)).setText(resto.getName());
        ((EditText)view.findViewById(R.id.rest_genre)).setText(resto.getGenre());
        ((EditText)view.findViewById(R.id.rest_addres)).setText(resto.getAddress());
        ((EditText)view.findViewById(R.id.rest_city)).setText(resto.getCity());
        ((EditText)view.findViewById(R.id.rest_zip)).setText(resto.getPostalCode());
        ((EditText)view.findViewById(R.id.rest_Tele)).setText(resto.getTelephone());

        int num = resto.getPriceRange();
        StringBuilder img = new StringBuilder("");
        for(int i = 0; i < num; i++)
            img.append("$");

        ((EditText)view.findViewById(R.id.rest_priceRan)).setText(img.toString());
        ((EditText)view.findViewById(R.id.rest_created)).setText(resto.getDateCreated());
        ((EditText)view.findViewById(R.id.rest_notes)).setText(resto.getNotes());

        //set the rating properly
        int star = resto.getRating() - 1 ;
        LinearLayout ll =((LinearLayout)view.findViewById(R.id.rating_box));
        ImageView v = null;
        for(int i = 4; i > star; i--) {
            v = (ImageView) ll.getChildAt(i);
            v.setVisibility(View.INVISIBLE);
        }


        WebView wv = (WebView)view.findViewById(R.id.mapsView);

        double longitude = resto.getLongitude();
        double latitude = resto.getLatitude();

        try {
            //exception may be thrown during the load
            wv.setWebViewClient(new WebViewClient());
            wv.getSettings().setJavaScriptEnabled(true);
            String url = "http://maps.google.com/?q="+ latitude + "," + longitude;
            wv.loadUrl(url);
        }catch(Exception e)
        {
            Log.d(TAG, "unable to load the webview");
        }

    }

    /**
     * Attaches handlers to various buttons of the present fragment
     * @param view
     */
    private void attachcontrolBtn(View view)
    {
        Button addBtn = (Button)view.findViewById(R.id.addRestoBtn);
        Button editBtn = (Button)view.findViewById(R.id.editRestoBtn);
        Button saveBtn = (Button)view.findViewById(R.id.saveButton);
        Button addFav = (Button)view.findViewById(R.id.addFavBtn);
        Button delFav = (Button)view.findViewById(R.id.delButton);
        Button addRev = (Button)view.findViewById(R.id.addReviewButton);
        Button revBtn = (Button)view.findViewById(R.id.reviewButton);


        revBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                loadReviews();
            }
        });



        if(resto.getResto_id() != 0) //already in local db
            addBtn.setVisibility(View.INVISIBLE);
        else
            addBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                addResto();
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                editResto();
            }
        });

        saveBtn.setVisibility(View.INVISIBLE);
        saveBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                confirm();
            }
        });

        if(resto.getRestoFavID() != 0) {
            addFav.setVisibility(View.INVISIBLE);

        }
        else
            addFav.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                addToFav();
            }
        });


        //hide this button if heroId is 0

        //Sets handler to add review button
        addRev.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).writeReview(resto);
            }
        });

            //resto is apart of the local db
            delFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //only resto in db can be deleted
                    //came from fav
                    if (resto.getRestoFavID() != 0) {
                        delFavResto();
                        hideButtons();
                    }
                        //came from resto
                    else if (resto.getResto_id() != 0) {
                        delResto();
                        hideButtons();
                    }
                    else
                        Log.d(TAG, "delete unsupported");
                }
            });

        if(!(resto.getRestoFavID() != 0 || resto.getResto_id() != 0))
            delFav.setVisibility(View.INVISIBLE);

        //only allow editing for restos in local db
        if(resto.getResto_id() == 0)
        {
            editBtn.setVisibility(View.INVISIBLE);
            saveBtn.setVisibility(View.INVISIBLE);
        }

        if(resto.getHerokuID() == 0) {
            revBtn.setVisibility(View.INVISIBLE);
            addRev.setVisibility(View.INVISIBLE);
        }

    }

    /**
     * Loads all reviews for this resto
     */
    private void loadReviews()
    {
        ListView lv = (ListView)view.findViewById(R.id.reviewBox);

        ReviewLoader loader = new ReviewLoader(lv,  R.id.reviewBox, getActivity());

        loader.execute(resto.getHerokuID());
    }

    /**
     * Hide all the button when the user deletes
     */
    private void hideButtons()
    {
        Button addBtn = (Button)view.findViewById(R.id.addRestoBtn);
        Button editBtn = (Button)view.findViewById(R.id.editRestoBtn);
        Button saveBtn = (Button)view.findViewById(R.id.saveButton);
        Button addFav = (Button)view.findViewById(R.id.addFavBtn);
        Button delFav = (Button)view.findViewById(R.id.delButton);
        Button addReview = (Button)view.findViewById(R.id.addReviewButton);

        addReview.setVisibility(View.INVISIBLE);
        addBtn.setVisibility(View.INVISIBLE);
        editBtn.setVisibility(View.INVISIBLE);
        saveBtn.setVisibility(View.INVISIBLE);
        addFav.setVisibility(View.INVISIBLE);
        delFav.setVisibility(View.INVISIBLE);

    }


    /**
     * Deletes current restoraunt
     */
    private void delResto()
    {
        Log.d(TAG, "Going to delete a REsto");
        db.deleteResto(resto.getResto_id());
        ((Button)view.findViewById(R.id.delButton)).setVisibility(View.INVISIBLE);
        Log.d(TAG, "delete was good");
    }

    /**
     * Deletes the current resto from favorites
     */
    private void delFavResto()
    {
        Log.d(TAG, "Going to delete a fav REsto");
        db.deleteFav(resto.getRestoFavID());
        ((Button)view.findViewById(R.id.delButton)).setVisibility(View.INVISIBLE);
    }

    /**
     * Edits details for the present restaurant
     */
    private void addResto()
    {
        if(resto.getResto_id() ==  0) {
            int id = db.addRestaurant(resto);
            resto.setResto_id(id);
            ((Button)view.findViewById(R.id.addRestoBtn)).setVisibility(View.INVISIBLE);
            Log.d(TAG, "added a resto to local db");
        }
    }

    /**
     * Edits details for the present restaurant
     */
    private void editResto()
    {
        prevResto = new Restaurant(resto);
        enableButton(true);
        ((Button)view.findViewById(R.id.saveButton)).setVisibility(View.VISIBLE);
        ((Button)view.findViewById(R.id.delButton)).setVisibility(View.INVISIBLE);
    }

    /**
     * Add current restaurant to favourites
     */
    private void addToFav()
    {
        //not in fav
        if(resto.getRestoFavID() == 0)
        {
            Log.d("Clicked", "Going to added to fav");
            int userId = getUserID();

            if(resto.getResto_id() == 0)
                addResto();

            if(userId != 0) {
                int favid = db.addToFav(resto.getResto_id(), userId);
                resto.setRestoFavID(favid);
            }
            Log.d(TAG, "added resto to fav");
            ((Button)view.findViewById(R.id.addFavBtn)).setVisibility(View.INVISIBLE);
        }
    }

    /**
     * returns user ID
     * @return
     */
    private int getUserID()
    {
        Activity act = getActivity();

        SharedPreferences prefs = act.getSharedPreferences("user", act.MODE_PRIVATE);
        return prefs.getInt("userId",0);

    }

    /**
     * Saves the current restaurant
     */
    private void saveResto()
    {
        Log.d(TAG, "You clicked Save");
        enableButton(false);
        ((Button)view.findViewById(R.id.saveButton)).setVisibility(View.INVISIBLE);
        ((Button)view.findViewById(R.id.delButton)).setVisibility(View.VISIBLE);

        Restaurant rest = parseFormForResto();

        if(rest != null)
            db.updateRestoInfo(rest);

    }

    /**
     * Enables editing in the text fields
     * @param enable
     */
    private void enableButton(boolean enable)
    {
        ((EditText)view.findViewById(R.id.rest_name)).setEnabled(enable);
        ((EditText)view.findViewById(R.id.rest_addres)).setEnabled(enable);
        ((EditText)view.findViewById(R.id.rest_genre)).setEnabled(enable);
        ((EditText)view.findViewById(R.id.rest_zip)).setEnabled(enable);
        ((EditText)view.findViewById(R.id.rest_created)).setEnabled(enable);
        ((EditText)view.findViewById(R.id.rest_created)).setEnabled(enable);
        ((EditText)view.findViewById(R.id.rest_notes)).setEnabled(enable);
        ((EditText)view.findViewById(R.id.rest_priceRan)).setEnabled(enable);
        ((EditText)view.findViewById(R.id.rest_city)).setEnabled(enable);
        ((EditText)view.findViewById(R.id.rest_Tele)).setEnabled(enable);
    }

    /**
     * Setter for Restaurant instance
     * @param resto
     */
    public void setResto(Restaurant resto) {

        this.resto = resto;
    }

    /**
     * Validates and input data and adds resto to db
     * @return
     */
    private Restaurant parseFormForResto()
    {

        String name = ((EditText)view.findViewById(R.id.rest_name)).getText().toString();
        String address = ((EditText)view.findViewById(R.id.rest_addres)).getText().toString();
        String genre = ((EditText)view.findViewById(R.id.rest_genre)).getText().toString();
        String zip = ((EditText)view.findViewById(R.id.rest_zip)).getText().toString();
        String city = ((EditText)view.findViewById(R.id.rest_city)).getText().toString();
        String date = ((EditText)view.findViewById(R.id.rest_created)).getText().toString();
        String notes = ((EditText)view.findViewById(R.id.rest_notes)).getText().toString();
        String pR = ((EditText)view.findViewById(R.id.rest_priceRan)).getText().toString();
        String tele = ((EditText)view.findViewById(R.id.rest_Tele)).getText().toString();

        //check values
        try{

            if(name.equals("") || address.equals("") || genre.equals("") || zip.equals("") || city.equals("")
                    || notes.equals(""))
                throw new IllegalArgumentException(getString(R.string.missingField));

            checkPR(pR);
            checkTele(tele);
            Restaurant rest = new Restaurant();
            checkAndSetCoordinates(address + " " + city + " " +zip, rest);

            return getResto( name,  address,  genre,  zip,  city ,  date, notes, pR.length(), tele, rest);
        }
        catch(IllegalArgumentException e)
        {
            e.printStackTrace();
            ((TextView)view.findViewById(R.id.errorContainer)).setText(e.getMessage());
        }
        catch(Exception e)
        {
            e.printStackTrace();
            ((TextView)view.findViewById(R.id.errorContainer)).setText(e.getMessage());
        }
        restoreView();
        return null;
    }

    /**
     * This method validates price category input by the user
     * @param pr
     * @throws IllegalArgumentException
     */
    private void checkPR(String pr) throws IllegalArgumentException
    {
        if(pr.length() <= 0 || pr.length() >= 5)
            throw new IllegalArgumentException(getString(R.string.badPR));

    }

    /**
     * Validates telephone input by the user
     * @param tele
     * @throws IllegalArgumentException
     */
    private void checkTele(String tele)throws IllegalArgumentException
    {

        for(int i = 0; i < tele.length(); i++)
        {
            char ele = tele.charAt(i);

            if(!Character.isDigit(ele))
                if(!(ele == '-'))
                throw new IllegalArgumentException(getString(R.string.wrongTele));
        }
    }

    /**
     * Method that is used to assemble the resaurant object
     * @param name
     * @param addres
     * @param genre
     * @param zip
     * @param city
     * @param date
     * @param notes
     * @param pr
     * @param tele
     * @param rest
     * @return
     */
    private Restaurant getResto(String name, String addres, String genre, String zip, String city , String date,
                           String notes, int pr, String tele, Restaurant rest)
    {
        rest.setName(name);
        rest.setAddress(addres);
        rest.setCity(city);
        rest.setGenre(genre);
        rest.setPostalCode(zip);
        rest.setDateCreated(date);
        rest.setNotes(notes);
        rest.setPriceRange(pr);
        rest.setTelephone(tele);
        rest.setResto_id(resto.getResto_id());
        rest.setRestoFavID(resto.getRestoFavID());

       return rest;

    }

    /**
     * This method checks the validity of the address input from the user and sets latitude and longitude
     * of the resto
     * @param address
     * @param rest
     * @return
     * @throws IllegalArgumentException
     *Sourcehttp://stackoverflow.com/questions/16686436/getting-latitude-longitude-from-address-in-android
     */
    private Restaurant checkAndSetCoordinates(String address, Restaurant rest)throws IllegalArgumentException
    {
        Geocoder geo = new Geocoder(getActivity());

        try {
            List<Address> addresses = geo.getFromLocationName(address, 1);

            if (addresses.size() >  0)
            {
                double latitude = addresses.get(0).getLatitude();
                double longtitude = addresses.get(0).getLongitude();

                rest.setLatitude(latitude);
                rest.setLongitude(longtitude);
            }
            else
                throw new IllegalArgumentException(getString(R.string.badAddress));

        }
        catch (IOException e) {
            throw new IllegalArgumentException(getString(R.string.badAddress));
        }
        return rest;
    }

    /**
     * Shows alert dialog when user wants to exit add new resto user interface
     * without saving modifications
     */
    private void confirm()
    {
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.saveHeader)
                .setMessage(R.string.saveDialogText)
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        restoreView();
                    }})
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        saveResto();
                    }})
                .show();
    }


    /**
     * Restores previous view if wrong info passed by user
     */
    private void restoreView()
    {
        resto = prevResto;
        attachInfoToView(view);
        enableButton(false);
        ((Button)view.findViewById(R.id.saveButton)).setVisibility(View.INVISIBLE);

    }


    @Override
    public void onSaveInstanceState(Bundle savedState) {
        super.onSaveInstanceState(savedState);
        savedState.putSerializable("curRestos", resto);
    }
}
