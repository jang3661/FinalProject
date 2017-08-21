package com.example.doublejk.laboum.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by doublejk on 2017-08-08.
 */

public class Music implements Parcelable{
    private String videoId;
    private String title;
    private String imgUrl;
    private boolean isSelected;
    private PaletteColor paletteColor;

    public Music() {}

    public Music(String videoId, String title, String imgUrl) {
        this.videoId = videoId;
        this.title = title;
        this.imgUrl = imgUrl;
    }

    protected Music(Parcel in) {
        videoId = in.readString();
        title = in.readString();
        imgUrl = in.readString();
        isSelected = in.readByte() != 0;
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

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
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
        dest.writeByte((byte) (isSelected ? 1 : 0));
        dest.writeParcelable(paletteColor, flags);
    }
}
