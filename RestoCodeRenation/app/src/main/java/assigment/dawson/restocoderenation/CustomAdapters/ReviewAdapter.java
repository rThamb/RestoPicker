package assigment.dawson.restocoderenation.CustomAdapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import assigment.dawson.restocoderenation.R;
import assigment.dawson.restocoderenation.beans.Review;

/**
 * The present class is used to create and display the list of reviews
 * @author Renuchan o
 * @since 12/7/2016.
 */
public class ReviewAdapter extends ArrayAdapter<Review> {


    private ArrayList<Review> revs;
    private LayoutInflater inflater;

    /**
     * Constructor
     * @param context
     * @param revBox
     * @param revs
     */
    public ReviewAdapter(Context context, int revBox, ArrayList<Review> revs) {
        super(context, revBox, revs);
        this.revs = revs;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(revs != null)
            Log.d("ReviewAdapter", "Array size for REVS is : " + revs.size());

    }

    /**
     * overrides standard getView method, uses inflater and Reviews objects
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Log.i("Adapter, getView", "goes here");
        View row = (View) inflater.inflate(R.layout.review_list_format, null);

        ((TextView)row.findViewById(R.id.rev_User)).setText(revs.get(position).getUser());
        ((TextView)row.findViewById(R.id.rev_Comment)).setText(revs.get(position).getComment());

        return row;
    }

    /**
     * returns one item form the list
     * @param position
     * @return
     */
    @Nullable
    @Override
    public Review getItem(int position) {
        return revs.get(position);
    }

    /**
     * Returns the length of the list items
     * @return
     */
    @Override
    public int getCount()
    {
        return revs.size();
    }
}
