package com.example.doublejk.laboum.retrofit;

import android.content.Context;
import android.util.Log;

import com.example.doublejk.laboum.Oauth;
import com.example.doublejk.laboum.firebase.FirebaseMessage;
import com.example.doublejk.laboum.model.AccessToken;
import com.google.gson.JsonObject;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by doublejk on 2017-08-29.
 */

public class OauthRetroClient {
    private OauthService oauthService;
    public static String baseUrl = OauthService.Base_URL;
    private static Context mContext;
    private static Retrofit retrofit;

    private static class SingletonHolder {
        private static OauthRetroClient INSTANCE = new OauthRetroClient(mContext);
    }

    public static OauthRetroClient getInstance(Context context) {
        if (context != null) {
            mContext = context;
        }
        return OauthRetroClient.SingletonHolder.INSTANCE;
    }

    private OauthRetroClient(Context context) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100,TimeUnit.SECONDS).build();
        retrofit = new Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseUrl)
                .client(client)
                .build();
    }

    public OauthRetroClient createBaseApi() {
        oauthService = create(OauthService.class);
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

    public void getAccessToken(String code, String client_id, String client_secret, String redirect_uri, String grant_type, final RetroCallback callback) {
        oauthService.getAccessToken(code, client_id, client_secret, redirect_uri, grant_type).enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                if (response.isSuccessful()) {
                    Log.d("AccessToken", "Success " + response.body().toString());
                    Oauth.access_token = response.body().getAccess_token();
                    Oauth.refresh_token = response.body().getRefresh_token();
                    callback.onSuccess(response.code(), response.body());

                } else {
                    Log.d("AccessToken", "Fail" + response.code());
                    callback.onFailure(response.code());
                }
            }

            @Override
            public void onFailure(Call<AccessToken> call, Throwable t) {
                Log.d("AccessToken", "" + t.toString());
                callback.onError(t);
            }
        });
    }

    public void getRefreshAccessToken(String client_id, String client_secret, String refresh_token, String grant_type, final RetroCallback callback) {
        oauthService.getRefreshAccessToken(client_id, client_secret, refresh_token, grant_type).enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                if (response.isSuccessful()) {
                    Log.d("AccessToken", "Success " + response.body().toString());
                    Oauth.access_token = response.body().getAccess_token();
                    callback.onSuccess(response.code(), response.body());

                } else {
                    Log.d("AccessToken", "Fail" + response.code());
                    callback.onFailure(response.code());
                }
            }

            @Override
            public void onFailure(Call<AccessToken> call, Throwable t) {
                Log.d("AccessToken", "" + t.toString());
                callback.onError(t);
            }
        });
    }
}
