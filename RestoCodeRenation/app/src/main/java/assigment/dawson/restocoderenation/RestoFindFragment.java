package assigment.dawson.restocoderenation;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.io.Serializable;


/**
 * This will provide all functionality required by the search form
 *
 * @author pishchalov 1430169
 * @since 24/11/2016
 */
@SuppressLint("ParcelCreator")
public class RestoFindFragment extends SerializableFragment implements Serializable {

    private View view;

    @Override
    public void onCreate(Bundle saveBundle)
    {
        super.onCreate(saveBundle);

    }

    /**
     * This method will preform all required action on setup
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
        this.view = inflater.inflate(R.layout.fragment_resto_find, container, false);
        view.findViewById(R.id.searchButton).setOnClickListener(searchClickListener);
        return view;

    }

    /**
     * This method obtains the information in the form and uses it to search restos
     */
    private View.OnClickListener searchClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            EditText nameText = (EditText) v.getRootView().findViewById(R.id.nameRestoInput);
            EditText genreText = (EditText) v.getRootView().findViewById(R.id.restoGenreInput);
            EditText cityText = (EditText) v.getRootView().findViewById(R.id.restoCityInput);

            String nameCriteria = nameText.getText().toString();
            String genreCriteria = genreText.getText().toString();
            String cityCriteria = cityText.getText().toString();

            ((MainActivity)getActivity()).searchRestos(nameCriteria, genreCriteria, cityCriteria);

        }
    };

}
