package productions.darthplagueis.imagesearch;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.List;

import productions.darthplagueis.imagesearch.controller.PhotoHitsAdapter;
import productions.darthplagueis.imagesearch.fragment.LoadingFragment;
import productions.darthplagueis.imagesearch.fragment.PhotoHitsFragment;
import productions.darthplagueis.imagesearch.pixabay.retrofit.PixabayGetter;
import productions.darthplagueis.imagesearch.pixabay.retrofit.PixabayRetrofit;
import productions.darthplagueis.imagesearch.pixabay.retrofit.model.PhotoHits;
import productions.darthplagueis.imagesearch.pixabay.retrofit.model.PhotoResults;
import productions.darthplagueis.imagesearch.util.DataProvider;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "Main Activity";
    private static final String API_KEY = BuildConfig.API_KEY;
    private PixabayGetter pixabayGetter;
    private String queryString = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final FrameLayout frameLayout = findViewById(R.id.fragment_container);
        final LinearLayout linearLayout = findViewById(R.id.layout_container);
        final EditText queryText = findViewById(R.id.query_text);

        findViewById(R.id.query_search_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchString = queryText.getText().toString();
                String[] splitter = searchString.split(" ");
                for (int i = 0; i < splitter.length; i++) {
                    queryString += splitter[i] + "+";
                }
                getPhotoHits(queryString);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.add(R.id.fragment_container, new LoadingFragment());
                fragmentTransaction.commit();

                linearLayout.setVisibility(View.GONE);
                frameLayout.setVisibility(View.VISIBLE);
            }
        });

        PixabayRetrofit pr = PixabayRetrofit.getInstanceOfRetrofit();
        pixabayGetter = pr.pixabayGetter();


    }

    private void getPhotoHits(String query) {
        Call<PhotoResults> call = pixabayGetter.getPhotoResults(API_KEY, query, "photo");
        call.enqueue(new Callback<PhotoResults>() {
            @Override
            public void onResponse(Call<PhotoResults> call, Response<PhotoResults> response) {
                if (response.isSuccessful()) {
                    queryString = "";
                    PhotoResults photoResults = response.body();
                    List<PhotoHits> photoHits = photoResults.getHits();
                    DataProvider.addPhotoHits(photoHits);
                    Log.d(TAG, "onResponse: photoHits returned " + photoHits.size());

                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, new PhotoHitsFragment()).addToBackStack("photos");
                    fragmentTransaction.commit();
                }
            }

            @Override
            public void onFailure(Call<PhotoResults> call, Throwable t) {
                call.clone();
                t.printStackTrace();
            }
        });
    }
}
