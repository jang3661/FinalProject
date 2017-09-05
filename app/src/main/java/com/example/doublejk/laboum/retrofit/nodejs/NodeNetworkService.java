package com.example.doublejk.laboum.retrofit.nodejs;

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
    final String Base_URL = "http://192.168.10.105:3000/";
    //final String  Base_URL = "http://1b1e6b75.ngrok.io/";

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

    @POST("room/exit")
    Call<String> postExitRoom(@Body Playlist playlist);
}