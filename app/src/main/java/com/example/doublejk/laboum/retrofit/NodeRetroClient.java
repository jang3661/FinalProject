package com.example.doublejk.laboum.retrofit;

import android.content.Context;
import android.util.Log;

import com.example.doublejk.laboum.model.User;
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
        retrofit = new Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseUrl)
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
