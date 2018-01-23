package productions.darthplagueis.imagesearch.pixabay.retrofit.model.videos;

/**
 * Created by oleg on 1/20/18.
 */

public class VideoHits {

    private String picture_id;
    private Vids videos;
    private String tags;
    private String type;

    public String getPicture_id() {
        return picture_id;
    }

    public Vids getVideos() {
        return videos;
    }

    public String getTags() {
        return tags;
    }

    public String getType() {
        return type;
    }
}
