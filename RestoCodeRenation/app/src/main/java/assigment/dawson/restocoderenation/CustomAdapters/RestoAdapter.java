package assigment.dawson.restocoderenation.CustomAdapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import assigment.dawson.restocoderenation.MainActivity;
import assigment.dawson.restocoderenation.R;
import assigment.dawson.restocoderenation.RestoInfoFragment;
import assigment.dawson.restocoderenation.beans.Restaurant;


/**
 * This class is in charge of defining all work related to creating n amount of view items in
 * the view list
 * * @author CodeRenation
 * @since 2016.12.03
 */

public class RestoAdapter extends ArrayAdapter<Restaurant> {

    private Context context;
    private ArrayList<Restaurant> restos;
    private static LayoutInflater inflater;
    private String TAG = "RestoAdapter";


    /**
     * Constructor
     *
     * @param context
     * @param layout
     * @param restos
     */
    public RestoAdapter(Context context, int layout, ArrayList<Restaurant> restos)
    {
        super(context, layout, restos);
        this.context = context;
        this.restos = restos;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Log.i("Adapter", restos.get(0).getName());
    }


    /**
     * Get the total number of the items to create views for
     *
     * @return          size of the info
     */
    @Override
    public int getCount() {
        Log.i("Adapter, count", Integer.toString(restos.size()+10));

        return restos.size();
    }

    /**
     * Used to obtain the item at the current index
     *
     * @param position          index of the item
     * @return                  item at index
     */
    @Override
    public Restaurant getItem(int position) {
        return restos.get(position);
    }

    /**
     * Returns the id of the item at the current position
     *
     * @param position          index of the current item
     * @return                  id of the item
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * This method will create the view for 1 row in a list
     *
     * @param position          Current item
     * @param convertView
     * @param parent
     * @return                  View associated with the column
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Log.i("Adapter, getView", "goes here");
        PlaceHolder items = new PlaceHolder();
        View row = (View)inflater.inflate(R.layout.resto_list_format, null);

        items.rName = (TextView)row.findViewById(R.id.rName_Lbl);
        items.rAddress = (TextView)row.findViewById(R.id.rAddress_Lbl);
        items.rZip = (TextView)row.findViewById(R.id.rZip_Lbl);
        items.rPR = (TextView)row.findViewById(R.id.rPRange_Lbl);
        items.tele = (TextView)row.findViewById(R.id.rTele_Num);

        //set the values for 1 row in the list

        Restaurant rest = restos.get(position);
        items.rName.setText(rest.getName());
        items.rAddress.setText(rest.getAddress());
        items.rZip.setText(rest.getCity() + ", " + rest.getPostalCode());
        items.tele.setText(rest.getTelephone());

        //set the price range
        int pr = rest.getPriceRange();
        StringBuilder dollars = new StringBuilder("");
        for(int i = 0; i < pr; i++)
            dollars.append("$");

        items.rPR.setText(dollars.toString());

        //set the rating stars

        int star = rest.getRating() - 1 ;
        LinearLayout ll =((LinearLayout)row.findViewById(R.id.ratingBox));
        ImageView v = null;
        for(int i = 4; i > star; i--) {
            v = (ImageView) ll.getChildAt(i);
            v.setVisibility(View.INVISIBLE);
        }

        //attach the click event to the view
        row.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Log.d("asd", "You clicked");
                Restaurant rest = restos.get(position);
                RestoInfoFragment frag = new RestoInfoFragment();
                ((MainActivity)context).setCurrentFragment(frag);
                frag.setResto(rest);
                showFragment(frag);
            }
        });

        row.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    //intent.setData(Uri.parse("tel:0123456789"));
                    context.startActivity(intent);

                    return true;
                }catch(Exception e)
                {
                    Log.d(TAG, "The device doesn't support calls");
                    return false;
                }
            }
        });

        return row;

    }

    /**
     * The present method shows the fragment with restoraunt info
     * @param frag
     */
    private void showFragment(Fragment frag) {

        Activity main = (Activity)context;
        FragmentManager fm = main.getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (main.findViewById(R.id.listFragmentBox)!=null) {
            ft.replace( R.id.listFragmentBox, frag);
        }
        else {
            ft.replace( R.id.functionMenuFragment, frag);
        }
        ft.commit();
    }

    /**
     * This class holds the View objects for 1 row
     */
    class PlaceHolder
    {
        TextView rName;
        TextView rAddress;
        TextView rZip;
        TextView rPR;
        TextView tele;

    }
}
