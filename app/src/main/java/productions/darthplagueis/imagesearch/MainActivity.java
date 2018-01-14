package productions.darthplagueis.imagesearch;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.support.v7.widget.Toolbar;

import java.util.List;

import productions.darthplagueis.imagesearch.controller.PhotoHitsAdapter;
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
    private final String TAG = "Main Activity";
    private static Toolbar toolbar;
    private FragmentManager fragmentManager;
    private PixabayGetter getter;
    private String queryString;
    private static PhotoHitsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.main_toolbar);
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

    private void setRetrofit() {
        PixabayRetrofit pixabayRetrofit = PixabayRetrofit.getInstanceOfRetrofit();
        getter = pixabayRetrofit.pixabayGetter();
    }


    @Override
    public void inflateLoadingFragment() {
        toolbar.setTitle(queryString);
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
    public void defineSearchQuery(String query) {
        queryString = query;
        getPhotoHits(queryString);
    }

    private void getPhotoHits(String query) {
        Call<PhotoResults> call = getter.getPhotoResults(API_KEY, query, "photo");
        call.enqueue(new Callback<PhotoResults>() {
            @Override
            public void onResponse(Call<PhotoResults> call, Response<PhotoResults> response) {
                if (response.isSuccessful()) {
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
                call.clone();
                t.printStackTrace();
            }
        });
    }

    private void inflateResultsFragment() {
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
    public void pageListener(int page) {
        loadMorePhotoHits(page);
    }

    private void loadMorePhotoHits(int page) {
        Call<PhotoResults> call = getter.getAllResults(API_KEY, queryString, "photo", page, 25);
        call.enqueue(new Callback<PhotoResults>() {
            @Override
            public void onResponse(Call<PhotoResults> call, Response<PhotoResults> response) {
                if (response.isSuccessful()) {
                    PhotoResults photoResults = response.body();
                    List<PhotoHits> photoHits = photoResults.getHits();
                    adapter.updateList(photoHits);
                    Log.d(TAG, "onResponse more photoHits returned: " + photoHits.size());
                }
            }

            @Override
            public void onFailure(Call<PhotoResults> call, Throwable t) {
                call.clone();
                t.printStackTrace();
            }
        });
    }

    public static PhotoHitsAdapter getAdapter() {
        return adapter;
    }

    public static Toolbar getToolbar() {
        return toolbar;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        toolbar.setTitle("Search");
        return true;
    }
}
