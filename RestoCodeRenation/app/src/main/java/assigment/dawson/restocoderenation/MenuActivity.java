package assigment.dawson.restocoderenation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

import assigment.dawson.restocoderenation.beans.Restaurant;
import assigment.dawson.restocoderenation.database.RestoDBHelper;
import assigment.dawson.restocoderenation.loaders.SyncTask;

/**
 * This class serves as the parent for all activities in this project. It will provide all the
 * action required from the menu
 *
 * @author pishchalov 1430169
 * @since 24/11/2016
 */
public class MenuActivity extends AppCompatActivity {


    /**
     * This method will inflate the menu will all the options
     *
     * @param menu
     * @return
     */
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {

            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.activity_menu, menu);
            return true;
        }


    /**
     * This method will handle clicks events that occur in the menu
     *
     * @param item
     * @return
     */
        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.about:
                    openAbout(item.getActionView());
                    break;
                case R.id.dawson:
                    openDawson(item.getActionView());
                    break;
                case R.id.settings:
                    openSettings(item.getActionView());
                    break;
                case R.id.synch:
                    openSynch(item.getActionView());
                    break;
            }
            return true;
        }

    /**
     * This method will launch the about activity when clicked
     * @param view
     */
    public void openAbout(View view) {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
        // Kill if not launched from MainActivity to get rid of infinite windows
        if (this.getClass() != MainActivity.class) {
            finish();
        }
    }

    /**
     * This method will open the dawson website on the default browser.
     * @param view
     */
    public void openDawson(View view) {
        Intent intent= new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.dawsoncollege.qc.ca/"));
        startActivity(intent);
    }

    /**
     * This method will launch the setting activity when clicked.
     * @param view
     */
    public void openSettings(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
        // Kill if not launched from MainActivity to get rid of infinite windows
        if (this.getClass() != MainActivity.class) {
            finish();
        }
    }

    /**
     * This method will sync the local database with the online heroku database
     * @param view
     */
    public void openSynch(View view) {

        SharedPreferences prefs = this.getSharedPreferences("user", MODE_PRIVATE);

        String userName = prefs.getString("email", "");
        String pass = prefs.getString("password", "");
        int userID = prefs.getInt("userId", 0);

        ArrayList<Restaurant> favs = new RestoDBHelper(this).getFavouriteRestos(userID);

        SyncTask sync = new SyncTask(this, userName, pass, favs);

        sync.execute();
    }

    /**
     * This method will open the dawson computer science webpage on the default browser.
     * @param view
     */
    public void openCS(View view) {
        Intent intent= new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.dawsoncollege.qc.ca/computer-science-technology/"));
        startActivity(intent);
    }
}
