package productions.darthplagueis.imagesearch.pixabay.retrofit;

import productions.darthplagueis.imagesearch.pixabay.retrofit.model.PhotoResults;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by oleg on 1/11/18.
 */

public interface PixabayGetter {
    String searchEndPoint = "api/";

    @GET(searchEndPoint)
    Call<PhotoResults> getPhotoResults(@Query("key") String apiKey, @Query("q") String query, @Query("image_type") String imageType);

    @GET(searchEndPoint)
    Call<PhotoResults> getAllResults(@Query("key") String apiKey, @Query("q") String query, @Query("image_type") String imageType, @Query("page") int page, @Query("per_page") int perPage);
}
