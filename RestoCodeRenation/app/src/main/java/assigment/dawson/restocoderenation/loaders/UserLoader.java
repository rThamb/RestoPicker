package assigment.dawson.restocoderenation.loaders;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;

import assigment.dawson.restocoderenation.beans.User;
import assigment.dawson.restocoderenation.database.RestoDBHelper;

/**
 * The present class performs db manipulation with user table using background thread
 * Created by pishc on 2016-12-05.
 */

public class UserLoader extends AsyncTask<Void,Void,ArrayList<User>> {

    private Activity activity;
    private LoadOptions option;
    private RestoDBHelper db;
    private String dataStr[];

    private final String USERS_PREFERENCES = "user";


    /**
     * constructor
     * @param option
     * @param context
     * @param db
     * @param dataStr
     */
    public UserLoader(LoadOptions option, Activity context, RestoDBHelper db, String[] dataStr)
    {
        super();
        this.activity = context;
        this.option = option;
        this.db = db;
        this.dataStr = dataStr;
    }

    /**
     * Performs one of three background actions depending on the option provided
     * @param params
     * @return
     */
    @Override
    protected ArrayList<User> doInBackground(Void... params) {

        ArrayList<User> users = null;

        switch(option)
        {
            case ADD_USER:
                db.addNewUser(dataStr);
                break;
            case MODIFY_USER:
                db.modifyUser(dataStr);
                break;
            case FIND_USER:
                users = db.findUserByEmail(dataStr[0]);
                break;
            default:
                users = new ArrayList<User>(); // return empty list
                break;

        }

        users = db.findUserByEmail(dataStr[0]);
        User currentUser = users.get(0);

        //Saves user details in shared preferences
        SharedPreferences sp = activity.getSharedPreferences(USERS_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("userId", currentUser.getUserId());
        editor.putString("firstName", currentUser.getFirstName());
        editor.putString("lastName", currentUser.getLastName());
        editor.putString("postalCode", currentUser.getPostalCode());
        editor.putString("password", currentUser.getPassword());
        editor.putString("email", currentUser.getEmail());
        editor.commit();
        Log.d("UserLoader", currentUser.getEmail());
        return users;
    }

}

