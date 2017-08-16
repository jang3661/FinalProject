package com.example.doublejk.laboum.model;

import com.example.doublejk.laboum.R;

/**
 * Created by doublejk on 2017-08-11.
 */

public class Playlist {
    int playlistImg;
    private String title;
    private String musicCount;

    public Playlist(String title, String musicCount) {
        this.playlistImg = R.drawable.playlist;
        this.title = title;
        this.musicCount = musicCount + "곡";
    }

    public int getPlaylistImg() {
        return playlistImg;
    }

    public void setPlaylistImg(int playlistImg) {
        this.playlistImg = playlistImg;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMusicCount() {
        return musicCount;
    }

    public void setMusicCount(String musicCount) {
        this.musicCount = musicCount;
    }
}
