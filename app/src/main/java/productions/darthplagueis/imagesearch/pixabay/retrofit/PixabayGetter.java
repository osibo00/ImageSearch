package productions.darthplagueis.imagesearch.pixabay.retrofit;

import productions.darthplagueis.imagesearch.pixabay.retrofit.model.PhotoResults;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Query;

/**
 * Created by oleg on 1/11/18.
 */

public interface PixabayGetter {

    @GET("api/")
    Call<PhotoResults> getPhotoResults(@Query("key") String apiKey, @Query("q") String query, @Query("image_type") String imageType);
}
