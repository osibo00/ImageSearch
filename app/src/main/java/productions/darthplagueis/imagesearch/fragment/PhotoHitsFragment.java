package productions.darthplagueis.imagesearch.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.List;

import productions.darthplagueis.imagesearch.BuildConfig;
import productions.darthplagueis.imagesearch.MainActivity;
import productions.darthplagueis.imagesearch.R;
import productions.darthplagueis.imagesearch.controller.PhotoHitsAdapter;
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
public class PhotoHitsFragment extends Fragment {

    FragmentListener listener;
    private final String TAG = "Photo Hits Fragment";
    private View rootView;
    private RecyclerView recyclerView;
    private GridLayoutManager layoutManager;
    private int pageNumber = 1;

    public PhotoHitsFragment() {}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (FragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement FragmentListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_photo_hits, container, false);

        ((AppCompatActivity) getActivity()).setSupportActionBar(MainActivity.getToolbar());
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        recyclerView = rootView.findViewById(R.id.recycler_view);

        recyclerView.setAdapter(MainActivity.getAdapter());
        layoutManager = new GridLayoutManager(rootView.getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int total = layoutManager.getItemCount();
                int firstVisibleItemCount = layoutManager.findFirstVisibleItemPosition();
                int lastVisibleItemCount = layoutManager.findLastVisibleItemPosition();

                //to avoid multiple calls to loadMore() method
                //maintain a boolean value (isLoading). if loadMore() task started set to true and completes set to false
                    if (total > 0)
                        if ((total - 1) == lastVisibleItemCount) {
                            pageNumber++;
                            listener.pageListener(pageNumber);
                        }
                }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

        });

        return rootView;
    }
}
