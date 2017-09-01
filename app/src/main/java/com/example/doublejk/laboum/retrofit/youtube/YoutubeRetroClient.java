package com.example.doublejk.laboum.retrofit.youtube;


import android.content.Context;
import android.util.Log;

import com.example.doublejk.laboum.retrofit.JsonParsing;
import com.example.doublejk.laboum.retrofit.RetroCallback;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by sonchangwoo on 2017. 1. 6..
 */

public class YoutubeRetroClient {

    private YoutubeApiService youtubeApiService;
    public static String baseUrl = YoutubeApiService.Base_URL;
    private static Context mContext;
    private static Retrofit retrofit;

    private static class SingletonHolder {
        private static YoutubeRetroClient INSTANCE = new YoutubeRetroClient(mContext);
    }

    public static YoutubeRetroClient getInstance(Context context) {
        if (context != null) {
            mContext = context;
        }
        return SingletonHolder.INSTANCE;
    }

    private YoutubeRetroClient(Context context) {
        retrofit = new Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseUrl)
                .build();
    }

    public YoutubeRetroClient createBaseApi() {
        youtubeApiService = create(YoutubeApiService.class);
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
    public void getTest(final RetroCallback callback) {
        youtubeApiService.getTest().enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Log.d("롸롸", "" + response.body().toString());
                    callback.onSuccess(response.code(), response.body());

                } else {
                    Log.d("롸롸", "Fail" + response.code());
                    callback.onFailure(response.code());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("롸롸", "Fail" + t);
                callback.onError(t);
            }
        });
    }

    public void getPopularTrack(String id, String key, final int maxResults, final RetroCallback callback) {
        youtubeApiService.getPopularTrack(id, key, maxResults).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    Log.d("getPopularTrack", "" + response.body().toString());
                    if(maxResults == 1) {
                        callback.onSuccess(response.code(), new JsonParsing(response.body()).homeBanner());
                    }else {
                        callback.onSuccess(response.code(), new JsonParsing(response.body()).popularTracks());
                    }

                } else {
                    Log.d("getPopularTrack", "Fail");
                    callback.onFailure(response.code());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("aaaa", "Fail");
                callback.onError(t);
            }
        });
    }

    public void getPopularSearch(String key, int maxResults, final RetroCallback callback) {
        youtubeApiService.getPopularSearch(key, maxResults).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.code(), new JsonParsing(response.body()).popularSearchParsing());

                } else {
                    Log.d("getPopularSearch", "Fail");
                    callback.onFailure(response.code());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("getPopularSearch", "Fail");
                callback.onError(t);
            }
        });
    }

    public void getActivities(int maxResults, final RetroCallback callback) {
        youtubeApiService.getActivities(maxResults).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    Log.d("getActivities!!", "" + response.body().toString());
                    callback.onSuccess(response.code(), response.body());

                } else {
                    Log.d("getActivities", "Fail" + response.code());
                    callback.onFailure(response.code());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("aaaa", "Fail" + t);
                callback.onError(t);
            }
        });
    }

    public void getSearch(String word, String key, int maxResults, final RetroCallback callback) {
        youtubeApiService.getSearch(word, key, maxResults).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.code(), new JsonParsing(response.body()).parsing());

                } else {
                    Log.d("getSearch", "Fail");
                    callback.onFailure(response.code());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("getSearch", "Fail");
                callback.onError(t);
            }
        });
    }

}
