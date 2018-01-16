package productions.darthplagueis.imagesearch;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import java.util.List;

import productions.darthplagueis.imagesearch.controller.PhotoHitsAdapter;
import productions.darthplagueis.imagesearch.fragment.AdvancedSearchFragment;
import productions.darthplagueis.imagesearch.fragment.FragmentListener;
import productions.darthplagueis.imagesearch.fragment.LoadingFragment;
import productions.darthplagueis.imagesearch.fragment.PhotoHitsFragment;
import productions.darthplagueis.imagesearch.fragment.SearchFragment;
import productions.darthplagueis.imagesearch.pixabay.retrofit.PixabayGetter;
import productions.darthplagueis.imagesearch.pixabay.retrofit.PixabayRetrofit;
import productions.darthplagueis.imagesearch.pixabay.retrofit.model.PhotoHits;
import productions.darthplagueis.imagesearch.pixabay.retrofit.model.PhotoResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements FragmentListener {

    public static final String API_KEY = BuildConfig.API_KEY;
    private static PhotoHitsAdapter adapter;
    private static RecyclerView.OnScrollListener scrollListener;
    private static Toolbar toolbar;
    private ProgressBar progressBar;
    private FragmentManager fragmentManager;
    private FrameLayout advSearchLayout;
    private PixabayGetter getter;
    private final String TAG = "Main Activity";
    private String queryString;
    private String imageTypeString;
    private String resultsTitle;
    private String advQueryStr;
    private String advImageTypeStr = "all";
    private String advCategoryStr = "";
    private boolean advEditorStr = false;
    private String advOrderStr = "popular";
    private boolean loadMore = false;
    private int pageNumber = 2;
    private int advPageNumber = 1;
    private boolean isAdvSearch = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.main_toolbar);
        progressBar = findViewById(R.id.main_progressbar);
        advSearchLayout = findViewById(R.id.small_fragment_container);

        fragmentManager = getSupportFragmentManager();
        inflateSearchFragment();

        setRetrofit();

        adapter = new PhotoHitsAdapter();

    }

    private void inflateSearchFragment() {
        toolbar.setTitle("Search");
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        SearchFragment fragment = (SearchFragment) fragmentManager.findFragmentByTag("searchFrag");
        if (fragment == null) {
            transaction.replace(R.id.fragment_container, new SearchFragment(), "searchFrag");
        } else {
            transaction.replace(R.id.fragment_container, fragment, "searchFrag");
        }
        transaction.addToBackStack("search");
        transaction.commit();
    }

    @Override
    public void inflateAdvSearchFragment() {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        AdvancedSearchFragment fragment = (AdvancedSearchFragment) fragmentManager.findFragmentByTag("advSearchFrag");
        if (fragment == null) {
            transaction.replace(R.id.small_fragment_container, new AdvancedSearchFragment(), "advSearchFrag");
        } else {
            transaction.replace(R.id.small_fragment_container, fragment, "advSearchFrag");
        }
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void setRetrofit() {
        PixabayRetrofit pixabayRetrofit = PixabayRetrofit.getInstanceOfRetrofit();
        getter = pixabayRetrofit.pixabayGetter();
    }


    @Override
    public void inflateLoadingFragment(String title) {
        resultsTitle = title + " results";
        String loadingTitle = "Searching " + title;
        toolbar.setTitle(loadingTitle);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        LoadingFragment fragment = (LoadingFragment) fragmentManager.findFragmentByTag("loadingFrag");
        if (fragment == null) {
            transaction.replace(R.id.fragment_container, new LoadingFragment(), "loadingFrag");
        } else {
            transaction.replace(R.id.fragment_container, fragment, "loadingFrag");
        }
        transaction.addToBackStack(null);
        transaction.commit();
    }


    @Override
    public void defineSimpleQuery(String query, String imageType) {
        queryString = query;
        imageTypeString = imageType;
        getPhotoHits();
    }

    /*
    By default when this call is made the page that is loaded is page 1, which is why in loadMorePhotoHits()
    the call that is made starts with pageNumber = 2.
    By default 20 results are returned from this call.
     */
    private void getPhotoHits() {
        Call<PhotoResults> call = getter.getPhotoResults(API_KEY, queryString, imageTypeString);
        call.enqueue(new Callback<PhotoResults>() {
            @Override
            public void onResponse(Call<PhotoResults> call, Response<PhotoResults> response) {
                if (response.isSuccessful()) {
                    loadMore = true;
                    PhotoResults photoResults = response.body();
                    List<PhotoHits> photoHits = photoResults.getHits();
                    adapter.clearList();
                    adapter.createList(photoHits);
                    inflateResultsFragment();
                    Log.d(TAG, "onResponse photoHits returned: " + photoHits.size());
                }
            }

            @Override
            public void onFailure(Call<PhotoResults> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void defineAdvQuery(String query) {
        advQueryStr = query;
        getAdvResults();
    }

    @Override
    public void defineImageType(String imageType) {
        advImageTypeStr = imageType;
    }

    @Override
    public void defineCategory(String category) {
        advCategoryStr = category;
    }

    @Override
    public void defineEditorChoice(String editor) {
        if (editor.equalsIgnoreCase("yes")) {
            advEditorStr = true;
        }
    }

    @Override
    public void defineOrder(String order) {
        advOrderStr = order;

    }

    private void getAdvResults() {
        Call<PhotoResults> call = getter.getAdvResults(API_KEY, advQueryStr, advImageTypeStr, advCategoryStr, advEditorStr, advOrderStr, advPageNumber, 20);
        call.enqueue(new Callback<PhotoResults>() {
            @Override
            public void onResponse(Call<PhotoResults> call, Response<PhotoResults> response) {
                if (response.isSuccessful()) {
                    loadMore = true;
                    isAdvSearch = true;
                    PhotoResults photoResults = response.body();
                    List<PhotoHits> photoHits = photoResults.getHits();
                    adapter.clearList();
                    adapter.createList(photoHits);
                    advSearchLayout.setVisibility(View.GONE);
                    inflateResultsFragment();
                    advPageNumber++;
                    Log.d(TAG, "onResponse more photoHits returned: " + photoHits.size());
                }
            }

            @Override
            public void onFailure(Call<PhotoResults> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void getMoreAdvResults() {
        Call<PhotoResults> call = getter.getAdvResults(API_KEY, advQueryStr, advImageTypeStr, advCategoryStr, advEditorStr, advOrderStr, advPageNumber, 20);
        call.enqueue(new Callback<PhotoResults>() {
            @Override
            public void onResponse(Call<PhotoResults> call, Response<PhotoResults> response) {
                loadMore = true;
                if (response.isSuccessful()) {
                    PhotoResults photoResults = response.body();
                    List<PhotoHits> photoHits = photoResults.getHits();
                    adapter.updateList(photoHits);
                    advPageNumber++;
                    Log.d(TAG, "onResponse more photoHits returned: " + photoHits.size());
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<PhotoResults> call, Throwable t) {
                loadMore = true;
                t.printStackTrace();
            }
        });
    }

    private void inflateResultsFragment() {
        toolbar.setTitle(resultsTitle);
        fragmentManager.popBackStack();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        PhotoHitsFragment fragment = (PhotoHitsFragment) fragmentManager.findFragmentByTag("hitsFrag");
        if (fragment == null) {
            transaction.replace(R.id.fragment_container, new PhotoHitsFragment(), "hitsFrag");
        } else {
            transaction.replace(R.id.fragment_container, fragment, "hitsFrag");
        }
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void getLayoutManager(GridLayoutManager layoutManager) {
        setScrollListener(layoutManager);
    }

    private void setScrollListener(final GridLayoutManager layoutManager) {
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
                            if (isAdvSearch) {
                                getMoreAdvResults();
                            } else {
                                loadMorePhotoHits();
                            }
                            progressBar.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        };
    }

    private void loadMorePhotoHits() {
        Call<PhotoResults> call = getter.getMoreResults(API_KEY, queryString, imageTypeString, pageNumber, 35);
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

    public static Toolbar getToolbar() {
        return toolbar;
    }

    public static PhotoHitsAdapter getAdapter() {
        return adapter;
    }

    public static RecyclerView.OnScrollListener getScrollListener() {
        return scrollListener;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        toolbar.setTitle("Search");
        pageNumber = 2;
        advPageNumber = 1;
        loadMore = false;
        isAdvSearch = false;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
