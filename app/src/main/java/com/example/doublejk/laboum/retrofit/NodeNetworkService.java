package com.example.doublejk.laboum.retrofit;

import com.example.doublejk.laboum.model.Playlist;
import com.example.doublejk.laboum.model.Room;
import com.example.doublejk.laboum.model.User;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by doublejk on 2017-08-20.
 */

public interface NodeNetworkService {
    final String  Base_URL = "http://192.168.10.102:3000/";
    //final String  Base_URL = "http://172.20.10.2:3000/";

    @POST("login")
    Call<String> postLogin(@Body User user);

    @POST("user")
    Call<User> postUser(@Body User user);

    @POST("room/create")
    Call<String> postCreateRoom(@Body Room room);

    @POST("room/delete")
    Call<String> postDeleteRoom(@Body Room room);

    @GET("room/list")
    Call<ArrayList<Room>> getRoomList();

    @POST("room/enter")
    Call<Playlist> postEnterRoom(@Body Room room);







    @GET("videos?part=snippet&chart=mostPopular&regionCode=KR&videoCategoryId=10")
    Call<JsonObject> getPopularSearch(@Query("key") String key, @Query("maxResults") int maxResults);

    @GET("activities?part=snippet&home=true")
    Call<JsonObject> getActivities(@Query("maxResults") int maxResults);

    @GET("login")
    Call<String> getTest();


    @GET("/posts/{userId}")
    Call<ResponseGet> getFirst(@Path("userId") String id);

    @GET("/posts")
    Call<List<ResponseGet>> getSecond(@Query("userId") String id);

    @FormUrlEncoded
    @POST("/posts")
    Call<ResponseGet> postFirst(@FieldMap HashMap<String, Object> parameters);

    @PUT("/posts/1")
    Call<ResponseGet> putFirst(@Body RequestPut parameters);

    @FormUrlEncoded
    @PATCH("/posts/1")
    Call<ResponseGet> patchFirst(@Field("title") String title);

    @DELETE("/posts/1")
    Call<ResponseBody> deleteFirst();
}
