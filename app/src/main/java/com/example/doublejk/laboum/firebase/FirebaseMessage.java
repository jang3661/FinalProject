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

    //topic용
    public FirebaseMessage(String to, String action) {
        this.to = "/topics/" + to;
        this.data = new Data(action);
    }
    //메세지용
    public FirebaseMessage(String to, String action, int playingMusicIndex, int currentMillis) {
        this.to = to;
        this.data = new Data(action, playingMusicIndex, currentMillis);
    }

    //메세지 요청용
    public FirebaseMessage(String to, String action, String token) {
        this.to = to;
        this.data = new Data(action, token);
    }

    public class Data {
        private String action;
        private String token;
        private int playingMusicIndex;
        private int currentMillis;

        public Data(String action) {
            this.action = action;

        }
        public Data(String action, String token) {
            this.action = action;
            this.token = token;
        }
        public Data(String action, int playingMusicIndex, int currentMillis) {
            this.action = action;
            this.playingMusicIndex = playingMusicIndex;
            this.currentMillis = currentMillis;
        }

        public int getPlayingMusicIndex() {
            return playingMusicIndex;
        }

        public int getDurationMillis() {
            return currentMillis;
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
