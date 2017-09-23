package assigment.dawson.restocoderenation;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.concurrent.RunnableFuture;

import assigment.dawson.restocoderenation.beans.Restaurant;
import assigment.dawson.restocoderenation.database.RestoDBHelper;
import assigment.dawson.restocoderenation.loaders.AddRestoPost;
import assigment.dawson.restocoderenation.loaders.ApiLoader;

import static android.content.Context.MODE_PRIVATE;

/**
 * This fragment will provide all functionality to add resto information to the local and heroku
 * database.
 *
 * @author Renuchan
 * @since 1/12/2016
 */
@SuppressLint("ParcelCreator")
public class RestoAddFragment extends SerializableFragment implements Serializable {

    private View view;
    private String DEBUG = "RestoAddFragment";


    /**
     * This method will preform the required actions will setting up the view.
     * This will attach the handler to the button and create the view
     * @param saveBundle
     */
    @Override
    public void onCreate(Bundle saveBundle)
    {
        super.onCreate(saveBundle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_add_resto, container, false);
        this.view = view;
        attachHandler(view);

        return view;
    }

    /**
     * This method attaches the handler to the submit button
     * @param v
     */
    private void attachHandler(View v)
    {
        Button but = (Button)v.findViewById(R.id.addResto_submit);

        but.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                parseForm();
            }
        });
    }


    /**
     * This method will collect all the data from the form and validate it.
     *
     */
    private void parseForm()
    {
        TextView errorMsg = (TextView)view.findViewById(R.id.errorMsg);

        String name = ((EditText)view.findViewById(R.id.resto_name)).getText().toString();
        String address = ((EditText)view.findViewById(R.id.resto_address)).getText().toString();
        String genre = ((EditText)view.findViewById(R.id.resto_gen)).getText().toString();
        String zip = ((EditText)view.findViewById(R.id.resto_zip)).getText().toString();
        String city = ((EditText)view.findViewById(R.id.resto_city)).getText().toString();
        String date = (new Date()).toString();
        String notes = ((EditText)view.findViewById(R.id.resto_notes)).getText().toString();
        String tele = ((EditText)view.findViewById(R.id.resto_tele)).getText().toString();


        //check values
        try{

            String price = ((EditText)view.findViewById(R.id.resto_pRange)).getText().toString();
            int pR = Integer.parseInt(price);
            checkNumbers(pR);

            if(name.equals("") || address.equals("") || genre.equals("") || zip.equals("") || city.equals("")
                    || notes.equals(""))
                throw new IllegalArgumentException(getString(R.string.missingField));

            Restaurant rest = new Restaurant();

            checkAndSetCoordinates(address + " " + city + " " +zip, rest);

            writeToDB( name,  address,  genre,  zip,  city ,  date, notes,  pR, tele, rest);
            writeToHeroku(rest);
            disableFields();


            errorMsg.setText(getString(R.string.addSucces));
        }
        catch(IllegalArgumentException e)
        {
            errorMsg.setText(e.getMessage());
        }
        catch(Exception e)
        {
            errorMsg.setText(getString(R.string.generalError));
        }

    }

    /**
     * This method will write the resto information to the heroku db
     * @param rest
     */
    private void writeToHeroku(final Restaurant rest)
    {
        final Activity context = getActivity();
        SharedPreferences prefs = context.getSharedPreferences("user", MODE_PRIVATE);

        final String username = prefs.getString("email", "");
        final String password = prefs.getString("password", "");

        AddRestoPost sender = new AddRestoPost(getActivity(), username ,password, rest);
        sender.execute();

    }

    /*
        This method check the price Range entered
    */
    private void checkNumbers(int pR) throws IllegalArgumentException
    {
        if (pR >= 4 || pR <=0)
            throw new IllegalArgumentException(getString(R.string.wrongPRange));
    }


    /**
     * This method will obtain the longitude and latitude based on the information passed
     *
     *
     * @param address
     * @param rest
     * @return
     * @throws IllegalArgumentException
     *
     * Source : http://stackoverflow.com/questions/16686436/getting-latitude-longitude-from-address-in-android
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
     * This method will write all the information provided to the local database
     *
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
     */
    private void writeToDB(String name, String addres, String genre, String zip, String city , String date,
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



        RestoDBHelper db = new RestoDBHelper(getActivity());
        db.addRestaurant(rest);
        Log.d(DEBUG, "added new resto to local db");

    }

    /**
     * This method will disable all the form field when successfully added
     */
    private void disableFields()
    {
        LinearLayout ll = (LinearLayout)view.findViewById(R.id.restoAddFields);

        for(int i = 0; i < ll.getChildCount(); i++)
            ((View)ll.getChildAt(i)).setEnabled(false);
    }

}
