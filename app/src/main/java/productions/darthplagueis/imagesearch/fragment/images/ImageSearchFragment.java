package productions.darthplagueis.imagesearch.fragment.images;


import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import productions.darthplagueis.imagesearch.R;
import productions.darthplagueis.imagesearch.util.CustomSpinner;


public class ImageSearchFragment extends Fragment {

    private SearchFragListener listener;
    private final String TAG = "Search Fragment";
    private EditText searchEditText;
    private CustomSpinner spinner;
    private Button searchButton;
    private Button advSearchFrag;
    private String spinnerChoice;
    private String defaultType = "all";

    public ImageSearchFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_image_search, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(false);

        LinearLayout layout = rootView.findViewById(R.id.search_root_layout);
        AnimationDrawable animationDrawable = (AnimationDrawable) layout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        searchEditText = rootView.findViewById(R.id.query_text);
        spinner = rootView.findViewById(R.id.spinner);
        searchButton = rootView.findViewById(R.id.query_search_btn);
        advSearchFrag = rootView.findViewById(R.id.adv_search_frag);

        rootView.findViewById(R.id.video_search_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.inflateVideoSearchFragment();
            }
        });

        setImageTypeSpinner();
        setSearchButton();
        inflateAdvSearchFrag();

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SearchFragListener) {
            listener = (SearchFragListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    private void setImageTypeSpinner() {
        String[] data = {"All", "Photo", "Illustration", "Vector"};

        ArrayAdapter adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item_selected, data);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setSpinnerEventsListener(new CustomSpinner.OnSpinnerEventsListener() {
            public void onSpinnerOpened() {
                spinner.setSelected(true);
            }

            public void onSpinnerClosed() {
                spinnerChoice = spinner.getSelectedItem().toString();
                spinner.setSelected(false);
            }
        });
    }

    /*
    Splits searchString using the spaces between words and creates distinct keywords from searchString.
    Then passes the queryString, spinnerChoice to the activity to make a network call.
     */
    private void setSearchButton() {
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchString = searchEditText.getText().toString();
                String queryString = "";
                String[] splitter = searchString.split(" ");
                if (splitter.length > 1) {
                    for (int i = 0; i < splitter.length; i++) {
                        if (i == splitter.length - 1) {
                            queryString += splitter[i];
                        } else {
                            queryString += splitter[i] + "+";
                        }
                    }
                } else {
                    queryString = splitter[0];
                }
                if (spinnerChoice != null) {
                    listener.defineSimpleQuery(queryString, makeLowerCaseFirstLetter(spinnerChoice));
                    listener.onSearchFragmentInteraction(capitalizeFirstLetter(searchString));
                    Log.d(TAG, "onClick Search: " + queryString + " " + makeLowerCaseFirstLetter(spinnerChoice));
                } else {
                    listener.defineSimpleQuery(queryString, defaultType);
                    listener.onSearchFragmentInteraction(capitalizeFirstLetter(searchString));
                    Log.d(TAG, "onClick Search: " + queryString + " " + defaultType);
                }
            }
        });
    }

    private void inflateAdvSearchFrag() {
        advSearchFrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.inflateAdvSearchFragment();
            }
        });
    }

    private String makeLowerCaseFirstLetter(String original) {
        if (original == null || original.length() == 0) {
            return original;
        }
        return original.substring(0, 1).toLowerCase() + original.substring(1);
    }

    private String capitalizeFirstLetter(String original) {
        if (original == null || original.length() == 0) {
            return original;
        }
        return original.substring(0, 1).toUpperCase() + original.substring(1);
    }
}
