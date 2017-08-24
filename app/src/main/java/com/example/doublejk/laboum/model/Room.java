package com.example.doublejk.laboum.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by doublejk on 2017-08-24.
 */

public class Room {
    @SerializedName("title")
    private String title;
    @SerializedName("useremail")
    private String userEmail;
    @SerializedName("username")
    private String userName;
    @SerializedName("playlistname")
    private String playlistName;
    @SerializedName("listenercount")
    private int listenerCount;
    @SerializedName("playlist")
    @Expose
    private Playlist playlist;

    public Room(String title, String userEmail, String userName, String playlistName) {
        this.title = title;
        this.userEmail = userEmail;
        this.userName = userName;
        this.playlistName = playlistName;
        this.listenerCount = 1;
        this.playlist = new Playlist(playlistName, userEmail, userName);
    }
}
