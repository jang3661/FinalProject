package com.example.doublejk.laboum.retrofit;

import android.content.Context;
import android.util.Log;

import com.example.doublejk.laboum.model.Playlist;
import com.example.doublejk.laboum.model.Room;
import com.example.doublejk.laboum.model.User;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by doublejk on 2017-08-20.
 */

public class NodeRetroClient {
    private NodeNetworkService nodeNetworkService;
    public static String baseUrl = NodeNetworkService.Base_URL;
    private static Context mContext;
    private static Retrofit retrofit;

    private static class SingletonHolder {
        private static NodeRetroClient INSTANCE = new NodeRetroClient(mContext);
    }

    public static NodeRetroClient getInstance(Context context) {
        if (context != null) {
            mContext = context;
        }
        return NodeRetroClient.SingletonHolder.INSTANCE;
    }

    private NodeRetroClient(Context context) {
//        OkHttpClient client = new OkHttpClient.Builder()
//                .connectTimeout(100, TimeUnit.SECONDS)
//                .readTimeout(100,TimeUnit.SECONDS).build();
        retrofit = new Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseUrl)
                //.client(client)
                .build();
    }

    public NodeRetroClient createBaseApi() {
        nodeNetworkService = create(NodeNetworkService.class);
        return this;
    }

    /**
     * create you ApiService
     * Create an implementation of the API endpoints defined by the {@code service} interface.
     */
    public  <T> T create(final Class<T> service) {
        if (service == null) {
            throw new RuntimeException("Api service is null!");
        }
        return retrofit.create(service);
    }

    public void getRoomList(final RetroCallback callback) {
        nodeNetworkService.getRoomList().enqueue(new Callback<ArrayList<Room>>() {
            @Override
            public void onResponse(Call<ArrayList<Room>> call, Response<ArrayList<Room>> response) {
                if (response.isSuccessful()) {
                    Log.d("getRoomList", "" + response.body().toString());
                    callback.onSuccess(response.code(), response.body());

                } else {
                    Log.d("getRoomList", "Fail" + response.code());
                    callback.onFailure(response.code());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Room>> call, Throwable t) {
                Log.d("getRoomList", "" + t.toString());
                callback.onError(t);
            }
        });
    }

    public void postEnterRoom(Room room, final RetroCallback callback) {
        nodeNetworkService.postEnterRoom(room).enqueue(new Callback<Playlist>() {
            @Override
            public void onResponse(Call<Playlist> call, Response<Playlist> response) {
                if (response.isSuccessful()) {
                    Log.d("postEnterRoom", "" + response.body().toString());
                    callback.onSuccess(response.code(), response.body());

                } else {
                    Log.d("postEnterRoom", "Fail" + response.code());
                    callback.onFailure(response.code());
                }
            }

            @Override
            public void onFailure(Call<Playlist> call, Throwable t) {
                Log.d("deleteRoom", "" + t.toString());
                callback.onError(t);
            }
        });
    }
    public void postDeleteRoom(Room room, final RetroCallback callback) {
        nodeNetworkService.postDeleteRoom(room).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Log.d("deleteRoom", "" + response.body().toString());
                    callback.onSuccess(response.code(), response.body());

                } else {
                    Log.d("deleteRoom", "Fail" + response.code());
                    callback.onFailure(response.code());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("deleteRoom", "" + t.toString());
                callback.onError(t);
            }
        });
    }

    public void postCreateRoom(Room room, final RetroCallback callback) {
        nodeNetworkService.postCreateRoom(room).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Log.d("createRoom", "" + response.body().toString());
                    callback.onSuccess(response.code(), response.body());

                } else {
                    Log.d("createRoom", "Fail" + response.code());
                    callback.onFailure(response.code());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("createRoom", "" + t.toString());
                callback.onError(t);
            }
        });
    }

    public void postLogin(User user, final RetroCallback callback) {
        nodeNetworkService.postLogin(user).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Log.d("postLogin", "" + response.body().toString());
                    callback.onSuccess(response.code(), response.body());

                } else {
                    Log.d("postLogin", "Fail" + response.code());
                    callback.onFailure(response.code());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("postLogin", "" + t.toString());
                callback.onError(t);
            }
        });
    }

    public void postUser(User user, final RetroCallback callback) {
        nodeNetworkService.postUser(user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    Log.d("롸롸", "" + response.body().toString());
                    callback.onSuccess(response.code(), response.body());

                } else {
                    Log.d("롸롸", "Fail" + response.code());
                    callback.onFailure(response.code());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("롸롸", "Fail");
                callback.onError(t);
            }
        });
    }
}
