package productions.darthplagueis.imagesearch.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import productions.darthplagueis.imagesearch.MainActivity;
import productions.darthplagueis.imagesearch.R;
import productions.darthplagueis.imagesearch.controller.PhotoHitsAdapter;
import productions.darthplagueis.imagesearch.util.DataProvider;

/**
 * A simple {@link Fragment} subclass.
 */
public class PhotoHitsFragment extends Fragment {

    private View rootView;
    private RecyclerView recyclerView;


    public PhotoHitsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_photo_hits, container, false);

        recyclerView = rootView.findViewById(R.id.recycler_view);
        PhotoHitsAdapter adapter = new PhotoHitsAdapter();
        adapter.createList(DataProvider.getPhotoHitsList());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(rootView.getContext(), 2));

        return rootView;
    }

}
