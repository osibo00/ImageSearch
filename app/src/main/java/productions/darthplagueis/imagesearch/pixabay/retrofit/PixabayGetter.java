package productions.darthplagueis.imagesearch.pixabay.retrofit;

import productions.darthplagueis.imagesearch.pixabay.retrofit.model.images.PhotoResults;
import productions.darthplagueis.imagesearch.pixabay.retrofit.model.videos.VideoResults;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by oleg on 1/11/18.
 */

public interface PixabayGetter {

    String imageEndPoint = "api/";
    String videoEndPoint = "api/videos/";

    @GET(imageEndPoint)
    Call<PhotoResults> getImageResults(@Query("key") String apiKey, @Query("q") String query, @Query("image_type") String imageType, @Query("page") int page, @Query("per_page") int perPage);

    @GET(imageEndPoint)
    Call<PhotoResults> getAdvImageResults(@Query("key") String apiKey, @Query("q") String query, @Query("image_type") String imageType, @Query("category") String category, @Query("editors_choice") Boolean editor,
                                          @Query("order") String order, @Query("page") int page, @Query("per_page") int perPage);

    @GET(videoEndPoint)
    Call<VideoResults> getVideoResults(@Query("key") String apiKey, @Query("q") String query, @Query("page") int page, @Query("per_page") int perPage);
}
