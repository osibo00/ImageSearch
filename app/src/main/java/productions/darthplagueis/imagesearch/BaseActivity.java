package productions.darthplagueis.imagesearch;


import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import productions.darthplagueis.imagesearch.controller.PhotoHitsAdapter;
import productions.darthplagueis.imagesearch.pixabay.retrofit.PixabayGetter;
import productions.darthplagueis.imagesearch.pixabay.retrofit.PixabayRetrofit;

public abstract class BaseActivity extends AppCompatActivity {

    final String API_KEY = BuildConfig.API_KEY;
    Toolbar toolbar;
    FragmentManager fragmentManager;
    PixabayGetter pixabayGetter;
    PhotoHitsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());
        configureToolbar();
        setFragmentManager();
        setRetrofit();
        setAdapter();
    }

    protected abstract int getLayoutResource();

    private void setFragmentManager() {
        fragmentManager = getSupportFragmentManager();
    }

    private void configureToolbar() {
        toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
    }

    private void setRetrofit() {
        PixabayRetrofit pixabayRetrofit = PixabayRetrofit.getInstanceOfRetrofit();
        pixabayGetter = pixabayRetrofit.pixabayGetter();
    }

    private void setAdapter() {
        adapter = PhotoHitsAdapter.getInstanceOfAdapter();
    }

}
