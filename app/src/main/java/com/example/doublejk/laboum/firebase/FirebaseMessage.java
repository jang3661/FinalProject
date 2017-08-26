package com.example.doublejk.laboum.firebase;

import com.google.gson.annotations.SerializedName;

/**
 * Created by doublejk on 2017-08-27.
 */

public class FirebaseMessage {
    @SerializedName("to")
    private String to;
    @SerializedName("data")
    private Data data;

    public FirebaseMessage(String to, String action) {
        this.to = "/topics/" + to;
        this.data = new Data(action);
    }

    public class Data {
        private String action;

        public Data(String action) {
            this.action = action;

        }

        public String getAction() {
            return action;
        }
    }

    public String getTo() {
        return to;
    }
    public Data getData() {
        return data;
    }
}
