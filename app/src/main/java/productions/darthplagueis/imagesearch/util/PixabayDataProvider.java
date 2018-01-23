package productions.darthplagueis.imagesearch.util;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

import productions.darthplagueis.imagesearch.pixabay.retrofit.model.images.PhotoHits;
import productions.darthplagueis.imagesearch.pixabay.retrofit.model.videos.VideoBitmaps;
import productions.darthplagueis.imagesearch.pixabay.retrofit.model.videos.VideoHits;

/**
 * Created by oleg on 1/22/18.
 */

public class PixabayDataProvider {

    private static PixabayDataProvider instanceOfPDP;

    private List<PhotoHits> photoHitsList;
    private List<VideoHits> videoHitsList;
    private List<VideoBitmaps> bitmapList;

    private PixabayDataProvider() {
        photoHitsList = new ArrayList<>();
        videoHitsList = new ArrayList<>();
        bitmapList = new ArrayList<>();
    }

    public static PixabayDataProvider getInstanceOfPDP() {
        if (instanceOfPDP != null) {
            return instanceOfPDP;
        }
        instanceOfPDP = new PixabayDataProvider();
        return instanceOfPDP;
    }

    public void updatePhotoHits(List<PhotoHits> newList) {
        photoHitsList.addAll(newList);
    }

    public void updateVideoHits(List<VideoHits> newList) {
        videoHitsList.addAll(newList);
    }

    public void updateBitmaps(List<VideoBitmaps> newList) {
        bitmapList.addAll(newList);
    }

    public List<PhotoHits> getPhotoHitsList() {
        return photoHitsList;
    }

    public List<VideoHits> getVideoHitsList() {
        return videoHitsList;
    }

    public List<VideoBitmaps> getBitmapList() {
        return bitmapList;
    }

    public void clearPhotoHits() {
        photoHitsList.clear();
    }

    public void clearVideoHits() {
        videoHitsList.clear();
    }

    public void clearBitmaps() {
        bitmapList.clear();
    }
}
