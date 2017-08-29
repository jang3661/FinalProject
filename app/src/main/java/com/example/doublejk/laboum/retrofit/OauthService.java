package com.example.doublejk.laboum.retrofit;

import com.example.doublejk.laboum.model.AccessToken;
import com.example.doublejk.laboum.model.User;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by doublejk on 2017-08-29.
 */

public interface OauthService {
    final String Base_URL = "https://accounts.google.com/";

    @FormUrlEncoded
    @POST("o/oauth2/token")
    Call<AccessToken> getAccessToken(
            @Field("code") String code,
            @Field("client_id") String clientId,
            @Field("client_secret") String clientSecret,
            @Field("redirect_uri") String redirectUri,
            @Field("grant_type") String grantType);

    @FormUrlEncoded
    @POST("/oauth/token")
    Call<AccessToken> getRefreshAccessToken(
            @Field("client_id") String clientId,
            @Field("client_secret") String clientSecret,
            @Field("refresh_token") String refreshToken,
            @Field("grant_type") String grantType);
}
