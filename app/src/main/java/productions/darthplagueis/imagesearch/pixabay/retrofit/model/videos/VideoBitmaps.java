package productions.darthplagueis.imagesearch.pixabay.retrofit.model.videos;

import android.graphics.Bitmap;

/**
 * Created by oleg on 1/23/18.
 */

public class VideoBitmaps {

    private Bitmap videoBitmap;
    private String videoUrl;

    public VideoBitmaps(Bitmap videoBitmap, String videoUrl) {
        this.videoBitmap = videoBitmap;
        this.videoUrl = videoUrl;
    }

    public Bitmap getVideoBitmap() {
        return videoBitmap;
    }

    public String getVideoUrl() {
        return videoUrl;
    }
}
