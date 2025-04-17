package vn.iotstar.videoshortapi;

import retrofit2.Call;
import retrofit2.http.GET;
import vn.iotstar.videoshortapi.Model.MessageVideoModel;

public interface APIService {
    @GET("getvideos.php")
    Call<MessageVideoModel> getVideos();
}
