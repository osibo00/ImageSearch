package productions.darthplagueis.imagesearch;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import java.util.List;

import productions.darthplagueis.imagesearch.fragment.PhotoHitsFragment;
import productions.darthplagueis.imagesearch.fragment.fragmentlisteners.ResultsFragListener;
import productions.darthplagueis.imagesearch.pixabay.retrofit.model.PhotoHits;
import productions.darthplagueis.imagesearch.pixabay.retrofit.model.PhotoResults;
import productions.darthplagueis.imagesearch.util.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResultsActivity extends BaseActivity implements ResultsFragListener {

    private final String TAG = "ResultsActivity";
    private static RecyclerView.OnScrollListener scrollListener;
    private ProgressBar progressBar;
    private String[] searchStrings;
    private boolean isAdvSearch;
    private boolean advEditor;
    private boolean loadMore;
    private int pageNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressBar = findViewById(R.id.results_progressbar);

        getIntentExtras();

        Log.d(TAG, "onCreate: " + searchStrings[0] + " " + searchStrings[1] + " " + searchStrings[2]);
        try {
            Log.d(TAG, "onCreate: " + searchStrings[3] + " " + searchStrings[4]);
        } catch (IndexOutOfBoundsException e) {
        }

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
    protected int getLayoutResource() {
        return R.layout.activity_results;
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

    private void setResultTitles() {
        toolbar.setTitle("You Searched For");
        toolbar.setSubtitle(searchStrings[0]);
    }

    private void getIntentExtras() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            searchStrings = extras.getStringArray(Constants.QUERY_STRING);
            Log.d(TAG, "getIntentExtras: " + extras.getBoolean(Constants.SEARCH));
            isAdvSearch = extras.getBoolean(Constants.SEARCH);
            advEditor = extras.getBoolean(Constants.ADV_EDITOR);
        }
    }

    private void inflateResultsFragment() {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        PhotoHitsFragment fragment = (PhotoHitsFragment) fragmentManager.findFragmentByTag("hitsFrag");
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
        Call<PhotoResults> call = pixabayGetter.getMoreResults(API_KEY, searchStrings[1], searchStrings[2], pageNumber, 35);
        call.enqueue(new Callback<PhotoResults>() {
            @Override
            public void onResponse(Call<PhotoResults> call, Response<PhotoResults> response) {
                loadMore = true;
                if (response.isSuccessful()) {
                    PhotoResults photoResults = response.body();
                    List<PhotoHits> photoHits = photoResults.getHits();
                    adapter.updateList(photoHits);
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
        Call<PhotoResults> call = pixabayGetter.getAdvResults(API_KEY, searchStrings[1], searchStrings[2], searchStrings[3], advEditor, searchStrings[4], pageNumber, 35);
        call.enqueue(new Callback<PhotoResults>() {
            @Override
            public void onResponse(Call<PhotoResults> call, Response<PhotoResults> response) {
                loadMore = true;
                if (response.isSuccessful()) {
                    PhotoResults photoResults = response.body();
                    List<PhotoHits> photoHits = photoResults.getHits();
                    adapter.updateList(photoHits);
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

}
