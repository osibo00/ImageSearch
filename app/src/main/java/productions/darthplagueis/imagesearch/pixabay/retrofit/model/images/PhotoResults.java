package productions.darthplagueis.imagesearch.pixabay.retrofit.model.images;

import java.util.List;

import productions.darthplagueis.imagesearch.pixabay.retrofit.model.images.PhotoHits;

/**
 * Created by oleg on 1/11/18.
 */

public class PhotoResults {

    private int totalHits;
    private List<PhotoHits> hits;

    public int getTotalHits() {
        return totalHits;
    }

    public List<PhotoHits> getHits() {
        return hits;
    }
}
