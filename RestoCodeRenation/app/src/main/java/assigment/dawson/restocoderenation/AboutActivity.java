package assigment.dawson.restocoderenation;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

/**
 * This class will provide all the functionality required by the about activity
 * @author pishchalov 1430169
 * @since 24/11/2016
 */
public class AboutActivity extends MenuActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

    }

    /**
     * Displays blurb for Brooker
     * @param view
     */
    public void infoBrooker(View view) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.brooker)
                .setMessage(R.string.brookerBlurb)
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
    }

    /**
     * Displays details for Thambirajah
     * @param view
     */
    public void infoRenuchan(View view) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.renuchan)
                    .setMessage(R.string.renblurb)
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).show();
    }

    /**
     * Displays details for Pishchalov
     * @param view
     */
    public void infoDenys(View view) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.denys)
                .setMessage(R.string.denblurb)
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
    }

    /**
     * displays details Spyropuls
     * @param view
     */
    public void infoDmitri(View view) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.dimitri)
                .setMessage(R.string.dimitriblurb)
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
    }

    /**
     * displays details for Kichev
     * @param view
     */
    public void infoKichev(View view) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.kichev)
                .setMessage(R.string.kichevblurb)
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
    }



}
