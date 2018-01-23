package productions.darthplagueis.imagesearch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import java.util.List;

import productions.darthplagueis.imagesearch.fragment.images.AdvancedSearchFragment;
import productions.darthplagueis.imagesearch.fragment.LoadingFragment;
import productions.darthplagueis.imagesearch.fragment.videos.VideoSearchFragment;
import productions.darthplagueis.imagesearch.fragment.images.SearchFragListener;
import productions.darthplagueis.imagesearch.fragment.images.ImageSearchFragment;
import productions.darthplagueis.imagesearch.pixabay.retrofit.model.images.PhotoHits;
import productions.darthplagueis.imagesearch.pixabay.retrofit.model.images.PhotoResults;
import productions.darthplagueis.imagesearch.pixabay.retrofit.model.videos.VideoHits;
import productions.darthplagueis.imagesearch.pixabay.retrofit.model.videos.VideoResults;
import productions.darthplagueis.imagesearch.util.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends BaseActivity implements SearchFragListener, VideoSearchFragment.VideoSearchFragmentListener {

    private final String TAG = "SearchActivity";
    private String videoQueryStr;
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
    public void onSearchFragmentInteraction(String title) {
        inflateLoadFragment(title);
    }

    @Override
    public void inflateAdvSearchFragment() {
        inflateAdvFragment();
    }

    @Override
    public void inflateVideoSearchFragment() {
        inflateVideoFragment();
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
    public void defineVideoQuery(String queryString) {
        videoQueryStr = queryString;
        getVideoResults();
    }

    @Override
    public void onVideoFragmentInteraction(String title) {
        inflateLoadFragment(title);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void inflateSearchFragment() {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        ImageSearchFragment fragment = (ImageSearchFragment) fragmentManager.findFragmentByTag("searchFrag");
        if (fragment == null) {
            transaction.add(R.id.search_fragment_container, new ImageSearchFragment(), "searchFrag");
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

    private void inflateVideoFragment() {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        VideoSearchFragment fragment = (VideoSearchFragment) fragmentManager.findFragmentByTag("advSearchFrag");
        if (fragment == null) {
            transaction.replace(R.id.search_fragment_container, new VideoSearchFragment(), "advSearchFrag");
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
        Call<PhotoResults> call = pixabayGetter.getImageResults(API_KEY, queryString, imageTypeString, 1, 20);
        call.enqueue(new Callback<PhotoResults>() {
            @Override
            public void onResponse(Call<PhotoResults> call, Response<PhotoResults> response) {
                if (response.isSuccessful()) {
                    PhotoResults photoResults = response.body();
                    List<PhotoHits> photoHits = photoResults.getHits();
                    dataProvider.clearPhotoHits();
                    dataProvider.updatePhotoHits(photoHits);
                    startImagesActivity(false);
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
        Call<PhotoResults> call = pixabayGetter.getAdvImageResults(API_KEY, advQueryStr, advImageTypeStr, advCategoryStr, advEditor, advOrderStr, 1, 20);
        call.enqueue(new Callback<PhotoResults>() {
            @Override
            public void onResponse(Call<PhotoResults> call, Response<PhotoResults> response) {
                if (response.isSuccessful()) {
                    PhotoResults photoResults = response.body();
                    List<PhotoHits> photoHits = photoResults.getHits();
                    dataProvider.clearPhotoHits();
                    dataProvider.updatePhotoHits(photoHits);
                    startImagesActivity(true);
                    Log.d(TAG, "onResponse Adv photoHits returned: " + photoHits.size());
                }
            }

            @Override
            public void onFailure(Call<PhotoResults> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void getVideoResults() {
        Call<VideoResults> call = pixabayGetter.getVideoResults(API_KEY, videoQueryStr, 1,5);
        call.enqueue(new Callback<VideoResults>() {
            @Override
            public void onResponse(Call<VideoResults> call, Response<VideoResults> response) {
                if (response.isSuccessful()) {
                    VideoResults videoResults = response.body();
                    List<VideoHits> videoHits = videoResults.getHits();
                    dataProvider.clearVideoHits();
                    dataProvider.clearBitmaps();
                    dataProvider.updateVideoHits(videoHits);
                    startVideoActivity();
                    Log.d(TAG, "onResponse videoHits returned: " + videoHits.size());
                }
            }

            @Override
            public void onFailure(Call<VideoResults> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void startImagesActivity(boolean isAdvSearch) {
        Bundle bundle = new Bundle();
        Intent intent = new Intent(SearchActivity.this, ImagesActivity.class);
        if (!isAdvSearch) {
            bundle.putBoolean(Constants.SEARCH, isAdvSearch);
            String[] searchStrings = new String[]{resultsTitle, queryString, imageTypeString};
            bundle.putStringArray(Constants.IMAGE_QUERY, searchStrings);
            intent.putExtras(bundle);
            startActivity(intent);
        } else {
            bundle.putBoolean(Constants.SEARCH, isAdvSearch);
            String[] advSearchStrings = new String[]{resultsTitle, advQueryStr, advImageTypeStr, advCategoryStr, advOrderStr};
            bundle.putStringArray(Constants.IMAGE_QUERY, advSearchStrings);
            bundle.putBoolean(Constants.ADV_EDITOR, advEditor);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    private void startVideoActivity() {
        Bundle bundle = new Bundle();
        String[] videoStrings = new String[]{resultsTitle, videoQueryStr};
        bundle.putStringArray(Constants.VIDEO_QUERY, videoStrings);
        Intent intent = new Intent(SearchActivity.this, VideoActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
