package com.example.doublejk.laboum.retrofit;

import com.example.doublejk.laboum.model.Music;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

/**
 * Created by doublejk on 2017-08-09.
 */

public class JsonParsing {
    JsonObject jsonObject;
    ArrayList<Music> musics;

    public JsonParsing(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
        musics = new ArrayList<>();
    }

    public ArrayList<Music> parsing() {
        JsonArray jsonArray = jsonObject.getAsJsonArray("items");
        for (int i = 0; i < jsonArray.size(); i++) {
            Music music = new Music();
            if (jsonArray.get(i).getAsJsonObject().get("id")
                    .getAsJsonObject().get("kind").toString().equals("\"youtube#video\"")) {
                music.setVideoId(subString(jsonArray.get(i).getAsJsonObject().get("id")
                        .getAsJsonObject().get("videoId").toString()));
                music.setTitle(subString(jsonArray.get(i).getAsJsonObject().get("snippet")
                        .getAsJsonObject().get("title").toString()));
                music.setImgUrl(subString(jsonArray.get(i).getAsJsonObject().get("snippet")
                        .getAsJsonObject().get("thumbnails").getAsJsonObject().get("medium")
                        .getAsJsonObject().get("url").toString()));
                musics.add(music);
            }
        }
        return musics;
    }

    public ArrayList<Music> popularSearchParsing() {
        JsonArray jsonArray = jsonObject.getAsJsonArray("items");
        for (int i = 0; i < jsonArray.size(); i++) {
            Music music = new Music();
            music.setVideoId(subString(jsonArray.get(i).getAsJsonObject().get("id").toString()));
            music.setTitle(subString(jsonArray.get(i).getAsJsonObject().get("snippet")
                    .getAsJsonObject().get("title").toString()));
            music.setImgUrl(subString(jsonArray.get(i).getAsJsonObject().get("snippet")
                    .getAsJsonObject().get("thumbnails").getAsJsonObject().get("medium")
                    .getAsJsonObject().get("url").toString()));
            musics.add(music);
        }
        return musics;
    }

    public String subString(String s) {
        return s.substring(1, s.length() - 1);
    }
}
