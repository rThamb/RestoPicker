package assigment.dawson.restocoderenation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import assigment.dawson.restocoderenation.CustomAdapters.RestoAdapter;
import assigment.dawson.restocoderenation.beans.Restaurant;

/**
 * This class will provided required actions when displaying a list of restos
 * @author Renuchan
 * @since 12/3/2016.
 */
@SuppressLint("ParcelCreator")
public class RestoListFragment extends SerializableFragment {

    private ArrayList<Restaurant> restos;
    private  Context context;
    private View view;


    @Override
    public void onCreate(Bundle saveBundle)
    {
        super.onCreate(saveBundle);
        if (saveBundle != null) {
            restos = (ArrayList<Restaurant>)saveBundle.getSerializable("myRestos");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.view = inflater.inflate(R.layout.resto_list_fragment, container, false);
        return view;
    }

    /**
     * There method will populate the view with the restos.
     * @param rest
     */
    public void updateList(ArrayList<Restaurant> rest)
    {
        this.restos=rest;
        if (getActivity()!=null) {
            this.context = getActivity();
        }
        ListView lv = (ListView)view.findViewById(R.id.restoListBox);

        if(rest != null && rest.size() != 0)
            lv.setAdapter(new RestoAdapter(context, R.id.restoListBox, rest));
        else
            ((TextView)view.findViewById(R.id.tvHeader)).setText(getString(R.string.nothingFound));
    }

    public ArrayList<Restaurant> getRestos(){
        return restos;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public void onSaveInstanceState(Bundle savedState) {
        super.onSaveInstanceState(savedState);
        savedState.putSerializable("myRestos", restos);
    }

}