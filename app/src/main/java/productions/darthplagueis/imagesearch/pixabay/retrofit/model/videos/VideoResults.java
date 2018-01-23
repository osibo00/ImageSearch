package productions.darthplagueis.imagesearch.pixabay.retrofit.model.videos;

import java.util.List;

/**
 * Created by oleg on 1/20/18.
 */

public class VideoResults {

    private int totalHits;
    private List<VideoHits> hits;

    public int getTotalHits() {
        return totalHits;
    }

    public List<VideoHits> getHits() {
        return hits;
    }
}
