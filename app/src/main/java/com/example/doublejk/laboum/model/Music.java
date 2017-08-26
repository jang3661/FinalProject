package com.example.doublejk.laboum.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by doublejk on 2017-08-08.
 */

public class Music implements Parcelable{
    @SerializedName("videoid")
    private String videoId;
    @SerializedName("title")
    private String title;
    @SerializedName("imgurl")
    private String imgUrl;
    @SerializedName("palette")
    private PaletteColor paletteColor;

    public Music() {
        this.paletteColor = new PaletteColor();
    }

    public Music(String videoId, String title, String imgUrl) {
        this.videoId = videoId;
        this.title = title;
        this.imgUrl = imgUrl;
        this.paletteColor = new PaletteColor();
    }

    protected Music(Parcel in) {
        videoId = in.readString();
        title = in.readString();
        imgUrl = in.readString();
        paletteColor = in.readParcelable(PaletteColor.class.getClassLoader());
    }

    public static final Creator<Music> CREATOR = new Creator<Music>() {
        @Override
        public Music createFromParcel(Parcel in) {
            return new Music(in);
        }

        @Override
        public Music[] newArray(int size) {
            return new Music[size];
        }
    };

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
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

    public PaletteColor getPaletteColor() {
        return paletteColor;
    }

    public void setPaletteColor(PaletteColor paletteColor) {

        this.paletteColor = new PaletteColor(paletteColor);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(videoId);
        dest.writeString(title);
        dest.writeString(imgUrl);
        dest.writeParcelable(paletteColor, flags);
    }
}
