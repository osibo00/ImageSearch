package productions.darthplagueis.imagesearch.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

import productions.darthplagueis.imagesearch.BuildConfig;
import productions.darthplagueis.imagesearch.MainActivity;
import productions.darthplagueis.imagesearch.R;
import productions.darthplagueis.imagesearch.pixabay.retrofit.PixabayGetter;
import productions.darthplagueis.imagesearch.pixabay.retrofit.PixabayRetrofit;
import productions.darthplagueis.imagesearch.pixabay.retrofit.model.PhotoHits;
import productions.darthplagueis.imagesearch.pixabay.retrofit.model.PhotoResults;
import productions.darthplagueis.imagesearch.util.DataProvider;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    FragmentListener listener;
    private final String TAG = "Search Fragment";
    private View rootView;
    private EditText searchEditText;
    private Button searchButton;


    public SearchFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_search, container, false);

        ((AppCompatActivity) getActivity()).setSupportActionBar(MainActivity.getToolbar());
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(false);

        searchEditText = rootView.findViewById(R.id.query_text);
        searchButton = rootView.findViewById(R.id.query_search_btn);

        setSearchButton();

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (FragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement FragmentListener");
        }
    }

    private void setSearchButton() {
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchString = searchEditText.getText().toString();
                String queryString = "";
                String[] splitter = searchString.split(" "); // Separates searchString by spaces between words into distinct keywords.
                if (splitter.length > 1) {
                    for (int i = 0; i < splitter.length; i++) {
                        if (i == splitter.length -1) {
                            queryString += splitter[i];
                        } else {
                            queryString += splitter[i] + "+";
                        }
                    }
                } else {
                    queryString = splitter[0];
                }
                listener.defineSearchQuery(queryString);
                listener.inflateLoadingFragment();
                Log.d(TAG, "onClick Search: " + queryString);
            }
        });
    }
}
