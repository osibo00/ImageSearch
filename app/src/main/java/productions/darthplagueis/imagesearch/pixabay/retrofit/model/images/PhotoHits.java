package productions.darthplagueis.imagesearch.pixabay.retrofit.model.images;

/**
 * Created by oleg on 1/11/18.
 */

public class PhotoHits {

    private int id;
    private int previewHeight;
    private int previewWidth;
    private String tags;
    private String pageURL;
    private String previewURL;
    private String webformatURL;
    private String type;

    public int getId() {
        return id;
    }

    public int getPreviewHeight() {
        return previewHeight;
    }

    public int getPreviewWidth() {
        return previewWidth;
    }

    public String getTags() {
        return tags;
    }

    public String getPageURL() {
        return pageURL;
    }

    public String getPreviewURL() {
        return previewURL;
    }

    public String getWebformatURL() {
        return webformatURL;
    }

    public String getType() {
        return type;
    }
}
