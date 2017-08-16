package com.example.doublejk.laboum.retrofit;

import com.google.gson.JsonObject;

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
 * Created by sonchangwoo on 2017. 1. 1..
 */

public interface RetroBaseApiService {

    final String Base_URL = "https://www.googleapis.com/youtube/v3/";

    @GET("search?part=snippet&type=video&videoCategoryId=10")
    Call<JsonObject> getSearch(@Query("q") String word, @Query("key") String key, @Query("maxResults") int maxResults);

    @GET("videos?part=snippet&chart=mostPopular&regionCode=KR&videoCategoryId=10")
    Call<JsonObject> getPopularSearch(@Query("key") String key, @Query("maxResults") int maxResults);


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
