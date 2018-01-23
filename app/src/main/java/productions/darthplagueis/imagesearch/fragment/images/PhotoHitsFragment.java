package productions.darthplagueis.imagesearch.fragment.images;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import productions.darthplagueis.imagesearch.ImagesActivity;
import productions.darthplagueis.imagesearch.R;
import productions.darthplagueis.imagesearch.controller.PhotoHitsAdapter;
import productions.darthplagueis.imagesearch.util.InsetDecoration;
import productions.darthplagueis.imagesearch.util.PixabayDataProvider;


public class PhotoHitsFragment extends Fragment {

    private ResultsFragListener listener;
    private final String TAG = "Photo Hits Fragment";
    private PhotoHitsAdapter adapter;
    private PixabayDataProvider dataProvider;

    public PhotoHitsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_photo_hits, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        RecyclerView recyclerView = rootView.findViewById(R.id.recycler_view);

        adapter = new PhotoHitsAdapter();
        dataProvider = PixabayDataProvider.getInstanceOfPDP();
        adapter.createList(dataProvider.getPhotoHitsList());
        recyclerView.setAdapter(adapter);

        GridLayoutManager layoutManager = new GridLayoutManager(rootView.getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addItemDecoration(new InsetDecoration(rootView.getContext(), false));

        listener.setScrollListener(layoutManager);
        recyclerView.addOnScrollListener(ImagesActivity.getScrollListener());

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ResultsFragListener) {
            listener = (ResultsFragListener) context;
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

    public void updateAdapter() {
        adapter.updateList(dataProvider.getPhotoHitsList());
    }

    public interface ResultsFragListener{
        void setScrollListener(GridLayoutManager layoutManager);
    }

}
