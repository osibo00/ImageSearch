package productions.darthplagueis.imagesearch.pixabay.retrofit.model;

import java.util.List;

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
