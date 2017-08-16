package com.example.doublejk.laboum.model;

/**
 * Created by doublejk on 2017-08-12.
 */

public class Music {
    private String title;
    private String imgUrl;
    private String videoId;

    public Music(String title, String imgUrl, String videoId) {
        this.title = title;
        this.imgUrl = imgUrl;
        this.videoId = videoId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }
}
