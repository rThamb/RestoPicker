package assigment.dawson.restocoderenation;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import assigment.dawson.restocoderenation.beans.User;
import assigment.dawson.restocoderenation.database.RestoDBHelper;
import assigment.dawson.restocoderenation.loaders.UserLoader;

import static assigment.dawson.restocoderenation.loaders.LoadOptions.ADD_USER;
import static assigment.dawson.restocoderenation.loaders.LoadOptions.MODIFY_USER;


/**
 * This classes provides functionality to allow the user to edit his details.
 * @author pishchalov 1430169
 * @since 2016/12/08
 */

public class SettingsActivity extends AppCompatActivity {

    private RestoDBHelper db;
    private User currentUser;
    private final String USERS_PREFERENCES = "user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new RestoDBHelper(this);
        db.getWritableDatabase();
        setContentView(R.layout.activity_settings);



        SharedPreferences prefs = getSharedPreferences(USERS_PREFERENCES, MODE_PRIVATE);
        if (prefs!=null) {
            Log.d("TAG", prefs.getString("email", ""));
            ((EditText) findViewById(R.id.userEdit)).setText(prefs.getString("email", ""));
            ((EditText) findViewById(R.id.fnEdit)).setText(prefs.getString("firstName", ""));
            ((EditText) findViewById(R.id.lnEdit)).setText(prefs.getString("lastName", ""));
            ((EditText) findViewById(R.id.pcEdit)).setText(prefs.getString("postalCode", ""));
        }
    }

    /**
     * This method obtains the information in the form and save it to the
     *
     * @param view
     */
    public void saveUser(View view) {

        String email = ((EditText)findViewById(R.id.userEdit)).getText().toString();
        String pwd = ((EditText)findViewById(R.id.passEdit)).getText().toString();
        String fName = ((EditText)findViewById(R.id.fnEdit)).getText().toString();
        String lName = ((EditText)findViewById(R.id.lnEdit)).getText().toString();
        String postal = ((EditText)findViewById(R.id.pcEdit)).getText().toString();

        if (validate(email, pwd, fName, lName, postal)) {
            if (getSharedPreferences(USERS_PREFERENCES, MODE_PRIVATE).getString("email", "") !=""){
                new UserLoader( MODIFY_USER, this, db, new String[] {email, pwd, fName, lName, postal}).execute();
            }
            else {
                new UserLoader( ADD_USER, this, db, new String[] {email, pwd, fName, lName, postal}).execute();
            }

            finish();

        }
        else{
            //shows alert if the user tries to input invalid data
            new AlertDialog.Builder(this)
                    .setTitle(R.string.error)
                    .setMessage(R.string.wrongInput)
                    .setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }}).show();
        }

    }


    /**
     *
     * Validates user input
     * @param email
     * @param pwd
     * @param fName
     * @param lName
     * @param postal
     * @return
     */
    public boolean validate(String email, String pwd, String fName, String lName, String postal) {
        if(email==""||pwd==""||fName==""||lName==""||postal=="")
            return false;
        //source: http://howtodoinjava.com/regex/java-regex-validate-email-address/
        String emailPattern = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * Overrites back button launching the dialog
     */
    @Override
    public void onBackPressed()
    {
        //final int[] end = new int[1];
        new AlertDialog.Builder(this)
                .setTitle(R.string.exitSettings)
                .setMessage(R.string.exitDialogText)
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }})
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }})
                .show();
    }

    /**
     * sets database instance
     * @param db
     */
    public void setDb(RestoDBHelper db){
        this.db = db;
    }
}