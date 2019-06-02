package api;

import java.util.List;
import model.ImageResponse;
import model.Items;
import model.LoginSignupResponse;
import model.Users;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UserAPI {

    @POST("api/register")
    Call<Void> addUser(@Body Users users);

    @POST("api/auth")
    Call<LoginSignupResponse> checkUser(@Body Users users);

    @Multipart
    @POST("api/upload")
    Call<ImageResponse> uploadImage(@Part MultipartBody.Part img);

    @POST("api/additem")
    Call<Void> addItem(@Body Items items);

    @GET("api/items")
    Call<List<Items>> getAllItems();

}
