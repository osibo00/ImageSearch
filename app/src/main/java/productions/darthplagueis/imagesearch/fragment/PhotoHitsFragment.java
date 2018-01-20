package productions.darthplagueis.imagesearch.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import productions.darthplagueis.imagesearch.R;
import productions.darthplagueis.imagesearch.ResultsActivity;
import productions.darthplagueis.imagesearch.controller.PhotoHitsAdapter;
import productions.darthplagueis.imagesearch.fragment.fragmentlisteners.ResultsFragListener;
import productions.darthplagueis.imagesearch.util.InsetDecoration;


public class PhotoHitsFragment extends Fragment {

    ResultsFragListener listener;
    private final String TAG = "Photo Hits Fragment";

    public PhotoHitsFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (ResultsFragListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement FragListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_photo_hits, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        RecyclerView recyclerView = rootView.findViewById(R.id.recycler_view);

        PhotoHitsAdapter adapter = PhotoHitsAdapter.getInstanceOfAdapter();
        recyclerView.setAdapter(adapter);

        GridLayoutManager layoutManager = new GridLayoutManager(rootView.getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addItemDecoration(new InsetDecoration(rootView.getContext()));

        listener.setScrollListener(layoutManager);
        recyclerView.addOnScrollListener(ResultsActivity.getScrollListener());

        return rootView;
    }
}
