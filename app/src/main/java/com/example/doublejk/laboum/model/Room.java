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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPlaylistName() {
        return playlistName;
    }

    public void setPlaylistName(String playlistName) {
        this.playlistName = playlistName;
    }

    public int getListenerCount() {
        return listenerCount;
    }

    public void setListenerCount(int listenerCount) {
        this.listenerCount = listenerCount;
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }
}
