package com.example.doublejk.laboum.retrofit;


import android.content.Context;
import android.util.Log;

import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
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

    public void getPopularSearch(String key, int maxResults, final RetroCallback callback) {
        youtubeApiService.getPopularSearch(key, maxResults).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    //Log.d("aaaa", "" + response.body().toString());
                    callback.onSuccess(response.code(), new JsonParsing(response.body()).popularSearchParsing());

                } else {
                    Log.d("aaaa", "Fail");
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

    public void getActivities(int maxResults, final RetroCallback callback) {
        youtubeApiService.getActivities(maxResults).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    Log.d("getActivities!!", "" + response.body().toString());
                    callback.onSuccess(response.code(), response.body());

                } else {
                    Log.d("aaaa", "Fail" + response.code());
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
                    //Log.d("aaaa", "" + response.body().toString());
                    callback.onSuccess(response.code(), new JsonParsing(response.body()).parsing());

                } else {
                    Log.d("aaaa", "Fail");
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
    public void getFirst(String id, final RetroCallback callback) {
        youtubeApiService.getFirst(id).enqueue(new Callback<ResponseGet>() {
            @Override
            public void onResponse(Call<ResponseGet> call, Response<ResponseGet> response) {
                if (response.isSuccessful()) {
                    //Log.d("aaaa", response.body().toString());
                    callback.onSuccess(response.code(), response.body());
                } else {
                    callback.onFailure(response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseGet> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    public void getSecond(String id, final RetroCallback callback) {
        youtubeApiService.getSecond(id).enqueue(new Callback<List<ResponseGet>>() {
            @Override
            public void onResponse(Call<List<ResponseGet>> call, Response<List<ResponseGet>> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.code(), response.body());
                } else {
                    callback.onFailure(response.code());
                }
            }

            @Override
            public void onFailure(Call<List<ResponseGet>> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    public void postFirst(HashMap<String, Object> parameters, final RetroCallback callback) {
        youtubeApiService.postFirst(parameters).enqueue(new Callback<ResponseGet>() {
            @Override
            public void onResponse(Call<ResponseGet> call, Response<ResponseGet> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.code(), response.body());
                } else {
                    callback.onFailure(response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseGet> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    public void putFirst(HashMap<String, Object> parameters, final RetroCallback callback) {
        youtubeApiService.putFirst(new RequestPut(parameters)).enqueue(new Callback<ResponseGet>() {
            @Override
            public void onResponse(Call<ResponseGet> call, Response<ResponseGet> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.code(), response.body());
                } else {
                    callback.onFailure(response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseGet> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    public void patchFirst(String title, final RetroCallback callback) {
        youtubeApiService.patchFirst(title).enqueue(new Callback<ResponseGet>() {
            @Override
            public void onResponse(Call<ResponseGet> call, Response<ResponseGet> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.code(), response.body());
                } else {
                    callback.onFailure(response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseGet> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    public void deleteFirst(final RetroCallback callback) {
        youtubeApiService.deleteFirst().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.code(), response.body());
                } else {
                    callback.onFailure(response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callback.onError(t);
            }
        });
    }
}
