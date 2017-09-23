package assigment.dawson.restocoderenation;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.Serializable;

import static android.content.Context.MODE_PRIVATE;

/**
 * This class setup the main menu view.
 *
 * @author pishchalov 1430169
 * @since 24/11/2016
 */
@SuppressLint("ParcelCreator")
public class mainMenuFragment extends SerializableFragment implements Serializable{

    private static final String USERS_PREFERENCES = "user";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * The method will obtain the users information and attach it to the view
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.menu_fragment, container, false);
        SharedPreferences prefs = getActivity().getSharedPreferences(USERS_PREFERENCES, MODE_PRIVATE);
        String fullname = prefs.getString("firstName", null) + " " + prefs.getString("lastName", null);
        if (!fullname.equals("") && fullname != null) {
            ((TextView) view.findViewById(R.id.welcome_banner)).setText("Welcome " +fullname);
        }
        return view;
    }



}