package com.example.doublejk.laboum.retrofit;

import com.example.doublejk.laboum.firebase.FirebaseMessage;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by doublejk on 2017-08-27.
 */

public interface FCMService {
    final String  Base_URL = "https://fcm.googleapis.com/fcm/";

    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAQmzagBA:APA91bHlmUNtzFL0WEP3ASMQG9F_nkrDbIwFhyG1XMNxHGiwmT-dxTkB1aCoCWyQk_JbG4xVEgSqNZ4NI-Qu7h5-T3_-9BukGCIVyclja9IT-P_y1o_mamfX2_yJJXfqgz45hSjNi6wN"
    })
    @POST("send")
    Call<JsonObject> postGroupMsg(@Body FirebaseMessage firebaseMessage);
}
