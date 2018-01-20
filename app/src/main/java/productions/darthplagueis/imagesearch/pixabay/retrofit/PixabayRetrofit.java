package productions.darthplagueis.imagesearch.pixabay.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by oleg on 1/11/18.
 */

public class PixabayRetrofit {

    private static Retrofit retrofit;

    private static PixabayRetrofit instanceOfRetrofit;

    private PixabayRetrofit() {
        String pixabayBaseUrl = "https://pixabay.com/";
        retrofit = new Retrofit.Builder()
                .baseUrl(pixabayBaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static PixabayRetrofit getInstanceOfRetrofit() {
        if (instanceOfRetrofit != null) {
            return instanceOfRetrofit;
        }
        instanceOfRetrofit = new PixabayRetrofit();
        return instanceOfRetrofit;
    }

    public static PixabayGetter pixabayGetter() {
        return retrofit.create(PixabayGetter.class);
    }
}
