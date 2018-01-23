package productions.darthplagueis.imagesearch;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import productions.darthplagueis.imagesearch.fragment.LoadingFragment;
import productions.darthplagueis.imagesearch.fragment.videos.VideoHitsFragment;
import productions.darthplagueis.imagesearch.pixabay.retrofit.model.videos.VideoBitmaps;
import productions.darthplagueis.imagesearch.pixabay.retrofit.model.videos.VideoHits;
import productions.darthplagueis.imagesearch.pixabay.retrofit.model.videos.VideoResults;
import productions.darthplagueis.imagesearch.util.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoActivity extends BaseActivity implements VideoHitsFragment.VideoHitsFragmentListener {

    private final String TAG = "VideoActivity";
    private static RecyclerView.OnScrollListener scrollListener;
    private ProgressBar progressBar;
    private String[] searchStrings;
    private VideoHitsFragment fragment;
    private boolean hasInstantiated;
    private boolean loadMore;
    private int pageNumber;
    private List<VideoBitmaps> videoBitmapsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressBar = findViewById(R.id.videos_progressbar);
        getIntentExtras();
        inflateLoadFragment();

        videoBitmapsList = new ArrayList<>();
        AsyncTaskRunner runner = new AsyncTaskRunner();
        runner.execute();
    }

    @Override
    public void onResume() {
        super.onResume();
        setResultTitles();
        hasInstantiated = false;
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
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: " + "Called");
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_video;
    }

    @Override
    public void setScrollListener(LinearLayoutManager layoutManager) {
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
            searchStrings = extras.getStringArray(Constants.VIDEO_QUERY);
        }
    }

    private void setResultTitles() {
        toolbar.setTitle("You Searched For");
        toolbar.setSubtitle(searchStrings[0]);
    }

    private Bitmap retrieveVideoFrameFromVideo(String videoPath) throws Throwable {
        Bitmap bitmap;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            bitmap = mediaMetadataRetriever.getFrameAtTime();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Throwable("Exception in retrieveVideoFrameFromVideo(" + videoPath + ") " + e.getMessage());

        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }

    private void inflateLoadFragment() {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        LoadingFragment fragment = (LoadingFragment) fragmentManager.findFragmentByTag("loadingFrag");
        if (fragment == null) {
            transaction.replace(R.id.video_fragment_container, new LoadingFragment(), "loadingFrag");
        } else {
            transaction.replace(R.id.video_fragment_container, fragment, "loadingFrag");
        }
        transaction.commit();
    }

    private void inflateResultsFragment() {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        fragment = (VideoHitsFragment) fragmentManager.findFragmentByTag("videoHitsFrag");
        if (fragment == null) {
            transaction.add(R.id.video_fragment_container, new VideoHitsFragment(), "videoHitsFrag");
        } else {
            transaction.replace(R.id.video_fragment_container, fragment, "videoHitsFrag");
        }
        transaction.commit();
    }

    private void initializeScrollListener(final LinearLayoutManager layoutManager) {
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
                            getVideoResults();
                            progressBar.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        };
    }

    private void getVideoResults() {
        Call<VideoResults> call = pixabayGetter.getVideoResults(API_KEY, searchStrings[1], pageNumber, 5);
        call.enqueue(new Callback<VideoResults>() {
            @Override
            public void onResponse(Call<VideoResults> call, Response<VideoResults> response) {
                loadMore = true;
                if (response.isSuccessful()) {
                    VideoResults videoResults = response.body();
                    List<VideoHits> videoHits = videoResults.getHits();
                    dataProvider.clearVideoHits();
                    dataProvider.clearBitmaps();
                    dataProvider.updateVideoHits(videoHits);
                    fragment = (VideoHitsFragment) fragmentManager.findFragmentByTag("videoHitsFrag");
                    if (fragment != null) {
                        AsyncTaskRunner runner = new AsyncTaskRunner();
                        runner.execute();
                    }
                    pageNumber++;
                    Log.d(TAG, "onResponse videoHits returned: " + videoHits.size());
                }
            }

            @Override
            public void onFailure(Call<VideoResults> call, Throwable t) {
                loadMore = true;
                progressBar.setVisibility(View.GONE);
                t.printStackTrace();
            }
        });
    }

    private class AsyncTaskRunner extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            for (int i = 0; i < dataProvider.getVideoHitsList().size(); i++) {
                VideoHits hits = dataProvider.getVideoHitsList().get(i);
                String videoPath = hits.getVideos().getTiny().getUrl();
                try {
                    videoBitmapsList.add(new VideoBitmaps(retrieveVideoFrameFromVideo(videoPath), videoPath));
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void param) {
            super.onPostExecute(param);
            dataProvider.updateBitmaps(videoBitmapsList);
            videoBitmapsList.clear();
            setFragment();

        }
    }

    private void setFragment() {
        if (hasInstantiated) {
            fragment.updateAdapter();
        } else {
            hasInstantiated = true;
            inflateResultsFragment();
        }
    }
}

