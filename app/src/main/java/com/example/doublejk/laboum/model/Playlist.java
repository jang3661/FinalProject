package com.example.doublejk.laboum.model;

import com.example.doublejk.laboum.R;

import java.util.ArrayList;

/**
 * Created by doublejk on 2017-08-11.
 */

public class Playlist {
    private String title;
    private String musicCount;
    private ArrayList<Music> musics;
    private int playlistImg;
    private String userName;

    public Playlist(String title, String userName) {
        this.title = title;
        this.musicCount = "0곡";
        this.playlistImg = R.drawable.playlist;
        this.musics = new ArrayList<>();
        this.userName = userName;
    }

    public void rename() {}

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
