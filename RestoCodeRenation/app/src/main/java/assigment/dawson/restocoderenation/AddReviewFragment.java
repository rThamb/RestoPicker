package assigment.dawson.restocoderenation;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import assigment.dawson.restocoderenation.beans.Restaurant;
import assigment.dawson.restocoderenation.beans.Review;
import assigment.dawson.restocoderenation.loaders.AddReviewLoader;

import static android.content.Context.MODE_PRIVATE;

/**
 * The present fragment provides UI functionality allowing to write and add new resto review to the db
 * @author pishchalov 1430169
 * @since 2016/12/08
 */
@SuppressLint("ParcelCreator")
public class AddReviewFragment extends SerializableFragment {

    private Restaurant resto;
    private String TAG = "AddReviewFragment";
    private View view;
    private final String USERS_PREFERENCES = "user";


    @Override
    public void onCreate(Bundle saveBundle) {
        super.onCreate(saveBundle);
    }

    /**
     * Creates view and attach handlers to the UI elements
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_add_review, container, false);
        this.view = view;
        attachHandler(view);
        return view;
    }


    /**
     * Attaches method handler to the submit button
     * @param v
     */
    private void attachHandler(View v)
    {
        Button but = (Button)v.findViewById(R.id.addReview_submit);

        but.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                parseForm();
            }
        });
    }

    /**
     * Validates the input and if OK calls the method writing new review to db
     */
    private void parseForm()
    {
        TextView errorMsg = (TextView)view.findViewById(R.id.errorMsg);
        String title = ((EditText)view.findViewById(R.id.review_title)).getText().toString();
        String comment = ((EditText)view.findViewById(R.id.review_text)).getText().toString();
        String rating = ((EditText)view.findViewById(R.id.review_rating)).getText().toString();

        //check values
        try{
            int rate = Integer.parseInt(rating);
            if (rate<0 || rate>5 || title.equals("") || comment.equals(""))
                throw new IllegalArgumentException(getString(R.string.missingField));


            SharedPreferences prefs = getActivity().getSharedPreferences(USERS_PREFERENCES, MODE_PRIVATE);
            Review review = new Review(prefs.getString("email", ""), title, comment, rate);
            String password =prefs.getString("password", "");

            Log.d(TAG, "resto id is " + resto.getHerokuID());

            AddReviewLoader loader = new AddReviewLoader(review, password, resto.getHerokuID(), getActivity());
            loader.execute();

            Toast.makeText(getActivity(), "Review added", Toast.LENGTH_LONG).show();
            getActivity().onBackPressed();
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
     * Setter to pass the Resaurant instance to the object
     * @param resto
     */
    public void setResto(Restaurant resto) {
        this.resto = resto;
    }
}
