package com.example.doublejk.laboum.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by doublejk on 2017-08-29.
 */

public class AccessToken {
    private String access_token;
    private String token_type;
    private Integer expires_in;
    private String refresh_token;

    public String getAccess_token() {
        return access_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public Integer getExpires_in() {
        return expires_in;
    }

    public String getRefresh_token() {
        return refresh_token;
    }
}