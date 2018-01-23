package productions.darthplagueis.imagesearch.fragment.videos;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import productions.darthplagueis.imagesearch.R;
import productions.darthplagueis.imagesearch.VideoActivity;
import productions.darthplagueis.imagesearch.controller.VideoHitsAdapter;
import productions.darthplagueis.imagesearch.util.InsetDecoration;
import productions.darthplagueis.imagesearch.util.PixabayDataProvider;

public class VideoHitsFragment extends Fragment {

    private VideoHitsFragmentListener listener;
    private VideoHitsAdapter adapter;
    private PixabayDataProvider dataProvider;

    public VideoHitsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_video_hits, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        RecyclerView recyclerView = rootView.findViewById(R.id.video_recycler_view);

        adapter = new VideoHitsAdapter();
        dataProvider = PixabayDataProvider.getInstanceOfPDP();

        adapter.createBitmapsList(dataProvider.getBitmapList());
        recyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(rootView.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addItemDecoration(new InsetDecoration(rootView.getContext(), true));

        listener.setScrollListener(layoutManager);
        recyclerView.addOnScrollListener(VideoActivity.getScrollListener());

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof VideoHitsFragmentListener) {
            listener = (VideoHitsFragmentListener) context;
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
        adapter.updateList(dataProvider.getBitmapList());
    }

    public interface VideoHitsFragmentListener {
        void setScrollListener(LinearLayoutManager layoutManager);
    }
}
