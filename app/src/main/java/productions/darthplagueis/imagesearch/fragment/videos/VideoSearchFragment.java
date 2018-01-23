package productions.darthplagueis.imagesearch.fragment.videos;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import productions.darthplagueis.imagesearch.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class VideoSearchFragment extends Fragment {

    private VideoSearchFragmentListener listener;
    private EditText videoEditText;
    private Button videoSearchButton;

    public VideoSearchFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_video_search, container, false);

        videoEditText = rootView.findViewById(R.id.video_query_text);
        videoSearchButton = rootView.findViewById(R.id.video_search_btn);

        setVideoSearchButton();
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof VideoSearchFragmentListener) {
            listener = (VideoSearchFragmentListener) context;
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

    private void setVideoSearchButton() {
        videoSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchString = videoEditText.getText().toString();
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
                listener.defineVideoQuery(queryString);
                listener.onVideoFragmentInteraction(capitalizeFirstLetter(searchString));
            }
        });
    }

    private String capitalizeFirstLetter(String original) {
        if (original == null || original.length() == 0) {
            return original;
        }
        return original.substring(0, 1).toUpperCase() + original.substring(1);
    }

    public interface VideoSearchFragmentListener {
        void defineVideoQuery(String queryString);
        void onVideoFragmentInteraction(String title);
    }

}
