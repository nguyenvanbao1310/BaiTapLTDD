package vn.iotstar.uploadimages.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import vn.iotstar.uploadimages.api.ServiceAPI;
import vn.iotstar.uploadimages.constant.Const;

public class RetrofitClient {
    private static Retrofit retrofit = null;

    public static ServiceAPI getServiceAPI() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Const.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(ServiceAPI.class);
    }
}
