package productions.darthplagueis.imagesearch;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import java.util.List;

import productions.darthplagueis.imagesearch.fragment.images.PhotoHitsFragment;
import productions.darthplagueis.imagesearch.pixabay.retrofit.model.images.PhotoHits;
import productions.darthplagueis.imagesearch.pixabay.retrofit.model.images.PhotoResults;
import productions.darthplagueis.imagesearch.util.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImagesActivity extends BaseActivity implements PhotoHitsFragment.ResultsFragListener {

    private final String TAG = "ImagesActivity";
    private static RecyclerView.OnScrollListener scrollListener;
    private ProgressBar progressBar;
    private String[] searchStrings;
    private PhotoHitsFragment fragment;
    private boolean isAdvSearch;
    private boolean advEditor;
    private boolean loadMore;
    private int pageNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressBar = findViewById(R.id.images_progressbar);
        getIntentExtras();
        inflateResultsFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        setResultTitles();
        loadMore = true;
        pageNumber = 2;
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: " + "Called");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: " + "Called");
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_images;
    }

    @Override
    public void setScrollListener(GridLayoutManager layoutManager) {
        initializeScrollListener(layoutManager);
    }

    @Override
    public void onBackPressed() {
        int count = fragmentManager.getBackStackEntryCount();
        if (count == 0) {
            super.onBackPressed();
        } else {
            setResultTitles();
            fragmentManager.popBackStack();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public static RecyclerView.OnScrollListener getScrollListener() {
        return scrollListener;
    }

    private void getIntentExtras() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            searchStrings = extras.getStringArray(Constants.IMAGE_QUERY);
            Log.d(TAG, "getIntentExtras: " + extras.getBoolean(Constants.SEARCH));
            isAdvSearch = extras.getBoolean(Constants.SEARCH);
            advEditor = extras.getBoolean(Constants.ADV_EDITOR);
        }
        Log.d(TAG, "onCreate: " + searchStrings[0] + " " + searchStrings[1] + " " + searchStrings[2]);
        try {
            Log.d(TAG, "onCreate: " + searchStrings[3] + " " + searchStrings[4]);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    private void setResultTitles() {
        toolbar.setTitle("You Searched For");
        toolbar.setSubtitle(searchStrings[0]);
    }

    private void inflateResultsFragment() {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        fragment = (PhotoHitsFragment) fragmentManager.findFragmentByTag("hitsFrag");
        if (fragment == null) {
            transaction.add(R.id.results_fragment_container, new PhotoHitsFragment(), "hitsFrag");
        } else {
            transaction.replace(R.id.results_fragment_container, fragment, "hitsFrag");
        }
        transaction.commit();
    }

    private void initializeScrollListener(final GridLayoutManager layoutManager) {
        scrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int pastVisibleItems = layoutManager.findFirstVisibleItemPosition();

                if (dy > 0) {
                    if (loadMore) {
                        if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                            loadMore = false;
                            Log.d(TAG, "onScrolled: " + "isAdvSearch " + isAdvSearch);
                            if (!isAdvSearch) {
                                loadMoreResults();
                                Log.d(TAG, "onScrolled: " + "loadMoreResults ran");
                            } else {
                                loadMoreAdvResults();
                                Log.d(TAG, "onScrolled: " + "loadMore AdvResults ran");
                            }
                            progressBar.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        };
        Log.d(TAG, "initializeScrollListener: " + "scroller ran");
    }

    private void loadMoreResults() {
        Call<PhotoResults> call = pixabayGetter.getImageResults(API_KEY, searchStrings[1], searchStrings[2], pageNumber, 35);
        call.enqueue(new Callback<PhotoResults>() {
            @Override
            public void onResponse(Call<PhotoResults> call, Response<PhotoResults> response) {
                loadMore = true;
                if (response.isSuccessful()) {
                    PhotoResults photoResults = response.body();
                    List<PhotoHits> photoHits = photoResults.getHits();
                    dataProvider.clearPhotoHits();
                    dataProvider.updatePhotoHits(photoHits);
                    fragment = (PhotoHitsFragment) fragmentManager.findFragmentByTag("hitsFrag");
                    pushNewResultsToFrag();
                    pageNumber++;
                    Log.d(TAG, "onResponse more photoHits returned: " + photoHits.size());
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<PhotoResults> call, Throwable t) {
                loadMore = true;
                progressBar.setVisibility(View.GONE);
                t.printStackTrace();
            }
        });
    }

    private void loadMoreAdvResults() {
        Call<PhotoResults> call = pixabayGetter.getAdvImageResults(API_KEY, searchStrings[1], searchStrings[2], searchStrings[3], advEditor, searchStrings[4], pageNumber, 35);
        call.enqueue(new Callback<PhotoResults>() {
            @Override
            public void onResponse(Call<PhotoResults> call, Response<PhotoResults> response) {
                loadMore = true;
                if (response.isSuccessful()) {
                    PhotoResults photoResults = response.body();
                    List<PhotoHits> photoHits = photoResults.getHits();
                    dataProvider.clearPhotoHits();
                    dataProvider.updatePhotoHits(photoHits);
                    pushNewResultsToFrag();
                    pageNumber++;
                    Log.d(TAG, "onResponse more photoHits returned: " + photoHits.size());
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<PhotoResults> call, Throwable t) {
                loadMore = true;
                progressBar.setVisibility(View.GONE);
                t.printStackTrace();
            }
        });
    }

    private void pushNewResultsToFrag() {
        fragment = (PhotoHitsFragment) fragmentManager.findFragmentByTag("hitsFrag");
        if (fragment != null) {
            fragment.updateAdapter();
        }
    }
}
