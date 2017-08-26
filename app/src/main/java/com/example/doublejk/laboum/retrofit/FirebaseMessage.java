package com.example.doublejk.laboum.retrofit;

import com.google.gson.annotations.SerializedName;

/**
 * Created by doublejk on 2017-08-27.
 */

public class FirebaseMessage {
    @SerializedName("to")
    private String to;
    @SerializedName("data")
    private Data data;

    public FirebaseMessage(String to, String key, String message) {
        this.to = to;
        this.data = new Data(key, message);
    }

    public class Data {
        private String key;
        private String message;

        public Data(String key, String message) {
            this.key = key;
            this.message = message;
        }

        public String getKey() {
            return key;
        }

        public String getMessage() {
            return message;
        }
    }

    public String getTo() {
        return to;
    }

    public Data getData() {
        return data;
    }
}
