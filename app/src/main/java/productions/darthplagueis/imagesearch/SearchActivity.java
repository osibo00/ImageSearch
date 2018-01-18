package productions.darthplagueis.imagesearch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import java.util.List;

import productions.darthplagueis.imagesearch.fragment.AdvancedSearchFragment;
import productions.darthplagueis.imagesearch.fragment.LoadingFragment;
import productions.darthplagueis.imagesearch.fragment.SearchFragListener;
import productions.darthplagueis.imagesearch.fragment.SearchFragment;
import productions.darthplagueis.imagesearch.pixabay.retrofit.model.PhotoHits;
import productions.darthplagueis.imagesearch.pixabay.retrofit.model.PhotoResults;
import productions.darthplagueis.imagesearch.util.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends BaseActivity implements SearchFragListener {

    private final String TAG = "SearchActivity";
    private String resultsTitle;
    private String queryString;
    private String imageTypeString;
    private String advQueryStr;
    private String advImageTypeStr;
    private String advCategoryStr;
    private String advOrderStr;
    private boolean advEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateSearchFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        toolbar.setTitle("Pixabay Search");
        resultsTitle = "";
        queryString = "";
        imageTypeString = "all";
        advQueryStr = "";
        advImageTypeStr = "all";
        advCategoryStr = "";
        advOrderStr = "popular";
        advEditor = false;
        fragmentManager.popBackStack();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }
    
    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_search;
    }

    @Override
    public void inflateLoadingFragment(String title) {
        inflateLoadFragment(title);
    }

    @Override
    public void inflateAdvSearchFragment() {
        inflateAdvFragment();
    }

    @Override
    public void defineSimpleQuery(String query, String imageType) {
        queryString = query;
        imageTypeString = imageType;
        getResults();
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
            advEditor = true;
        }
    }

    @Override
    public void defineOrder(String order) {
        advOrderStr = order;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void inflateSearchFragment() {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        SearchFragment fragment = (SearchFragment) fragmentManager.findFragmentByTag("searchFrag");
        if (fragment == null) {
            transaction.add(R.id.search_fragment_container, new SearchFragment(), "searchFrag");
        } else {
            transaction.replace(R.id.search_fragment_container, fragment, "searchFrag");
        }
        transaction.commit();
    }

    private void inflateAdvFragment() {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        AdvancedSearchFragment fragment = (AdvancedSearchFragment) fragmentManager.findFragmentByTag("advSearchFrag");
        if (fragment == null) {
            transaction.add(R.id.search_fragment_container, new AdvancedSearchFragment(), "advSearchFrag");
        } else {
            transaction.replace(R.id.search_fragment_container, fragment, "advSearchFrag");
        }
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void inflateLoadFragment(String title) {
        resultsTitle = title;
        String loadingTitle = "Searching " + title;
        toolbar.setTitle(loadingTitle);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        LoadingFragment fragment = (LoadingFragment) fragmentManager.findFragmentByTag("loadingFrag");
        if (fragment == null) {
            transaction.replace(R.id.search_fragment_container, new LoadingFragment(), "loadingFrag");
        } else {
            transaction.replace(R.id.search_fragment_container, fragment, "loadingFrag");
        }
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void getResults() {
        Call<PhotoResults> call = pixabayGetter.getPhotoResults(API_KEY, queryString, imageTypeString);
        call.enqueue(new Callback<PhotoResults>() {
            @Override
            public void onResponse(Call<PhotoResults> call, Response<PhotoResults> response) {
                if (response.isSuccessful()) {
                    PhotoResults photoResults = response.body();
                    List<PhotoHits> photoHits = photoResults.getHits();
                    adapter.clearList();
                    adapter.createList(photoHits);
                    startResultsActivity(false);
                    Log.d(TAG, "onResponse photoHits returned: " + photoHits.size());
                }
            }

            @Override
            public void onFailure(Call<PhotoResults> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void getAdvResults() {
        Call<PhotoResults> call = pixabayGetter.getAdvResults(API_KEY, advQueryStr, advImageTypeStr, advCategoryStr, advEditor, advOrderStr, 1, 20);
        call.enqueue(new Callback<PhotoResults>() {
            @Override
            public void onResponse(Call<PhotoResults> call, Response<PhotoResults> response) {
                if (response.isSuccessful()) {
                    PhotoResults photoResults = response.body();
                    List<PhotoHits> photoHits = photoResults.getHits();
                    adapter.clearList();
                    adapter.createList(photoHits);
                    startResultsActivity(true);
                    Log.d(TAG, "onResponse Adv photoHits returned: " + photoHits.size());
                }
            }

            @Override
            public void onFailure(Call<PhotoResults> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void startResultsActivity(boolean isAdvSearch) {
        Bundle bundle = new Bundle();
        Intent intent = new Intent(SearchActivity.this, ResultsActivity.class);
        if (!isAdvSearch) {
            bundle.putBoolean(Constants.SEARCH, isAdvSearch);
            String[] searchStrings = new String[]{resultsTitle, queryString, imageTypeString};
            bundle.putStringArray(Constants.QUERY_STRING, searchStrings);
            intent.putExtras(bundle);
            startActivity(intent);
        } else {
            bundle.putBoolean(Constants.SEARCH, isAdvSearch);
            String[] advSearchStrings = new String[]{resultsTitle, advQueryStr, advImageTypeStr, advCategoryStr, advOrderStr};
            bundle.putStringArray(Constants.QUERY_STRING, advSearchStrings);
            bundle.putBoolean(Constants.ADV_EDITOR, advEditor);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
}
