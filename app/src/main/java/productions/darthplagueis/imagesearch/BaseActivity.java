package productions.darthplagueis.imagesearch;


import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import productions.darthplagueis.imagesearch.controller.VideoHitsAdapter;
import productions.darthplagueis.imagesearch.pixabay.retrofit.PixabayGetter;
import productions.darthplagueis.imagesearch.pixabay.retrofit.PixabayRetrofit;
import productions.darthplagueis.imagesearch.util.PixabayDataProvider;

public abstract class BaseActivity extends AppCompatActivity {

    final String API_KEY = BuildConfig.API_KEY;
    Toolbar toolbar;
    FragmentManager fragmentManager;
    PixabayGetter pixabayGetter;
    PixabayDataProvider dataProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());
        configureToolbar();
        setFragmentManager();
        setRetrofit();
        initializeDataProvider();
    }

    protected abstract int getLayoutResource();

    private void setFragmentManager() {
        fragmentManager = getSupportFragmentManager();
    }

    private void configureToolbar() {
        toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setTitleTextColor(getResources().getColor(R.color.white));
            toolbar.setSubtitleTextColor(getResources().getColor(R.color.white));
        }
    }

    private void setRetrofit() {
        PixabayRetrofit pixabayRetrofit = PixabayRetrofit.getInstanceOfRetrofit();
        pixabayGetter = pixabayRetrofit.pixabayGetter();
    }

    private void initializeDataProvider() {
        dataProvider = PixabayDataProvider.getInstanceOfPDP();
    }
}
