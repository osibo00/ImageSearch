package productions.darthplagueis.imagesearch.util;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import productions.darthplagueis.imagesearch.pixabay.retrofit.model.PhotoHits;

/**
 * Created by oleg on 1/11/18.
 */

public class DataProvider {
    private static final String TAG = "Data Provider";
    private static List<PhotoHits> photoHitsList = new ArrayList<>();

    public static void addPhotoHits(List<PhotoHits> photoHits) {
        photoHitsList.addAll(photoHits);
    }

    public static List<PhotoHits> getPhotoHitsList() {
        Log.d(TAG, "photoHitsList size: " + photoHitsList.size());
        return photoHitsList;
    }
}
