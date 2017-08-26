package com.example.doublejk.laboum.retrofit;

import android.content.Context;
import android.util.Log;

import com.example.doublejk.laboum.model.Playlist;
import com.example.doublejk.laboum.model.Room;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by doublejk on 2017-08-27.
 */

public class FCMRetroClient {
    private FCMService fcmService;
    public static String baseUrl = FCMService.Base_URL;
    private static Context mContext;
    private static Retrofit retrofit;

    private static class SingletonHolder {
        private static FCMRetroClient INSTANCE = new FCMRetroClient(mContext);
    }

    public static FCMRetroClient getInstance(Context context) {
        if (context != null) {
            mContext = context;
        }
        return FCMRetroClient.SingletonHolder.INSTANCE;
    }

    private FCMRetroClient(Context context) {
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

    public FCMRetroClient createBaseApi() {
        fcmService = create(FCMService.class);
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

    public void postMessage(FirebaseMessage firebaseMessage, final RetroCallback callback) {
        fcmService.postMessage(firebaseMessage).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    Log.d("getRoomList", "" + response.body().toString());
                    callback.onSuccess(response.code(), response.body());

                } else {
                    Log.d("getRoomList", "Fail" + response.code());
                    callback.onFailure(response.code());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("getRoomList", "" + t.toString());
                callback.onError(t);
            }
        });
    }
}